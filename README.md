# Peg Solitaire

![Peg Solitaire](/assets/peg-solitaire.jpeg)

There are many different versions of [Peg Solitaire](https://en.wikipedia.org/wiki/Peg_solitaire). This code demonstrates how to use the A* algorithm to solve the triangular 15 peg version. In this version, you start the game by removing one peg, any peg you choose, then you continue to jump over other pegs to remove them. The goal is to remove all pegs but one. There’s situations where you will get stuck because you can only jump into an empty space and you can’t jump an existing space.

There's an advanced version of this game where you try to have the peg start and end in the same place. The code in this program is setup to solve this version, but can be easily modified to solve the simpler version. 

I originally wrote this code based on watching an episode from History channel’s show Smartest Guy in the Room. In this [blog post](https://thefalc.com/2016/02/solving-peg-solitaire-as-seen-on-smartest-guy-in-the-room/), I analyze different versions of the game and how your starting position impacts the moves required to win.

## Technical details

The project is written in Java. The board is represented by a grid of rows and columns where pegs are represented by the number 1 and empty locations are represented by the number 2. Zeroes are out of bounds areas not part of the board.

Most games of this nature can be modeled as a graph where each state of the board is a node in a graph and there’s an edge between two nodes (states) if there’s a legal move that would transpose the game board state into another. The image below demonstrates this with a partial graph of 4 different moves. Since we are always removing pieces, this graph actually forms a tree. The depth of the tree corresponds to how many peg removals have taken place.

![Graph Example](/assets/graph_example.jpeg)

**Code structure**
* [Main.java](https://github.com/thefalc/peg-solitaire/blob/main/src/com/thefalc/main/Main.java) - Contains input and A* solver.

