package BeeClustering.Behaviours;

import BeeClustering.Agents.Bee;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

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
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setSender(myAgent.getAID());
        message.addReceiver(bee.getVisitedBee());
        myAgent.send(message);
        
        MessageTemplate MT = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        message = myAgent.receive(MT);
        while(message==null)
            message = myAgent.receive(MT);
            //adicionar timeout
        String content = message.getContent();
        Scanner scan = new Scanner(content);
        scan.useLocale(Locale.US);
        float x = scan.nextFloat();
        float y = scan.nextFloat();
        //int group = scan.nextInt();

        System.out.println("Abelha "+bee.getX() +", " +bee.getY() + " visitendo "+ x + ", " + y);
            
        double distance = Math.sqrt(Math.pow(bee.getX() - x, 2) + Math.pow(bee.getY() -y, 2));
        double pa = 100 / (distance + 1);
        
        Random rand = new Random();
        int r = rand.nextInt(100);
        
        if(r>pa)            
            System.out.println("Abandonei");
            //Verificar utilidade de seu grupo
                    //informar a membros de seu grupo que saiu
                    //trocar de grupo
                    //informar membros do novo grupo
        else
            System.out.println("Voltei a assistir");
            //voltar a assistir
        //System.out.println(bee.getVisitedBee().getLocalName() + " visitado por " + myAgent.getAID().getLocalName());
    }

    @Override
    public int onEnd() {
        return 1;
    }
    
}
