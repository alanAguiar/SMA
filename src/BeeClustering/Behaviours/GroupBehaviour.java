package BeeClustering.Behaviours;

import BeeClustering.Agents.Group;
import jade.core.behaviours.SimpleBehaviour;

public class GroupBehaviour extends SimpleBehaviour{
    Group group;
    public GroupBehaviour(Group group){
        super(group);
        this.group = group;
    }
    
    @Override
    public void action() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean done() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
