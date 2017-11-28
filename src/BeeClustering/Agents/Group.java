package BeeClustering.Agents;

import BeeClustering.Behaviours.GroupBehaviour;
import jade.core.AID;
import jade.core.Agent;
import java.util.Map;

public class Group extends Agent{
    private Map<AID, float[]> groupComponents;
    private double utility;
    private double centroidX, centroidY;

    @Override
    public void setup(){
        this.utility = 0;
        this.centroidX = 0;
        this.centroidY = 0;
        Object[] arg = getArguments();
        if(arg != null){
            this.addBee((AID)arg[0], Float.parseFloat((String)arg[1]), 
                                     Float.parseFloat((String)arg[2]));
        }
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
        centroidX = ((centroidX*size) - values[0])/(size - 1);        
        centroidY = ((centroidY*size) - values[0])/(size - 1);
        groupComponents.remove(bee);
    }
    
    public void calculateUtility(){
        double var = 0;
        for(Map.Entry<AID, float[]> entry : groupComponents.entrySet())
        {
            var += Math.pow(centroidX-entry.getValue()[0], 2) + 
                   Math.pow(centroidY-entry.getValue()[1], 2);
        }
        var /= groupComponents.size();
        utility = 1 - Math.sqrt(var);
    }
    
    public double getUtility(){
        return this.utility;
    }
    
    public int getSize(){
        return groupComponents.size();
    }
}



