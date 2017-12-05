package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitingBehaviour extends OneShotBehaviour
{
    private Bee bee;
    int returnValue=0;
    
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
        
        MessageTemplate MT  = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate MT2 = MessageTemplate.MatchSender(bee.getVisitedBee());
        MessageTemplate MT3 = MessageTemplate.and(MT, MT2);
        message = myAgent.receive(MT3);
        
        while(message==null){
            message = myAgent.receive(MT3);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(VisitingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          
        String content = message.getContent();
        Scanner scan = new Scanner(content);
        scan.useLocale(Locale.US);
        float x = scan.nextFloat();
        float y = scan.nextFloat();

        double distance = Math.sqrt(Math.pow(bee.getX() - x, 2) + Math.pow(bee.getY() -y, 2));
        double pa = (Bee.maxDistance-distance)/Bee.maxDistance;
        pa = Math.pow(pa, 3)*75;

        Random rand = new Random();
        int r = rand.nextInt(100);
        if(pa > r){
            double utility=0;
            try {
                utility = bee.getGroupUtility();
            } catch (InterruptedException ex) {
                Logger.getLogger(VisitingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
            ACLMessage removeBee = new ACLMessage(ACLMessage.INFORM);
            removeBee.addReceiver(bee.getGroup());
            removeBee.setContent("Remove");
            bee.send(removeBee);
            double newUtility = 0;

            if(bee.getGroupSize() > 1)
            {
                try {
                    newUtility = bee.getGroupUtility();
                } catch (InterruptedException ex) {
                    Logger.getLogger(VisitingBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if(bee.getGroupSize()==1 || newUtility > utility){
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
