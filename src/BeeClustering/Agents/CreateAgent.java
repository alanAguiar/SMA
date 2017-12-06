package BeeClustering.Agents;

import BeeClustering.aux.Key;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class CreateAgent {

    static ContainerController containerController;
    static AgentController agentController;

    public static void main(String args[]) throws InterruptedException {
        startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "UFABC");
        try {
            System.out.println("Carregando a base hepta...");
            loadDataset("hepta.txt", 1, 2,"\\s+");
//            System.out.println("Carregando a base iris...");
//            loadDataset("hepta.txt", 2, 3,",");          
        }catch(Exception e){
            e.printStackTrace();
        }
          
        
    }

    
    public static void loadDataset(String dataset, int att1, int att2, String separador) throws FileNotFoundException
    {
        File file = new File(dataset);
        Scanner scan = new Scanner(file);
        scan.useLocale(Locale.US);
        int i = 0;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;

        BeeHive bh = new BeeHive();
        Map<Key, Integer> createdBees;
        createdBees = new HashMap<Key, Integer>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] elements = line.split(separador);
            float x = Float.parseFloat(elements[att1]);
            float y = Float.parseFloat(elements[att2]);
            Key k = new Key(x, y);
            if (!createdBees.containsKey(k)) {
                createdBees.put(k, 1);
                Bee bee = new Bee();
                if (x < minX) {
                    minX = x;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (y > maxY) {
                    maxY = y;
                }
                bee.setX(x);
                bee.setY(y);
                addExistingAgent(containerController, "Bee" + i, bee);

                Group g = new Group();
                g.addBee(bee.getAID(), x, y);
                addExistingAgent(containerController, "Group" + i, g);
                bee.setGroup(g.getAID());

                bh.addBee(bee.getAID());
                i++;
            }
        }
        Bee.maxDistance = Math.sqrt(Math.pow(maxX - minX, 2) + Math.pow(maxY - minY, 2));
        bh.setMaxX(maxX);
        bh.setMinX(minX);
        bh.setMaxY(maxY);
        bh.setMinY(minY);
        addExistingAgent(containerController, "BeeHive", bh);
    }
    
    public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);

        containerController = runtime.createMainContainer(profile);
    }

    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) {
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
    }
    
    public static void addExistingAgent(ContainerController cc, String nickname, Agent agent){
        try{
            agentController = cc.acceptNewAgent(nickname, agent);
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
}
