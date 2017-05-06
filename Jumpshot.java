
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
}


public class Jumpshot extends JPanel implements MouseInputListener {

    // Store Joint instances.
    Vector<Joint> Joints;
    Vector<Rectangle2D.Double> obstacles;

    Joint target;

    //Jump Shot simulation global vars
    double jumpPower = -1;
    double t1, t2, t3;


    // Radius of circle to draw.
    int radius = 10;
    int numJoints = 6;
    double linkSize = 200;
    int currentJoint = -1;

    String msg = "";
    int numIllegalMoves = 0;

    public Jumpshot ()
    {
        this.addMouseListener (this);
        this.addMouseMotionListener (this);

        this.setSize (800, 500);
        this.setBackground (Color.gray);

        // Make the Joints and place them in their initial positions.
        // Note: the  positions satisfy the link size.
        Joints = new Vector<Joint> ();
        double heightOffset = linkSize * Math.sin(2*Math.PI*15.0/360.0);
        double xOffset = linkSize * Math.cos(2*Math.PI*15.0/360.0);
        for (int i=0; i<numJoints; i++) {
            Joint Joint = new Joint ();
            Joint.ID = i;
            if (i % 2 == 0) {
                Joint.x = 0;
            }
            else {
                Joint.x = xOffset;
            }
            Joint.y = i * heightOffset;
            Joints.add (Joint);
        }

        // The obstacles.
        obstacles = new Vector<Rectangle2D.Double>();
        obstacles.add (new Rectangle2D.Double(260,300,140,50));
        obstacles.add (new Rectangle2D.Double(350,240,125,120));

        // Target.
        target = new Joint ();
        target.x = 450;
        target.y = 275;
    }

    public void paintComponent (Graphics g)
    {
        super.paintComponent (g);
        Dimension D = this.getSize ();
        g.setColor (Color.white);
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
                g.setColor (Color.blue);
            }
            g.fillOval (x-radius, y-radius, 2*radius, 2*radius);
            g.setColor (Color.pink);

            Graphics2D g2 = (Graphics2D) g;
            if (prevX >= 0) {
                g.drawLine (prevX, prevY, x, y);

            }
            prevX = x;
            prevY = y;
        }

        // Draw net.
        g.setColor (Color.red);
        Graphics2D g2 = (Graphics2D) g;
        for (Rectangle2D.Double R: obstacles) {
            Rectangle2D.Double Rjava = new Rectangle2D.Double (R.x,D.height-R.y,R.width,R.height);
            g2.fill (Rjava);
        }

        // Target.
        g.setColor (Color.green);
        int x = (int) target.x;
        int y = D.height - (int) target.y;
        g.fillOval (x-radius, y-radius, 2*radius, 2*radius);

        // Message.
        g.setColor (Color.black);
        msg = "# Illegal moves: " + numIllegalMoves;
        g.drawString (msg, 20, 20);
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

        // Check validity here: intersection w/ obstacles.
        Dimension D = this.getSize();
        for (int i=0; i<Joints.size()-1; i++) {
            Joint n = Joints.get(i);
            Joint m = Joints.get(i+1);
            Line2D.Double L = new Line2D.Double (n.x,D.height-n.y, m.x,D.height-m.y);
            for (Rectangle2D.Double R: obstacles) {
                // See if R intersects line.
                Rectangle2D.Double Rjava = new Rectangle2D.Double (R.x,D.height-R.y,R.width,R.height);

                if (Rjava.intersectsLine (L)) {
                    numIllegalMoves ++;
                }
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
                    System.out.println("Pressed Plan");
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
