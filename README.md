# The Maze Of Waze
This is a homework project Exercise #3 in the Object-oriented programming course.
In this assignment we used the infrastructure of the previous project (Ex2). The main goal of this assignment is to develop a logic structure and algorithms for a game where a group of ‘robots’ needs to make effective movement and thus gain as many points as possible by collecting fruit. By a given graph, we would like to place the robots strategically and run the program visually. This game should run manually or automatically depending on the user’s choice.
In this project we use a given server which helps us to understand a bit of the logic behind this project and through the server we get all the information that we need for creating this game.
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

![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/manuel%20or%20auto.png)

secondly, you must chose a level to play (0 – 23 levles) we chose level 21 on automatic mode for this example
![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/choose%20level.png)




After choosing a level the game will start running and the ‘robots’ will move towards the ‘fruit’
by a defined algorithm, on the top right side the seconds left for the game to finish will 
be shown starting from 60 seconds..

![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/game%20palying.png)




When the time has reached 0.0 seconds the game will stop and a message with the result will be shown with the result
![alt text](https://github.com/VadimKachevski/OOP_Ex3/blob/master/images/game%20end.png)













