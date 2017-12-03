package BeeClustering.Behaviours;

import BeeClustering.Agents.Group;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Locale;
import java.util.Scanner;

public class GroupBehaviour extends SimpleBehaviour{
    Group group;
    int count = 100;
    public GroupBehaviour(Group group){
        super(group);
        this.group = group;
    }
    
    @Override
    public void action() {
        ACLMessage message = group.receive();
        if(message != null){
            if(message.getPerformative() == ACLMessage.REQUEST){
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(group.getUtility() + " " + group.getSize());
                group.send(reply);
            }
            else if (message.getPerformative() == ACLMessage.INFORM) {
                Scanner scan = new Scanner(message.getContent());
                scan.useLocale(Locale.US);
                String type = scan.next();
                if (type.equals("Add")) {
                    float x = scan.nextFloat();
                    float y = scan.nextFloat();
                    AID aid = message.getSender();
                    group.addBee(aid, x, y);
                    group.calculateUtility();
                } else if (type.equals("Remove")) {
                    AID aid = message.getSender();
                    group.removeBee(aid);
                    group.calculateUtility();
               }
            }
        }
    }

    @Override
    public boolean done() {
//        return false;
        if(group.getSize() == 0)
            count++;
        else
            count = 100;
        return count<0;
    }
    
    @Override
    public int onEnd(){
        myAgent.doDelete();
        return  1;
    }
    
}
