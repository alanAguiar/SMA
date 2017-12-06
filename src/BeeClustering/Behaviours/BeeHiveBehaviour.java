package BeeClustering.Behaviours;

import BeeClustering.Agents.BeeHive;
import BeeClustering.aux.Key;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BeeHiveBehaviour extends CyclicBehaviour{
    private BeeHive bh;
    private Map<Key, String[]> points;
    private JFrame frame;
    private BeeHivePanel pane;
    
    int timeToLive = 100;
    
    public BeeHiveBehaviour(BeeHive bh){
        super(bh);
        this.bh = bh;
        points = new ConcurrentHashMap<>(); 
        pane = new BeeHivePanel(points, bh.getMaxX(), bh.getMinX(), bh.getMaxY(), bh.getMinY(), 640, 480);
                
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {               
                frame = new JFrame("Bee Clustering");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(640, 480);
                frame.setVisible(true);
                frame.setContentPane(pane);
            }
        });    
    }
    
    @Override
    public void action() {
        if(timeToLive-- > 0)
        {
            System.out.println("Ciclos de execução restantes:"+timeToLive);
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setSender(myAgent.getAID());
            for (AID bee : bh.bees) {
                message.addReceiver(bee);
            }
            myAgent.send(message);

            message = myAgent.receive();
            while(message!=null){
                String content = message.getContent();
                Scanner scan = new Scanner(content);
                scan.useLocale(Locale.US);
                float x = scan.nextFloat();
                float y = scan.nextFloat();
                int state = scan.nextInt();
                String group = scan.nextLine();
                String val[] = {group, Integer.toString(state)};
                points.put(new Key(x, y), val);
                message = myAgent.receive();
            }           


            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(BeeHiveBehaviour.class.getName()).log(Level.SEVERE, null, ex);
            }
            pane.repaint();
        }
        else{
            ACLMessage message = new ACLMessage(ACLMessage.CANCEL);
            message.setSender(myAgent.getAID());
            for (AID bee : bh.bees) {
                message.addReceiver(bee);
            }
            myAgent.send(message);
            myAgent.doDelete();
        }
    }
    
    private class BeeHivePanel extends JPanel{
        private Map<Key, String[]> points;
        private float maxX, minX, maxY, minY;
        private int height, width;
        
        public BeeHivePanel(Map<Key, String[]> p, float maxX, float minX, float maxY, float minY, int w, int h){
            this.points = p;
            this.maxX = maxX;
            this.maxY = maxY;
            this.minX = minX;
            this.minY = minY;
            this.height = h;
            this.width = w;
        }
        synchronized 
        @Override
        public void paintComponent(Graphics g){ 
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("default", Font.BOLD, 14));
            for(Key key : this.points.keySet()){  
                String value[] = points.get(key);
                int color = (Math.abs(value[0].hashCode()))%255;
                if(color%3 == 0)
                    g2.setColor(new Color(255, color/2, color));
                else if(color%2 == 1)
                    g2.setColor(new Color(color,255, color/2));
                else 
                    g2.setColor(new Color(color/2, color, 255));

                switch(value[1].charAt(0)){
                    case '0':
                        g2.drawString("V",((key.x-minX)/(maxX-minX+0.1f)*(this.width-50))+25, ((key.y-minY)/(maxY-minY+0.1f)*(this.height-50))+25);
                        break;
                    case '1':
                        g2.drawString("W",((key.x-minX)/(maxX-minX+0.1f)*(this.width-50))+25, ((key.y-minY)/(maxY-minY+0.1f)*(this.height-50))+25);
                        break;
                    case '2':
                        g2.drawString("D", ((key.x-minX)/(maxX-minX+0.1f)*(this.width-50))+25, ((key.y-minY)/(maxY-minY+0.1f)*(this.height-50))+25);
   
                        break;
                }
            }
        }
    }

}
