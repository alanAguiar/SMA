package BeeClustering.Agents;

import BeeClustering.Behaviours.GroupBehaviour;
import jade.core.AID;
import jade.core.Agent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Group extends Agent{
    private Map<AID, float[]> groupComponents;
    private double utility;
    private double centroidX=0, centroidY=0;

    public Group(){
        this.groupComponents = new HashMap<AID, float[]>();
    }
    
    @Override
    public void setup(){
        this.utility = 1;
        addBehaviour(new GroupBehaviour(this));
    }
    
    
    public void addBee(AID bee, float x, float y)
    {
        float[] value = {x, y};
        int size = groupComponents.size();
        centroidX = ((centroidX*size) + x)/(size + 1);        
        centroidY = ((centroidY*size) + y)/(size + 1);
        groupComponents.put(bee, value);
    }
    
    public void removeBee(AID bee)
    {
        int size = groupComponents.size();
        float[] values = groupComponents.get(bee);  
        if(size == 1){
            centroidX = 0;
            centroidY = 0;
        }
        else{
            centroidX = ((centroidX*size) - values[0])/(size - 1);        
            centroidY = ((centroidY*size) - values[1])/(size - 1);
        }
        groupComponents.remove(bee);
    }
    
    public void calculateUtility(){
        if(groupComponents.isEmpty())
            utility = 2;
        else{
            double var = 0;
            for(Map.Entry<AID, float[]> entry : groupComponents.entrySet())
            {
                var += Math.pow(centroidX-entry.getValue()[0], 2) + 
                       Math.pow(centroidY-entry.getValue()[1], 2);
            }
            var /= groupComponents.size();
            utility = (Bee.maxDistance - Math.sqrt(var))/Bee.maxDistance;
        }
    }
    
    public double getUtility(){
        return this.utility;
    }
    
    public int getSize(){
        return groupComponents.size();
    }
}



