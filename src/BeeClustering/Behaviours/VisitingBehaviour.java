package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class VisitingBehaviour extends OneShotBehaviour
{
    private Bee bee;
    int returnValue;
    
    public VisitingBehaviour(Bee a){
        super(a);
        bee = a;
    }    

    @Override
    public void action() {
        bee.receiveMessage(bee.VISITING);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setSender(myAgent.getAID());
        message.addReceiver(bee.getVisitedBee());
        myAgent.send(message);
        
        MessageTemplate MT = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        message = myAgent.receive(MT);
        while(message==null)
            message = myAgent.receive(MT);
          
        String content = message.getContent();
        Scanner scan = new Scanner(content);
        scan.useLocale(Locale.US);
        float x = scan.nextFloat();
        float y = scan.nextFloat();
        
        double distance = Math.sqrt(Math.pow(bee.getX() - x, 2) + Math.pow(bee.getY() -y, 2));
        double pa = (Bee.maxDistance-distance)/Bee.maxDistance;
        pa *= pa*100;

        Random rand = new Random();
        int r = rand.nextInt(100);
        
        if(pa > r){
            double utility = bee.getGroupUtility();
            ACLMessage removeBee = new ACLMessage(ACLMessage.INFORM);
            removeBee.addReceiver(bee.getGroup());
            removeBee.setContent("Remove");
            bee.send(removeBee);
            double newUtility = bee.getGroupUtility();
            if(newUtility > utility){
                bee.setGroup(bee.getVisitedGroup());
            }
            returnValue = 1;
            
            ACLMessage addBee = new ACLMessage(ACLMessage.INFORM);
            addBee.addReceiver(bee.getGroup());
            addBee.setContent("Add " + bee.getX() + " " + bee.getY());
            bee.send(addBee);
        }
        else
            returnValue = 0;
    }

    @Override
    public int onEnd() {
        return returnValue;
    }
    
}
