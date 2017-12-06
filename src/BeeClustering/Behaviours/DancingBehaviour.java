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
    private double s=1, theta=0.5;
    private final double alpha = 0.02;
    private double utility;
    private Random rand;
    DFAgentDescription dfd;
    ServiceDescription service;
    
    public DancingBehaviour(Bee a){
        super(a);
        bee = a;
        rand = new Random();
    }
    
    private boolean added = false;
    int count = 0;
    
    @Override
    public void action() {
        try {
            this.utility = bee.getGroupUtility(Bee.DANCING);
        } catch (InterruptedException ex) {
            Logger.getLogger(DancingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        bee.receiveMessage(bee.DANCING);
        if (!added) {
            //System.out.println(myAgent.getLocalName() + " is dancing");
            service = new ServiceDescription();
            service.setType("DANCING");

            Property p1 = new Property("group", bee.getGroup().toString());
            
            Property p2 = new Property("groupSize", bee.getGroupSize());
            service.addProperties(p1);
            service.addProperties(p2);

            service.setName(myAgent.getLocalName());
            dfd = new DFAgentDescription();
            dfd.addServices(service);
            added = true;
            try {
                DFService.register(myAgent, dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean done(){
        double newUtility=0;
        try {
            newUtility = bee.getGroupUtility(bee.DANCING);
        } catch (InterruptedException ex) {
            Logger.getLogger(DancingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(newUtility > utility){
            s+= alpha;
            theta -= alpha;
        }
        else if(newUtility < utility){
            s-= alpha;
            theta += alpha;
        }
        
        utility = newUtility;
        double pd = Math.pow(s, 2)/(Math.pow(s, 2)+Math.pow(theta, 2));
        int r = rand.nextInt(100);
        return pd*100<r;
    }
    
    @Override
    public int onEnd() { 
        try
        {
            dfd.removeServices(service);
            DFService.deregister(myAgent);
            added = false;
        }
        catch(FIPAException e)
        {
            e.printStackTrace();
        }
        return 1;
    }
    
    
}
