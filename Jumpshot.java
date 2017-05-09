import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.text.*;

class Joint {
    int ID;
    double x,y;
    double nextX, nextY;
    double xV;
    double yV;

    public String toString(){
        return "ID: " + ID + "; x= " + x + "; y= " + y + ";";
    }
}


public class Jumpshot extends JPanel implements MouseInputListener {


    // Store Joint instances.
    Vector<Joint> Joints;
    Vector<Rectangle2D.Double> obstacles;

    Joint target;

    //Jump Shot simulation global vars
    double jumpPower = -1;
    public double time1, time2, time3;


    double clock = 0;            // Track the current time.
    int sleepTime = 100;         // For animation.
    double timeStep = sleepTime;    // Advance the clock by this much.
    int maxtime = 60;

    // Radius of circle to draw.
    int radius = 20;
    int numJoints = 4;
    double linkSize = 100;
    int currentJoint = -1;


    boolean thrown = false;

    String msg = "";
    int numOfBasket = 0;
    //define a new ball located at joint 2
    ball testBall = new ball(341.6714473628482,163.63054470011215,25,50);
    physics testPhysics = new physics();

    public Jumpshot ()
    {
        this.addMouseListener (this);
        this.addMouseMotionListener (this);

        this.setSize (800, 500);
        this.setBackground (Color.gray);

        // Make the Joints and place them in their initial positions.
        // Note: the  positions satisfy the link size.
        Joints = new Vector<Joint> ();
        //double heightOffset = linkSize * Math.sin(2*Math.PI*15.0/360.0);
        //double xOffset = linkSize * Math.cos(2*Math.PI*15.0/360.0);
        /**for (int i=0; i<numJoints; i++) {
            Joint Joint = new Joint ();
            Joint.ID = i;
            if (i % 2 == 0) {
                Joint.x = 0;
            }
            else {
                Joint.x = xOffset;
            }
            Joint.y = i * heightOffset;
            Joint.x = 800;
            Joint.y = (i*100) + 100;

            Joints.add (Joint);
        }**/


        //Joint 1
        Joint Joint1 = new Joint ();
        Joint1.ID = 0;
        Joint1.x = 423.9985946492372;
        Joint1.y = 62.00498608067244;
        Joints.add (Joint1);

        //Joint 2
        Joint Joint2 = new Joint ();
        Joint2.ID = 1;
        Joint2.x = 423.0;
        Joint2.y = 162.0;
        Joints.add (Joint2);

        //Joint 3
        Joint Joint3 = new Joint ();
        Joint3.ID = 2;
        Joint3.x = 380.5045263951948;
        Joint3.y = 71.47853998579505;
        Joints.add (Joint3);

        //Joint 4
        Joint Joint4 = new Joint ();
        Joint4.ID = 3;
        Joint4.x = 341.6714473628482;
        Joint4.y = 163.63054470011215;
        Joints.add (Joint4);


        //TODO use obstacle detection at basket detection
        // The obstacles.
        obstacles = new Vector<Rectangle2D.Double>();
        //obstacles.add (new Rectangle2D.Double(260,300,140,50));
        //obstacles.add (new Rectangle2D.Double(350,240,125,120));

        // Target.
        target = new Joint ();
        target.x = 450;
        target.y = 275;
    }

    //TODO paint time, paint points
    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);
        Dimension D = this.getSize ();

        //get that nice wood court color
        Color court = new Color(255, 219, 158);

        g.setColor (court);
        g.fillRect (0,0, D.width, D.height);

        // Draw Joints.
        int prevX=-1, prevY=-1;
        for (Joint Joint : Joints) {
            int x = (int) Joint.x;
            int y = D.height - (int) Joint.y;
            if (Joint.ID == currentJoint) {
                g.setColor (Color.cyan);
            }
            else {
                g.setColor (Color.black);
            }

            if(Joint.ID == 0){
                //bottom fill
                g.fillOval (x-radius, y-radius, 3*radius, 2*radius);

                //legs
                g.fillRect (x-radius, y-radius, 15, 80);
                g.fillRect (x-radius+40, y-radius, 15, 80);

                //torso
                g.fillRect (x-radius, y-radius, radius, radius);

            }else if(Joint.ID == 1){
                //regular fill
                g.fillOval (x-radius, y-radius, 2*radius, 2*radius);
            }else if(Joint.ID == 2){


                g.fillOval (x-radius, y-radius, 2*radius, 2*radius);
            }else{
                //paint the end

                g.fillOval (x-radius, y-radius, radius, radius);

                //color Ball
                g.setColor(Color.orange);
                if (thrown) {
                  g.fillOval  ((int)Math.ceil(testBall.getCurrentX())-radius,D.height-(int)Math.ceil( testBall.getCurrentY())-radius-20,2*radius, 2*radius);
                }else{
                  g.fillOval  (x-radius,y-radius-20,2*radius, 2*radius);
                }


            }

            g.setColor (Color.black);

            Graphics2D g2 = (Graphics2D) g;
            if (prevX >= 0) {
                g.drawLine (prevX, prevY, x, y);

            }
            prevX = x;
            prevY = y;
        }

        // Draw net and back board. TODO: better version
        g.setColor (Color.white);
        Graphics2D g2 = (Graphics2D) g; //
        Rectangle2D.Double net = new Rectangle2D.Double (0,300,120,20);
        Rectangle2D.Double backboard = new Rectangle2D.Double (0, 220, 20, 80);

        g2.fill (net);
        g2.fill (backboard);

        // Target.
        //g.setColor (Color.green);
        //int x = (int) target.x;
        //int y = D.height - (int) target.y;
        //g.fillOval (x-radius, y-radius, 2*radius, 2*radius);

        // Message.
        g.setColor (Color.black);
        msg = "# of baskets " + numOfBasket + "\n     " + "Time : " + clock/1000;
        g.drawString (msg, 20, 20);
    }


    boolean stopped = true;

    void go(){
        try {

            //commented out unused fields.
            // Read angle and mass from textfields.
            //alpha = Double.parseDouble (angleField.getText());
            //m = Double.parseDouble (massField.getText());
            stopped = false;
            // Fire off the simulation thread.
            start ();
        }
        catch (Exception e) {
        }
    }

    void start ()
    {
        // Make a thread and run a simulation (whenever "Go" is clicked).
           Thread t = new Thread () {
            public void run ()
              {
                  simulate ();
              }
           };
           t.start ();
    }

    void simulate ()
    {
           runSimulation (Integer.MAX_VALUE);
    }

    void runSimulation (int stopTime){
        System.out.println("Starting sim...");


        while(!stopped){
        // First pause the thread.
            try {
                Thread.sleep (sleepTime);
            }
                catch (InterruptedException e) {
            }

            clock += timeStep;


            if(clock > stopTime){
                stopped = true;
                break;
            }
            double t = clock / 1000;
            //use t for time


            //TODO do stuff.
            for (Joint j : Joints ) {
                if(j.ID == 0){
                    //initial jump
                    if(t < jumpPower/2){
                        //System.out.println("moving 0 up");
                        move(j, j.x, j.y + 1);
                    }
                    else if(j.y > 60){
                        //System.out.println("moving 0 down");
                        move(j, j.x, j.y - 1);
                    }

                }
                else if(j.ID == 1){
                    //The head, dont move too much
                }
                else if(j.ID == 2){
                    if (t >= time1 && t < time1 + 4) {
                        //System.out.println("moving 1 up");
                        move(j, j.x-0.5, j.y + 5);
                    }else if(j.y >= 80 && t > time1){
                        //System.out.println("moving 1 down");
                        move(j, j.x, j.y - 1);
                    }
                }
                else if(j.ID == 3){
                        if (t >= time1 && t < time2 + 4) {
                            //System.out.println("moving 1 up");
                            move(j, j.x-1, j.y + 2);
                        }else if(j.y >= 165 && t > time2){
                            //System.out.println("moving 1 down");
                            move(j, j.x, j.y - 1);
                        }
                    }
                }//end joint for


            //update ball
            if (t<=time3) {
              Joint j = Joints.elementAt(3);
              testBall.setCurrentX(j.x);
              testBall.setCurrentY(j.y);
            }

            if (t>time3) {
              // after time3 the ball will shot at 45 degree
              thrown = true;
              testBall = testPhysics.update(testBall);
            }
            //goal detection

            if (testBall.getCurrentX()<=100 && testBall.getCurrentX()>=15 && testBall.getCurrentY()>= 300 && testBall.getCurrentY()<= 360) {
              numOfBasket = 1;
              System.out.println("the current X of ball is : "+testBall.getCurrentX()+"the current Y of ball is: "+testBall.getCurrentY());
            }



            repaint();

        }//end while
    }


    void move (Joint Joint, double nextX, double nextY)
    {
        // Move this Joint and all later ones up by same amount.
        double delX = nextX - Joint.x;
        double delY = nextY - Joint.y;
        for (int j=Joint.ID; j<numJoints; j++) {
            // Translate by same amount.
            Joint n = Joints.get(j);
            n.nextX = n.x + delX;
            n.nextY = n.y + delY;
        }

        for (int j=Joint.ID-1; j>=0; j--) {
            // Find closest point to circle centered at previous Joint's center.
            Joint n = Joints.get(j);
            Joint prev = Joints.get(j+1);
            double d = distance (prev.nextX, prev.nextY, n.x, n.y);
            double xDiff = prev.nextX - n.x;
            double yDiff = prev.nextY - n.y;
            double moveX = xDiff*(d-linkSize)/d;
            double moveY = yDiff*(d-linkSize)/d;
            n.nextX = n.x + moveX;
            n.nextY = n.y + moveY;
            //System.out.println ("j=" + j + " d=" + d + " xD=" + xDiff + " yD=" + yDiff + " mX=" + moveX + " mY=" + moveY);
        }

        // Check validity here: intersection w/ obstacles.  use similar code to detect baskets
        Dimension D = this.getSize();
        for (int i=0; i<Joints.size()-1; i++) {
            Joint n = Joints.get(i);
            Joint m = Joints.get(i+1);
            Line2D.Double L = new Line2D.Double (n.x,D.height-n.y, m.x,D.height-m.y);
            for (Rectangle2D.Double R: obstacles) {
                // See if R intersects line.
                Rectangle2D.Double Rjava = new Rectangle2D.Double (R.x,D.height-R.y,R.width,R.height);

                // if (Rjava.intersectsLine (L)) {
                //     numOfBasket ++;
                // }
            }
        }


        // Now update.
        for (Joint n: Joints) {
            n.x = n.nextX;
            n.y = n.nextY;
        }


    }

    //------------------------------------------------------------------
    // Mouse-listening - to flip state.

    public void mouseDragged (MouseEvent e)
    {
	       Dimension D = this.getSize ();
        if (currentJoint < 0) {
            return;
        }

        Joint Joint = (Joint) Joints.get(currentJoint);
        int x = (int) Joint.x;
        int y = D.height - (int) Joint.y;
        int d = (int) distance (x, y, e.getX(), e.getY());

        if (d > radius) {
            // Mouse drag occurred too far.
            return;
        }

        move (Joint, e.getX(), D.height-e.getY());
        this.repaint ();
    }

    public void mouseClicked (MouseEvent e)
    {
    	// Find out if any Joint got clicked.
    	Dimension D = this.getSize ();
            currentJoint = -1;
    	for (int k=0; k<Joints.size(); k++) {
    	    Joint Joint = (Joint) Joints.get(k);
                int x = (int) Joint.x;
                int y = D.height - (int) Joint.y;
    	    int d = (int) distance (x, y, e.getX(), e.getY());
    	    if (d < radius) {
    		// Click occured => change state.
                    currentJoint = k;
    		break;
    	    }
    	}
            this.repaint ();
    }

    public void mouseMoved (MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited (MouseEvent e) {}
    public void mousePressed (MouseEvent e) {}
    public void mouseReleased (MouseEvent e) {}

    double distance (int x1, int y1, int x2, int y2)
    {
	 return Math.sqrt ( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
    }

    double distance (double x1, double y1, double x2, double y2)
    {
	       return Math.sqrt ( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );
    }


    JPanel makeBottomPanel(){
        JPanel panel = new JPanel ();


        //get jump power;
        panel.add (new JLabel ("Jump Power: "));
        JTextField jp = new JTextField("0", 3);
        panel.add(jp);

        //=======Timings =========//
        //get time 1,
        panel.add (new JLabel ("Upper Arm: "));
        JTextField t1 = new JTextField("0", 3);
        panel.add(t1);

        //get time 2
        panel.add (new JLabel ("Forearm: "));
        JTextField t2 = new JTextField("0", 3);
        panel.add(t2);

        //get release ball time, t3
        panel.add (new JLabel ("Release: "));
        JTextField t3 = new JTextField("0", 3);
        panel.add(t3);

        //==========+End of timers+==========//

        panel.add (new JLabel ("    "));
        JButton planB = new JButton ("Simulate");
        panel.add (planB);
        planB.addActionListener (
            new ActionListener ()
            {
                public void actionPerformed (ActionEvent a)
                {
                    jumpPower = Double.parseDouble(jp.getText());
                    time1 = Double.parseDouble(t1.getText());
                    time2 = Double.parseDouble(t2.getText());
                    time3 = Double.parseDouble(t3.getText());

                    System.out.println("Simulating");
                    System.out.println("jump power = " + jumpPower);
                    System.out.println("t1 = " + time1);
                    System.out.println("t2 = " + time2);
                    System.out.println("t3 = " + time3);

                    for (Joint joint : Joints) {
                        System.out.println(joint.toString());
                    }

                    go ();

                }
            }
        );

    	//nextB.setEnabled (false);

        panel.add (new JLabel ("    "));
    	JButton quitB = new JButton ("Quit");
    	quitB.addActionListener (
    	   new ActionListener () {
    		   public void actionPerformed (ActionEvent a)
    		   {
    		       System.exit(0);
    		   }
               }
            );
    	panel.add (quitB);

        return panel;
    }

    public static void main (String[] argv)
    {
        Jumpshot a = new Jumpshot ();
        JFrame frame = new JFrame ();
        frame.setSize (1200, 1200);

        //pane for the game
        Container cPane = frame.getContentPane();
        cPane.add (a);

        //pane for the buttons
        cPane.add (a.makeBottomPanel(), BorderLayout.SOUTH);

        //make all panels visible
        frame.setVisible (true);


    }

}
