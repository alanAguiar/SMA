package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

public class VisitingBehaviour extends OneShotBehaviour
{
    private Bee bee;
    
    public VisitingBehaviour(Bee a){
        super(a);
        bee = a;
    }    

    @Override
    public void action() {
        bee.receiveMessage(bee.VISITING);
        //System.out.println(bee.getVisitedBee().getLocalName() + " visitado por " + myAgent.getAID().getLocalName());
    }

    @Override
    public int onEnd() {
        return 1;
    }
    
}
