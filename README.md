# Heuristic-based Chess Program

The source code for this project folder allows you to run a chess game with an actual chess board and chess pieces. There are a variety of buttons which do various things. An image of the GUI can be seen below.

![Alt text](https://prnt.sc/UjRFGwxXnfDg) "a title")

### 'New' Button

Clicking the “New” button will generate a new random legal starting position. 

### 'Start' Button

Clicking the “Start” button will commence the game with a move from the White pieces, and it will also generate a window prompting the user to pick between one or two step Minimax/Maximin before the game starts.

### 'Reset' Button

Clicking the “Reset” button will reset the chess pieces back to their initial position before the “Start” button has been clicked. 

### 'Next' Button

Clicking the “Next” button will initiate either Black’s or White’s next move, depending on which side’s turn it is. 

### 'About' Button

Clicking the “About” button will open a window where users can find the names of the authors and testers, the affiliation of the authors, as well as what year the program was last updated.

### 'Tree' Button

Clicking the “Tree” button will open a new window with a tree diagram, where the depth is equal to double the amount of steps, plus the root node. A tree with 1 step of Minimax will have 3 levels of nodes, while a tree with 2 steps of Minimax will have 5 levels of nodes. The root node represents the initial position of the board before a move is made. If it is white to move, a step consists of all potential moves which can be made by White, followed by all the potential moves which can be made by Black starting from those potential White moves - this is what is known as the Minimax tree. A Maximin tree is analogous to the Minimax tree, except that it is generated upon, i.e., after, Black’s move. 

Each node on the tree diagram is represented by a string of numbers and characters (e.g., 120k12R35K65), which, in their respective order, represent: the value of the position, followed by the coordinates of the Black King, White Rook, and White King. The user can use the arrow keys to traverse through the tree diagram. Pressing on the down arrow key for the first time when the program is executed will have the view of the window centered around the root node. From here, if the user presses the down arrow key again, it will show the subtree of the root node, starting with the leftmost node in that subtree. If the user presses the up arrow key, the view of the window will shift again to the root node of that subtree. Pressing the right and left arrow keys allows the user to traverse through the subtree that they are in. If the color of a node is bolded, then that node represents the node that the user is traversing from.
 
There are also distinct colors which distinguish the different kinds of possible nodes on the tree diagram. If the node is colored blue, then that node represents a checkmate. If the node is grey, then that node represents a stalemate. If the node is colored magenta, then that node represents a threefold repetition draw. If the node is colored pink, then that node represents a rook capture draw. If the node is colored orange, then that node represents a 50 move rule draw. If the node is colored black, then it is simply a standard move. If the node is colored green, and it is located on the first level, then that move is the one that was chosen to be made. All nodes vertically connected to the chosen move are highlighted in green, including the lines themselves, so that the user can see how and why the move was chosen.


# JavaApplication13

The purpose of "JavaApplication13" is to generate all legal starting chess positions and store them in a file, so that they can later be retrieved and tested.


# JavaApplication12

The "JavaApplication12" program is similar to the main chess program, with some substantial differences. Firstly, all GUI aspects of the program have been removed in this program, in order to increase performance. Secondly, the program creates both a directory named “LegalPositionTests” and 175,168 files which are placed inside the aforementioned directory. These files are all named after a legal enumeration (e.g., “194.txt”) and are created before the legal enumeration is put through a simulation game (i.e., a chess game which runs on its own, without the need for human input). Moreover, there are new functions added to this program for the sake of writing test results into the files.

Test results include:
1. Starting position written in Forsyth-Edwards notation (FEN)
2. List of all chess moves made during the game, written in standard chess notation
3. The amount of moves it took the game to reach a conclusion
4. The outcome of the game, which can include: checkmate, stalemate, 50-move-rule draw, threefold repetation draw, or rook capture draw

The results file will include both the results of the 1-step and 2-step simulation of the chess game. 


# JavaApplication11

The purpose of "JavaApplication11" is to read every results file and output all relevant statistical data on the terminal. However, the results which were already obtained will be stated below.

From the obtained results, in 1-step, 48.27% of games resulted in checkmates and the other 51.73% resulted in 50-move-rule draws. The average number of moves per game was 37 moves. In 2-step, 84.25% of games resulted in checkmates and the other 15.75% resulted in 50-move-rule draws. The average number of moves per game was 23 moves. The number of checkmates increased by 74.54%, the number of 50-move-rule draws decreased by 228.44%, and the average amount of moves per game decreased 35.14% from 1-step to 2-step. Overall, the results show notable improvement from 1-step to 2-step, as seen from White’s perspective. 
