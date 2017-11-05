package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DancingBehaviour extends SimpleBehaviour
{    
    Bee bee;
    private double s=1, theta=1;
    private final double alpha = 0.02;
    private float utility;
    private Random rand;
    
    public DancingBehaviour(Bee a){
        super(a);
        bee = a;
        rand = new Random();
    }
    
    private boolean added = false;
    int count = 0;
    
    @Override
    public void action() {
        this.utility = bee.getUtility();
        bee.receiveMessage(bee.DANCING);
        if (!added) {
            //System.out.println(myAgent.getLocalName() + " is dancing");
            ServiceDescription service = new ServiceDescription();
            service.setType("DANCING");

            Property p1 = new Property("group", bee.getGroup());
            Property p2 = new Property("groupSize", bee.groupMembers.size() + 1);
            service.addProperties(p1);
            service.addProperties(p2);

            service.setName(myAgent.getLocalName());
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.addServices(service);
            added = true;
            try {
                DFService.register(myAgent, dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(DancingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean done(){
        if(bee.getUtility() > this.utility){
            s+= alpha;
            theta -= alpha;
        }
        else if(bee.getUtility() < this.utility){
            s-= alpha;
            theta += alpha;
        }
        double pn = Math.pow(s, 2)/(Math.pow(s, 2)+Math.pow(theta, 2));
        int r = rand.nextInt(101);
        //System.out.println(myAgent.getAID().getLocalName()+": "+pn*100+" "+ r);
        return pn*100<r;
    }
    
    @Override
    public int onEnd() { 
        try
        {
            DFService.deregister(myAgent);
            //System.out.println(myAgent.getAID().getLocalName() + " is watching");
            added = false;
        }
        catch(FIPAException e)
        {
            e.printStackTrace();
        }
        return 1;
    }
    
}