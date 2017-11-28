package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchingBehaviour extends OneShotBehaviour{
    Bee bee;
    private float pn;
    private Property group, groupSize;
    private Random rand;
    
    public WatchingBehaviour(Bee a){
        super(a);
        bee = a;
        rand = new Random();
    }
    
    @Override
    public void action() {
        bee.receiveMessage(bee.WATCHING);
        DFAgentDescription agentDescription = new DFAgentDescription();

        ServiceDescription sd = new ServiceDescription();
        sd.setType("DANCING"); 

        agentDescription.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, agentDescription);
            if (result.length > 0){
                
                do{
                    int r = rand.nextInt(result.length);    
                    ServiceDescription service = (ServiceDescription)result[r].getAllServices().next();
                    Iterator  properties = service.getAllProperties();
                    group = (Property)properties.next();
                    groupSize = (Property)properties.next();
                    bee.setVisitedBee(result[r].getName());
                    bee.setVisitedGroup((AID)group.getValue());
                }while(bee.getVisitedGroup() == bee.getGroup());

                int count =0;
                for (DFAgentDescription result1 : result) {
                    ServiceDescription service = (ServiceDescription) result1.getAllServices().next();
                    Iterator properties = service.getAllProperties();
                    Property p = (Property) properties.next();
                    if((AID)p.getValue() == (AID)group.getValue()){
                        count++;
                    }
                }
                
                pn = (count/Integer.parseInt((String)groupSize.getValue())) * 100;
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onEnd() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(WatchingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
        int r = rand.nextInt(100);
        if(pn > r){
            return 1;
        }
        else
            return 0;
            
    }
    
}
