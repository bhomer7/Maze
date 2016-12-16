
import java.util.*;
import java.io.*;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.InputEvent;

/**
 * A 2 dimensional maze game. Solve as many mazes as you want of arbitrary size.
 * Move with hjkl or arrow keys. Once at the goal, press enter to create a new
 * maze. Quit with q. Goal is to move from top left corner to bottom right
 * corner.
 *
 * @author Benjamin Homer
 */
public class MazeRenderer extends Application {

    private final int pixSize = 7;
    private final Color wallColor = Color.DARKGREY;
    private final Color pathColor = Color.LIGHTGRAY;
    private final Color cursorColor = Color.RED;
    private final Color borderColor = Color.BLACK;
    private final Color goalColor = Color.GREEN;
    private static char[][] maze;
    private int cursorX;
    private int cursorY;

    /**
     * Launches the program. Initializes the maze. Accepts 2 integers as its
     * arguments. The first is the height of the maze, the second is the width
     * of the maze. The created window is sized to fit the maze exactly.
     *
     * @param args The commandline arguments.
     */
    public static void main(String[] args) {
        validateArgs(args);
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        Maze m = new Maze(width, height, 1);
        maze = m.getFullArray();

        Application.launch(args);
    }

    /**
     * Checks that the command line arguments are 2 integers exactly. Exits the
     * program with an error message if invalid arguments are passed.
     *
     * @param args The commandline arguments.
     */
    private static void validateArgs(String[] args) {
        if(args.length != 2) {
            System.err.println("Incorrect number of arguments");
            System.exit(2);
        }
        try {
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e) {
            System.out.println("Arguments need to be  2 integers");
            System.exit(1);
        }
    }

    /**
     * Starts the javafx application.
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Maze");
        Group root = new Group();

        Canvas canvas = new Canvas((this.maze.length+2)*pixSize, (this.maze[0].length+2)*pixSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawMaze(gc);
        addMoveHandler(canvas);
        root.getChildren().add(canvas);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Draws the maze on the primary canvas. The maze is rendered in two colors,
     * surrounded by a dark border. The cursor the player controls is placed in
     * the top left, and the goal is colored differently in the bottom right.
     */
    private void drawMaze(GraphicsContext gc) {
        gc.setFill(borderColor);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        for(int i=1; i<=this.maze[0].length; i++) {
            for(int j=1; j<=this.maze.length; j++) {
                gc.setFill((this.maze[j-1][i-1]==Maze.BLOCK) ? wallColor : pathColor);
                gc.fillRect(j*pixSize, i*pixSize, pixSize, pixSize);
            }
        }
        gc.setFill(goalColor);
        gc.fillRect(maze.length*pixSize, maze[0].length*pixSize, pixSize, pixSize);

        cursorX = cursorY = 0;
        drawCursor(gc);
    }

    /**
     * Draws the cursor at the current position as indicated by the class
     * variables.
     */
    private void drawCursor(GraphicsContext gc) {
        gc.setFill(cursorColor);
        gc.fillRect((cursorX+1)*pixSize, (cursorY+1)*pixSize, pixSize, pixSize);
    }

    /**
     * Clears the cursor at the current position as indicated by the class
     * variables.
     */
    private void clearCursor(GraphicsContext gc) {
        gc.setFill(pathColor);
        gc.fillRect((cursorX+1)*pixSize, (cursorY+1)*pixSize, pixSize, pixSize);
    }

    /**
     * Adds the control handling to the passed canvas.
     * <ul>
     * <li>h and left move the cursor left
     * <li>j and down move the cursor down
     * <li>k and up move the cursor up
     * <li>l and right move the cursor right
     * <li>enter creates a new maze and resets the cursor if the cursor is on the
     * goal
     * <li>q quits the program
     * <li>all other input is ignored
     * </ul>
     */
    private void addMoveHandler(Canvas canvas) {
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                //System.out.println("Key pressed: " + ke);
                switch(ke.getCode()) {
                    case LEFT:
                    case H:
                        //System.out.println("move left");
                        if(cursorX > 0 && maze[cursorX-1][cursorY] == Maze.PATH) {
                            clearCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                            cursorX--;
                            drawCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                        }
                        break;
                    case DOWN:
                    case J:
                        //System.out.println("move down");
                        if(cursorY < maze[0].length-1 && maze[cursorX][cursorY+1] == Maze.PATH) {
                            clearCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                            cursorY++;
                            drawCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                        }
                        break;
                    case UP:
                    case K:
                        //System.out.println("move up");
                        if(cursorY > 0 && maze[cursorX][cursorY-1] == Maze.PATH) {
                            clearCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                            cursorY--;
                            drawCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                        }
                        break;
                    case RIGHT:
                    case L:
                        //System.out.println("move right");
                        if(cursorX < maze.length-1 && maze[cursorX+1][cursorY] == Maze.PATH) {
                            clearCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                            cursorX++;
                            drawCursor(((Canvas)ke.getSource()).getGraphicsContext2D());
                        }
                        break;
                    case ENTER:
                        if(cursorX == maze.length-1 && cursorY == maze[0].length-1) {
                            //System.out.println("new maze");
                            maze = (new Maze(maze[0].length/2+1, maze.length/2+1, 1)).getFullArray();
                            drawMaze(((Canvas)ke.getSource()).getGraphicsContext2D());
                        }
                        break;
                    case Q:
                        System.exit(0);
                    default:
                }
            }
        });
    }

}
