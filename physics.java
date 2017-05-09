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
    this.currentX = ball.getCurrentX();
    this.currentY = ball.getCurrentY();
    this.current_velX = ball.getCurrent_velX();
    this.current_velY = ball.getCurrent_velY();
    bounceOffWall();
    ball.setCurrentX(this.currentX);
    ball.setCurrentY(this.currentY);
    ball.setCurrent_velX(this.current_velX);
    ball.setCurrent_velY(this.current_velY);
    return ball;

  }

/*
  void bounceOffWall(){
    //initial position

    if(goal()==true||collision>3){
    goalPoints++;
    this.nextX = 50;
    this.nextY = 10;
    this.next_velX = 0;
    this.next_velY = -9.8;
    //init(ball);
    }

    //see if the ball collides with the wall
    bounceOffWall();

  }
  */


  void bounceOffWall(){

    //bounce off the left wall

    //assume ball has a radius of 10
    if(this.currentX<20){
      System.out.println("reach left");

      this.current_velX = (-1)*this.current_velX;
      this.currentX = 0+20-0.1*current_velX;
      this.current_velY = this.current_velY - 0.1 * 9.8;
      this.currentY = this.currentY + 0.1 * current_velY;

      //collision++;

    }
    //bounce off the right wall
    if(this.currentX>1200-20){
      this.current_velX = (-1)*this.current_velX;

      this.currentX = 1000-0.1*current_velX;

      this.current_velY = this.current_velY - 0.1 * 9.8;
      this.currentY = this.currentY + 0.1 * current_velY;

      //collision++;
    }

    //bounce off the floor
    if(this.currentY>1200-20){
    this.current_velY = (-1)*this.current_velY - 0.1 * 9.8;
    this.currentY = 1000 + 0.1*current_velY;
    this.current_velX = this.current_velX;
    this.currentX = this.currentX - 0.1 * current_velX;

    //collision++;

    }
    //bounce off the ceiling
    if(this.currentY<20){
    System.out.println("reach top");
    this.current_velY = (-1)*this.current_velY - 0.1 * 9.8;

    this.currentY = 20+ 0.1 * current_velY;

    this.current_velX = this.current_velX;
    this.currentX = this.currentX - 0.1 * current_velX;
    //collision++;
    }

    else{
    //this.next_velX = this.current_velX;
    //this.next_velY = this.current_velY - 0.1 * 9.8;
    //this.nextX = this.currentX + 0.1 * current_velX;
    //this.nextY = this.currentY + 0.1 * current_velY;
    this.currentX = this.currentX - 0.1 * current_velX;
    this.currentY = this.currentY + 0.1 * current_velY;
    this.current_velX = this.current_velX;
    this.current_velY = this.current_velY - 0.1 * 9.8;
    }

    }

/*
  ball update(ball ball){
    //return an updated ball
    this.currentX = ball.getCurrentX();
    this.currentY = ball.getCurrentY();
    this.current_velX = ball.getCurrent_velX();
    this.current_velY = ball.getCurrent_velY();
    updateStatus();
    ball.setCurrentX(this.currentX);
    ball.setCurrentY(this.currentY);
    ball.setCurrent_velX(this.current_velX);
    ball.setCurrent_velY(this.current_velY);
    return ball;

  }

  void updateStatus(){
    //this method will update the status of the ball
    this.currentX = this.currentX - 0.1 * current_velX;
    this.currentY = this.currentY + 0.1 * current_velY;
    this.current_velX = this.current_velX + 0.1 * 9.8;
    this.current_velY = this.current_velY + 0.1 * 9.8;
  }
  */
}
