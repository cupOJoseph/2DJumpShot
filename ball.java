public class ball  {
  //this is a template of ball class
  double currentX;              //current X coordinate
  double currentY;              //current Y coordinate
  double current_velX;          //current velocity along X-axis
  double current_velY;          //current velocity along Y-axis
  double nextX;                 //next X coordinate
  double nextY;                 //next Y coordinate
  double next_velX;             //next velocity along X-axis
  double next_velY;             //next velocity along Y-axis

	/**
	* Default empty ball constructor
	*/
  public ball(double currentX, double currentY, double current_velX, double current_velY){
    // general ball constructor
    this.currentX = currentX;
    this.currentY = currentY;
    this.current_velX = current_velX;
    this.current_velY = current_velY;
  }

	public double getCurrentX() {
		return currentX;
	}

	public void setCurrentX(double currentX) {
		this.currentX = currentX;
	}

	public double getCurrentY() {
		return currentY;
	}

	public void setCurrentY(double currentY) {
		this.currentY = currentY;
	}

	public double getCurrent_velX() {
		return current_velX;
	}

	public void setCurrent_velX(double current_velX) {
		this.current_velX = current_velX;
	}

	public double getCurrent_velY() {
		return current_velY;
	}

	public void setCurrent_velY(double current_velY) {
		this.current_velY = current_velY;
	}

	public double getNextX() {
		return nextX;
	}

	public void setNextX(double nextX) {
		this.nextX = nextX;
	}

	public double getNextY() {
		return nextY;
	}

	public void setNextY(double nextY) {
		this.nextY = nextY;
	}

	public double getNext_velX() {
		return next_velX;
	}

	public void setNext_velX(double next_velX) {
		this.next_velX = next_velX;
	}

	public double getNext_velY() {
		return next_velY;
	}

	public void setNext_velY(double next_velY) {
		this.next_velY = next_velY;
	}


}
