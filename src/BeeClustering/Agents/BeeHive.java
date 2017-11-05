package BeeClustering.Agents;

import BeeClustering.Behaviours.BeeHiveBehaviour;
import jade.core.AID;
import jade.core.Agent;
import java.util.ArrayList;

public class BeeHive extends Agent{
    public ArrayList<AID> bees;
    private float maxX, maxY, minX, minY;    
    
    @Override
    public void setup(){    
        bees = new ArrayList<AID>();
        
        addBehaviour(new BeeHiveBehaviour(this));
    }
    
    public void addBee(AID b){
        bees.add(b);
    }
    
    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }
}
