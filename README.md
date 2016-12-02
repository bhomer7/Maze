
Maze
==============
A small maze game with minimalist controls and appearance. There is no scoring
at this time.

Its written in java using javafx for the graphics.

Installation
===============
Clone the git repo and run:
```
$ javac *.java
```

Running the game
================
Run a command of the following format where h and w are integers:
```
$ java MazeRenderer h w
```
The program will launch in a separate window with a maze of height h*2-1 and
width w*2-1. This is a result of internal representations of the maze.

Controls
===============
Move around with hjkl or the arrow keys.
 * h or left: move left
 * j or down: move down
 * k or up: move up
 * l or right: move right
 * enter: create a new maze and reset the cursor (only works if the cursor is at
   the goal already)
 * q: quit

