public class Blocks {
  private double width = 50;
  private double height = 50;
  private static int numBlocks = 3;

  public Blocks() {
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public String toString() {
    return getClass().getName();
  }

  public static int getBlocks() {
    return numBlocks;
  }

  public static void subtractBlock() {
    numBlocks--;
  }

  public static void resetBlocks() {
    numBlocks = 3;
  }
}