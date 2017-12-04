package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.StringACLCodec;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchingBehaviour extends OneShotBehaviour{
    Bee bee;
    private float pn=0;
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
                    StringACLCodec codec = new StringACLCodec(new StringReader((String)group.getValue()), null);
                    AID groupAID = codec.decodeAID();
                    bee.setVisitedGroup(groupAID);
                    //System.out.println(bee.getGroup().getLocalName());
                }while(bee.getVisitedGroup().toString().equals(bee.getGroup().toString()));

                int count =0;
                for (DFAgentDescription result1 : result) {
                    ServiceDescription service = (ServiceDescription) result1.getAllServices().next();
                    Iterator properties = service.getAllProperties();
                    Property p = (Property) properties.next();
                    StringACLCodec codec = new StringACLCodec(new StringReader((String)p.getValue()), null);
                    AID pAID = codec.decodeAID();
                    if(pAID.toString().equals(bee.getVisitedGroup().toString())){
                        count++;
                    }
                }
                pn = (count*100f/Integer.parseInt((String)groupSize.getValue()));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onEnd() {
        int r = rand.nextInt(100);
        if(pn > r){
            return 1;
        }
        else
            return 0;
            
    }
    
}
