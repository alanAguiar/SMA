package BeeClustering.Agents;

import BeeClustering.Behaviours.DancingBehaviour;
import BeeClustering.Behaviours.WatchingBehaviour;
import BeeClustering.Behaviours.VisitingBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bee extends Agent
{
    public static final int VISITING    = 0;
    public static final int WATCHING    = 1;
    public static final int DANCING     = 2;
    
    //private int maxSteps = 200;

    private int group;
    private float x, y;
    public ArrayList<AID> groupMembers;
    private float utility;
    
    private AID visitedBee;
    private int visitedGroup;
    
    @Override
    public void setup()
    {
        groupMembers = new ArrayList<AID>();
        this.setUtility(0);
        Object[] arg = getArguments();
        if(arg != null){
            this.setX(Float.parseFloat((String)arg[0]));
            this.setY(Float.parseFloat((String)arg[1]));
            this.setGroup(Integer.parseInt((String)arg[2]));
        }
        
        try {
            Thread.sleep(group*1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Bee.class.getName()).log(Level.SEVERE, null, ex);
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
            registerTransition(THREE_STATE, THREE_STATE, 0);
            registerTransition(THREE_STATE, ONE_STATE, 1);
        }
    }
    
    public void receiveMessage(int replyContent){
        ACLMessage message = this.receive();
        while(message != null){
            if(message.getPerformative() == ACLMessage.REQUEST){
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(this.getX() + " " + this.getY() + " " + this.getGroup() + " " + replyContent);
                this.send(reply);
            }
            message = this.receive();
        }
    }
    
    
    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
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

    public float getUtility() {
        return utility;
    }

    public void setUtility(float utility) {
        this.utility = utility;
    }

    public AID getVisitedBee() {
        return visitedBee;
    }

    public void setVisitedBee(AID visitedBee) {
        this.visitedBee = visitedBee;
    }

    public int getVisitedGroup() {
        return visitedGroup;
    }

    public void setVisitedGroup(int visitedGroup) {
        this.visitedGroup = visitedGroup;
    }
    
    
}
