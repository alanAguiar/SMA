package BeeClustering.Agents;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CreateAgent {

    static ContainerController containerController;
    static AgentController agentController;

    public static void main(String args[]) throws InterruptedException {
        startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "UFABC");
        try {
            Bee bee1 = new Bee();
            bee1.setX(1.0f);
            bee1.setY(2.0f);
            //bee1.setGroup(1);
            Bee bee2 = new Bee();
            bee2.setX(2.0f);
            bee2.setY(3.0f);
            //bee2.setGroup(2);
            Bee bee3 = new Bee();
            bee3.setX(2.0f);
            bee3.setY(4.0f);
            //bee3.setGroup(3);
            BeeHive bh = new BeeHive();
            bh.setMaxX(2.0f);
            bh.setMinX(1.0f);
            bh.setMaxY(4.0f);
            bh.setMinY(2.0f);
            addExistingAgent(containerController, "BeeHive", bh);
            addExistingAgent(containerController, "Bee1", bee1);
            addExistingAgent(containerController, "Bee2", bee2);
            addExistingAgent(containerController, "Bee3", bee3);
            bh.addBee(bee3.getAID());
            bh.addBee(bee2.getAID());
            bh.addBee(bee1.getAID());
        }catch(Exception e){
            e.printStackTrace();
        }
          
        
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
