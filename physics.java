import java.util.*;
public class physics {
  // this class contains the physical calculation in this program
  // the default refresh rate of this program is 0.1 second, all the calculations are based on this fact
  // the gravitational force used in this program is 9.8 m/s^2
  // the coordinate of the next is calculated using the current speed, and the speed is updated after the calculation

  double currentX;              //current X coordinate
  double currentY;              //current Y coordinate
  double current_velX;          //current velocity along X-axis
  double current_velY;          //current velocity along Y-axis
  double nextX;                 //next X coordinate
  double nextY;                 //next Y coordinate
  double next_velX;             //next velocity along X-axis
  double next_velY;             //next velocity along Y-axis

  void init (ball ball){
    // before provessing, first use this method to initilize the class
    this.currentX = ball.currentX;
    this.currentY = ball.currentY;
    this.current_velY = ball.current_velY;
    this.current_velX = ball.current_velX;
    this.nextX = ball.nextX;
    this.nextY = ball.nextY;
    this.next_velY = ball.next_velY;
    this.next_velX = ball.next_velX;
  }

  ball update(ball ball){
    //return an updated ball
    updateStatus();
    ball.setNextX(this.nextX);
    ball.setNextY(this.nextY);
    ball.setNext_velX(this.next_velX);
    ball.setNext_velY(this.next_velY);
    return ball;

  }

  void updateStatus(){
    //this method will update the status of the ball
    this.nextX = this.currentX + 0.1 * current_velX;
    this.nextY = this.currentY + 0.1 * current_velY;
    this.next_velX = this.current_velX;
    this.next_velY = this.current_velY - 0.1 * 9.8;
  }
}
