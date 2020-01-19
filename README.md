# The Maze Of Waze
This is a homework project Exercise #3 in the Object-oriented programming course.

In this assignment we used the infrastructure of the previous project (Ex2).
The main goal of this assignment is to develop a logic structure and algorithms for a game where a group of ‘robots’ needs to make effective movement and thus gain as many points as possible by collecting fruit.
By a given graph, we would like to place the robots strategically and run the program visually. This game should run manually or automatically depending on the user’s choice.

In this project we use a given server which helps us to understand the logic behind this project and through the server we get all the data that we need for creating this game.
In addition to the previous packages & classes we’ve added a package named 

### gameClient which includes these classes:

•	autoGame

•	manuelGame

•	KML_Logger

•	MyGameGUI

•	SimpleGameClient


Another class added to this project was StdDraw_gameGui in the utils package.
Addition information about these classes will be find in the WIKI section.

# How to play the Maze Of Waze

first the user must choose if to play the game manually or automatically

![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/play.jpg)

secondly, you must chose a level to play (0 – 23 levles) we chose level 21 on automatic mode for this example
![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/choose%20level.png)




After choosing a level the game will start running and the ‘robots’ will move towards the ‘fruit’
by a defined algorithm, on the top right side the seconds left for the game to finish will 
be shown starting from 60 seconds..

![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/level21%20playing.jpg)




When the time has reached 0.0 seconds the game will stop and a message with the result will be shown with the result
![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/game%20end.png)


Best resaults of games

|game	|grade		|moves|
|---|---|---|
|0		|178		|292  |
|1		|641		|826  |
|2		|300		|291  |
|3		|1110		|827  |
|4		|484		|411  |
|5		|917		|823  |
|6		|85			|290  |
|7		|479		|824  |
|8		|157		|407  |
|9		|631		|814  |
|10		|235		|407  |
|11		|2777		|812  |
|12		|79			|288  |
|13		|605		|816  |
|14		|267		|405  |
|15		|441		|817  |
|16		|276		|405  |
|17		|1458		|808  |
|18		|40			|289  |
|19		|392		|816  |
|20		|310		|287  |
|21		|435		|810  |
|22		|285		|403  |
|23		|1269		|806  |

## Export as KML file

![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/GE.JPG)











