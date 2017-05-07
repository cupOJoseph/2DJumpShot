import java.util.*;
public class Collision {
  // this class contains the methods for before and after the collision
  // we assume that the ball has a radius of 10 because when it collide with the wall
  // we want the surface of the ball to collide instead of the center of the ball.

  double currentX;              //current X coordinate
  double currentY;              //current Y coordinate
  double current_velX;          //current velocity along X-axis
  double current_velY;          //current velocity along Y-axis
  double nextX;                 //next X coordinate
  double nextY;                 //next Y coordinate
  double next_velX;             //next velocity along X-axis
  double next_velY;             //next velocity along Y-axis
  double collision = 0;
  double goalPoints = 0;
  double minX;
  double maxX;
  double minY;
  double maxY;

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

  void init (Box box){
    this.minX = box.minX;
    this.maxX = box.maxX;
    this.minY = box.minY;
    this.maxY = box.maxY;
  }

  ball update(ball ball){
    //return an updated ball
    bounceOffWall();
    ball.setNextX(this.nextX);
    ball.setNextY(this.nextY);
    ball.setNext_velX(this.next_velX);
    ball.setNext_velY(this.next_velY);
    return ball;

  }


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
    //bounce off the left wall
    //assume ball has a radius of 10
    if(this.nextX<this.minX+10){
      this.next_velX = (-1)*this.current_velX;
      this.nextX = this.minX+10;
      this.next_velY = this.current_velY - 0.1 * 9.8;
      this.nextY = this.currentY + 0.1 * current_velY;
      collision++;

    }
    //bounce off the right wall
    if(this.nextX>this.maxX-10){
      this.next_velX = (-1)*this.current_velX;
      this.nextX = this.maxX-10;
      this.next_velY = this.current_velY - 0.1 * 9.8;
      this.nextY = this.currentY + 0.1 * current_velY;
      collision++;
    }
 
    //bounce off the floor
    if(this.nextY<this.minY+10){
    this.next_velY = (-1)*this.current_velY - 0.1 * 9.8;
    this.nextY = this.minY+10;
    this.next_velX = this.current_velX;
    this.nextX = this.currentX + 0.1 * current_velX;
    collision++;

    }
    //bounce off the ceiling
    if(this.nextY<this.maxY-10){
    this.next_velY = (-1)*this.current_velY - 0.1 * 9.8;
    this.nextY = this.maxY-10;
    this.next_velX = this.current_velX;
    this.nextX = this.currentX + 0.1 * current_velX;
    collision++;
    }
    
    else{
    this.next_velX = this.current_velX;
    this.next_velY = this.current_velY - 0.1 * 9.8;
    this.nextX = this.currentX + 0.1 * current_velX;
    this.nextY = this.currentY + 0.1 * current_velY;
    }
    
    }
   public double getSpeed(){
    return (double)Math.sqrt(this.current_velX * this.current_velX + this.current_velY * this.current_velY);
  }

   public double getAngle(){
    return (double)Math.toDegrees(Math.atan2(-this.current_velY, this.current_velX));  
  }

  //suppose the goal is on the left wall with a range from (0,30) to (0,40)
  //Thus if the ball hit within this range, we say that the ball reaches its goal
  //This method checks whether the ball hit the goal
  public boolean goal(){
    if(this.currentX==0&&this.currentY>30&&this.currentY<40){
      
      return true;

    }
    return false;
  }



}