import java.awt.*;

//box class
//suppose the box has a width of 60 and a height of 40

public class Box {
   int minX;
   int maxX;
   int minY;
   int maxY;  // Box's bounds 
   
   //Constructors
    public Box(int x, int y, int width, int height) {
      minX = x;
      minY = y;
      maxX = x + width - 1;
      maxY = y + height - 1;
   }
   
   
   //set the boundary of the box
   public void set(int x, int y, int width, int height) {
      minX = x;
      minY = y;
      maxX = x + width - 1;
      maxY = y + height - 1;
   }
}