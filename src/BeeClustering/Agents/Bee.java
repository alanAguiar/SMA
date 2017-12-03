package BeeClustering.Agents;

import BeeClustering.Behaviours.DancingBehaviour;
import BeeClustering.Behaviours.WatchingBehaviour;
import BeeClustering.Behaviours.VisitingBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bee extends Agent
{
    public static final int VISITING    = 0;
    public static final int WATCHING    = 1;
    public static final int DANCING     = 2;
    public static double maxDistance;
    //private int maxSteps = 200;

    private AID group = null;
    private float x, y;
    private int groupSize;
    
    private AID visitedBee;
    private AID visitedGroup;
    
    @Override
    public void setup()
    {
        Object[] arg = getArguments();
        if(arg != null){
            this.setX(Float.parseFloat((String)arg[0]));
            this.setY(Float.parseFloat((String)arg[1]));
        }
        while(group == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Bee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        addBehaviour(new BeeFSMBehaviour(this));
    }
   
    private class BeeFSMBehaviour extends FSMBehaviour{
        private static final String ONE_STATE = "DANCING";
        private static final String TWO_STATE = "WATCHING";
        private static final String THREE_STATE = "VISITING";
        
        private Bee bee;
        
        public BeeFSMBehaviour(Bee a){
            super(a);
            bee = a;
        }
        
        @Override
        public void onStart(){
            registerFirstState(new DancingBehaviour(bee), ONE_STATE);
            registerState(new WatchingBehaviour(bee), TWO_STATE);
            registerState(new VisitingBehaviour(bee), THREE_STATE);
            
            registerTransition(ONE_STATE, ONE_STATE, 0);
            registerTransition(ONE_STATE, TWO_STATE, 1);
            registerTransition(TWO_STATE, TWO_STATE, 0);
            registerTransition(TWO_STATE, THREE_STATE, 1);
            registerTransition(THREE_STATE, TWO_STATE, 0);
            registerTransition(THREE_STATE, ONE_STATE, 1);     
        }
    }
    
    public void receiveMessage(int replyContent){
        ACLMessage message = this.receive();
        while(message != null){
            if(message.getPerformative() == ACLMessage.REQUEST){
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(this.getX() + " " + this.getY() + " " + replyContent + " " + this.getGroup().toString());
                                
                this.send(reply);
            }
            message = this.receive();
        }
    }     
    
    public double getGroupUtility() throws InterruptedException{
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(this.getGroup());
        this.send(message);
        
        MessageTemplate MT  = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        MessageTemplate MT2 = MessageTemplate.MatchSender(this.getGroup());
        MessageTemplate MT3 = MessageTemplate.and(MT, MT2);
        
        message = this.receive(MT3);
        while(message==null){
            message = this.receive(MT3);
            Thread.sleep(100);
        }
        
        Scanner scan = new Scanner(message.getContent());
        scan.useLocale(Locale.US);
        double utility = scan.nextDouble();
        this.setGroupSize(scan.nextInt());
        return utility;
    }
    
    public AID getGroup() {
        return group;
    }

    public void setGroup(AID group) {
        this.group = group;
    }

    public void setGroupSize(int s){
        this.groupSize = s;
    }
    
    public int getGroupSize(){
        return this.groupSize;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    } 

    public AID getVisitedBee() {
        return visitedBee;
    }

    public void setVisitedBee(AID visitedBee) {
        this.visitedBee = visitedBee;
    }

    public AID getVisitedGroup() {
        return visitedGroup;
    }

    public void setVisitedGroup(AID visitedGroup) {
        this.visitedGroup = visitedGroup;
    }
}
