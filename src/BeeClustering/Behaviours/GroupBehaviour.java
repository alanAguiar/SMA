package BeeClustering.Behaviours;

import BeeClustering.Agents.Group;
import jade.core.behaviours.CyclicBehaviour;

public class GroupBehaviour extends CyclicBehaviour{
    Group group;
    public GroupBehaviour(Group group){
        super(group);
        this.group = group;
    }
    
    @Override
    public void action() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
