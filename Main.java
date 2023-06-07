import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.animation.Animation;
import static javafx.application.Application.launch;
import javafx.scene.shape.Polygon; 
import javafx.animation.RotateTransition; 
import javafx.util.Duration; 
import javafx.scene.transform.Rotate;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.shape.Shape;
import javafx.animation.Transition;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.VPos;
import javafx.geometry.HPos;


//website: https://edencoding.com/game-loop-javafx/

//pause transition: https://www.tutorialspoint.com/javafx/javafx_sequential_parallel.htm

//binding website: https://yakovfain.com/2014/08/11/javafx-event-handling-and-property-binding/

//key events: https://docs.oracle.com/javase/8/docs/api/java/awt/event/KeyEvent.html


public class Main extends Application { 
  
  @Override
  public void start(Stage primaryStage) {
    
    //Change Scene: https://replit.com/@SkyAdams/JavaFXchangescenedemo#Main.java
    Text text = new Text("Stacker");
    text.getStyleClass().add("words");
    text.setFont(Font.font ("Verdana", 50));
    Rectangle background = new Rectangle(357, 765);
    background.getStyleClass().add("home");
    
    Button startButton = new Button("Play Game");
    startButton.getStyleClass().add("play");
    VBox vbox = new VBox(text, startButton);
    vbox.getStyleClass().add("homeScreen");
    vbox.setSpacing(20);
    vbox.setAlignment(Pos.CENTER);
    Scene home = new Scene(vbox, 357, 765);

    startButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        playGame(primaryStage, home);
      }
    });

    home.getStylesheets().add("style.css");
    primaryStage.setTitle("Stacker Game");
    primaryStage.setScene(home);
    primaryStage.show();
  } 

  public void playGame(Stage primaryStage, Scene home) {

    GridPane gameGrid = new GridPane();
    Scene game = new Scene(gameGrid, 357, 765);
    
    Blocks[][] grid = new Blocks[15][7];
    Blocks.resetBlocks();
    
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        grid[i][j] = new BackgroundBlocks();
      }
    }

    grid[14][2] = new NonMovingBlocks();
    grid[14][3] = new NonMovingBlocks();
    grid[14][4] = new NonMovingBlocks();
    
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid[i].length; j++) {
        if(grid[i][j] instanceof NonMovingBlocks) {
          Rectangle tower = new Rectangle(grid[i][j].getWidth(), grid[i][j].getHeight());
          tower.getStyleClass().add("tower");
          gameGrid.add(tower, j, i);
        }
        else if(grid[i][j] instanceof BackgroundBlocks) {
          Rectangle back = new Rectangle(grid[i][j].getWidth(), grid[i][j].getHeight());
          back.getStyleClass().add("background");
          gameGrid.add(back, j, i);
        }
      }
    }
      
    Polygon rect = new Polygon();
    rect.getPoints().addAll(new Double[]{    
         200.0, 50.0, 
         250.0, 50.0, 
         250.0, 100.0,          
         200.0, 100.0
      });
    rect.setFill(Color.RED);  
    
    RotateTransition rotate = new RotateTransition(); 
    
    rotate.setDuration(Duration.millis(1000)); 
    rotate.setByAngle(360);
    rotate.setNode(rect);  
    rotate.setCycleCount(Animation.INDEFINITE);
    rotate.setAutoReverse(false);
    
    rotate.play(); 
    
    gameGrid.add(rect, 3, 7); 

    move(primaryStage, home, game, grid, gameGrid, 13, 3, 500);

    game.getStylesheets().add("style.css");
    gameGrid.getStyleClass().add("screen");
    primaryStage.setScene(game);
    primaryStage.show();
  }

  public void move(Stage primaryStage, Scene home, Scene game, Blocks[][] grid, GridPane gameGrid, int row, int numBlocks, int speed) {
    if(numBlocks > 0 && row > -1) {
      
      Rectangle ref = new Rectangle(50, 50);
      Timeline[] forTimes = new Timeline[7-numBlocks];
      //SequentialTransition seqTransFor = new SequentialTransition(new PauseTransition(Duration.millis(500)));
      
      for(int j = 0; j < 7-numBlocks; j++) {
        forTimes[j] = new Timeline();
        forTimes[j].setCycleCount(5);
        forTimes[j].setAutoReverse(false);
        
      }
      SequentialTransition seqTransFor = new SequentialTransition();
      
      for(int i = 0; i < numBlocks; i++) {
        Rectangle rect = new Rectangle(50, 50);
        rect.getStyleClass().add("tower");
        gameGrid.add(rect, i, row);
        int f = 51;
        for(int j = 0; j < 7-numBlocks; j++) {
          forTimes[j].getKeyFrames().add(new KeyFrame(Duration.millis(2), new KeyValue (rect.translateXProperty(), f)));
          f += 51;
        }
        
        if(i == 0) {
          ref = rect;
        }
      }
      seqTransFor.getChildren().addAll(new PauseTransition(Duration.millis(speed/2)));
      for(int j = 0; j < 7-numBlocks; j++) {
        if(j != 7-numBlocks-1) {
          seqTransFor.getChildren().addAll(forTimes[j]);
          seqTransFor.getChildren().addAll(new PauseTransition(Duration.millis(speed)));
        }
        else {
          seqTransFor.getChildren().addAll(forTimes[j]);
        }
      }
      seqTransFor.getChildren().addAll(new PauseTransition(Duration.millis(speed/2)));
      
      seqTransFor.setCycleCount(Animation.INDEFINITE);
      seqTransFor.setAutoReverse(true);
      seqTransFor.play();
  
      //Website source: https://stackoverflow.com/questions/33224161/how-do-i-run-a-function-on-a-specific-key-press-in-javafx
      Rectangle newRect = ref;
      game.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.SPACE) {
          seqTransFor.stop();
          int start = 0;
          int end = 0;
          for(int i = 0; i < numBlocks; i++) {
            if(i == 0) {
              start = (int)(newRect.getTranslateX())/51;
            }
            if(i == numBlocks-1) {
              end = (int)(newRect.getTranslateX())/51 + numBlocks - 1;
            }
          }
  
          for(int i = start; i < end+1; i++) {
            if(!(grid[row+1][i] instanceof NonMovingBlocks)) {
              grid[row][i] = new BackgroundBlocks();
              Blocks.subtractBlock();
            }
            else {
              grid[row][i] = new NonMovingBlocks();
            }
          }
          
          for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
              if(grid[i][j] instanceof NonMovingBlocks) {
                Rectangle tower = new Rectangle(grid[i][j].getWidth(), grid[i][j].getHeight());
                tower.getStyleClass().add("tower");
                gameGrid.add(tower, j, i);
              }
              else if(grid[i][j] instanceof BackgroundBlocks) {
                Rectangle back = new Rectangle(grid[i][j].getWidth(), grid[i][j].getHeight());
                back.getStyleClass().add("background");
                gameGrid.add(back, j, i);
              }
            }
          }
          int newNumBlocks = Blocks.getBlocks();
          move(primaryStage, home, game, grid, gameGrid, row-1, newNumBlocks, speed-34);
        }
      });
    }
    else if(numBlocks == 0) {
      //https://www.tutorialspoint.com/javafx/javafx_text.htm
      //textfield tut https://stackoverflow.com/questions/38138437/add-textfields-on-gridpane
      Text text = new Text();
      text.setFill(Color.RED);
      String t = "You Lose!"; 
      text.setText(t);
	    gameGrid.add(text, 1, 6, 3, 3);
      text.setFont(Font.font ("Verdana", 50));
      Button homeButton = new Button("Home");
      homeButton.getStyleClass().add("goHome");
      gameGrid.add(homeButton, 3, 9, 1, 1);
      homeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
          primaryStage.setScene(home);
          primaryStage.show();
        }
      });
    }
    else if(row == -1) {
      Text text = new Text();
      text.setFill(Color.RED);
      String t = "You Win!"; 
      text.setText(t);
	    gameGrid.add(text, 1, 6, 3, 3);
      text.setFont(Font.font ("Verdana", 50));
      text.setFont(Font.font ("Verdana", 50));
      //confetti goes here
      Button homeButton = new Button("Home");
      homeButton.getStyleClass().add("goHome");
      gameGrid.add(homeButton, 3, 9, 1, 1);
      homeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
          primaryStage.setScene(home);
          primaryStage.show();
        }
      });
    }
  }
  public static void main(String[] args) {
    launch(args);
  }
} 

/*
Commands:

cd desktop

set PATH_TO_FX="C:\Users\P1\Desktop\javafx-sdk-19.0.2.1\lib"

cd Stacker-Game-Project

javac --module-path %PATH_TO_FX% --add-modules javafx.controls Main.java

java --module-path %PATH_TO_FX% --add-modules javafx.controls Main

*/