package BeeClustering.Agents;

import BeeClustering.Behaviours.BeeHiveBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeeHive extends Agent{
    public ArrayList<AID> bees;
    private float maxX, maxY, minX, minY;    
    
    public BeeHive(){        
        bees = new ArrayList<AID>();
    }
    
    @Override
    public void setup(){            
        addBehaviour(new BeeHiveBehaviour(this));
    }
    
    @Override
    public void takeDown(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(BeeHive.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Execução finalizada");
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
