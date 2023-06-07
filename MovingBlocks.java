import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MovingBlocks extends Blocks {
  private static int speed = 1;
  private double xPos;
  
  public MovingBlocks(double x) {
      xPos = x;
  }

  public MovingBlocks() {
    
  }

  public static void move(Blocks[][] grid, int row, int numBlocks){
    if(row >= 0 && numBlocks > 0) {
      for(int i = 0; i < numBlocks; i++) {
        grid[row][2+i] = new MovingBlocks();
      }
      //start while loop here
      //while(true) { //!(KeyEvent.KeyCode(KeyCode.getKeyCode("SPACE")))) {
        if(grid[row][6] instanceof MovingBlocks || grid[row][0] instanceof MovingBlocks) {
          speed *= -1;
        }
        if(speed == -1) {
          for(int i = grid[row].length-1; i >= 0; i+= speed) {
            if(grid[row][i] instanceof MovingBlocks) {
              Blocks temp = grid[row][i];
              for(int j = i+speed; j >= 0; j+= speed) {
                if(grid[row][j] instanceof BackgroundBlocks) {
                  grid[row][i] = grid[row][j];
                  grid[row][j] = temp;
                  j = -1;
                }
              }
            }
          }
        }
        else if(speed == 1) {
          for(int i = 0; i < grid[row].length; i += speed) {
            if(grid[row][i] instanceof MovingBlocks) {
              Blocks temp = grid[row][i];
              for(int j = i+speed; j < grid[row].length; j+= speed) {
                if(grid[row][j] instanceof BackgroundBlocks) {
                  grid[row][i] = grid[row][j];
                  grid[row][j] = temp;
                  j = grid[row].length;
                }
              }
            }
          }
        }
      //}
      //end while loop here
      /*for(int i = 0; i < grid.length; i++) {
        for(int j = 0; j < grid[i].length; j++) {
          System.out.print(grid[i][j] + " ");
        }
        System.out.println();
      }*/
      for(int i = 0; i < grid[row].length; i++) {
        if(grid[row][i] instanceof MovingBlocks) {
          if(!(grid[row+1][i] instanceof NonMovingBlocks)) {
            grid[row][i] = new BackgroundBlocks();
            numBlocks--;
          }
          else {
            grid[row][i] = new NonMovingBlocks();
          }
        }
      }
      move(grid, row-1, numBlocks);
    }
  }
  
}