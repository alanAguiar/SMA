package BeeClustering.Behaviours;

import BeeClustering.Agents.BeeHive;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BeeHiveBehaviour extends CyclicBehaviour{
    private BeeHive bh;
    private Map<Key, int[]> points;
    private JFrame frame;
    private BeeHivePanel pane;
    
    public BeeHiveBehaviour(BeeHive bh){
        super(bh);
        this.bh = bh;
        points = new HashMap<>(); 
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
            int group = scan.nextInt();
            int state = scan.nextInt();
            int val[] = {group, state};
            points.put(new Key(x, y), val);
            message = myAgent.receive();
        }               
             
        pane.repaint();
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(BeeHiveBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class BeeHivePanel extends JPanel{
        private Map<Key, int[]> points;
        private float maxX, minX, maxY, minY;
        private int height, width;
        
        public BeeHivePanel(Map<Key, int[]> p, float maxX, float minX, float maxY, float minY, int w, int h){
            this.points = p;
            this.maxX = maxX;
            this.maxY = maxY;
            this.minX = minX;
            this.minY = minY;
            this.height = h;
            this.width = w;
        }
        
        @Override
        public void paintComponent(Graphics g){ 
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("default", Font.BOLD, 14));
            
            for(Key key : this.points.keySet()){  
                int value[] = points.get(key);
                int color = value[0] * 50;
                if(color%3 == 0)
                    g2.setColor(new Color(255, color, color));
                else if(color%2 == 1)
                    g2.setColor(new Color(color,255, color));
                else 
                    g2.setColor(new Color(color, color, 255));

                switch(value[1]){
                    case 0:
                        g2.drawString("V",((key.x-minX)/(maxX-minX)*(this.width-50))+25, ((key.y-minY)/(maxY-minY)*(this.height-50))+25);
                        break;
                    case 1:
                        g2.drawString("W",((key.x-minX)/(maxX-minX)*(this.width-50))+25, ((key.y-minY)/(maxY-minY)*(this.height-50))+25);
                        break;
                    case 2:
                        g2.drawString("D", ((key.x-minX)/(maxX-minX)*(this.width-50))+25, ((key.y-minY)/(maxY-minY)*(this.height-50))+25);
                        break;
                }
            }
        }
    }
    
    private class Key{
        private float x, y;
        public Key(float x, float y){
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }
            Key key = (Key) o;
            return x == key.x && y == key.y;
        }

        @Override
        public int hashCode() {
            return (int)(((int)this.x << 16)+this.y);
        }
    }
}
