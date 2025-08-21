# 2150_Checkers

Developers:
Pour Over Java
Will Lovin - Vernaiko
Michael Funchess - l-pis
Jarrett Wilson - Jarrrw
Curren Patel - Pizzasauce3274

This project should be runnable with JDK17 and Junit 4.

This is a terminal based game of checkers. To play the game, it is recommended that you have IntelliJ IDEA or some other
coding IDE to run the code, as no file has been provided that compiles and runs it for you. When running on IntelliJ, make
sure that you click on the play button while in the file "CheckersFE.java", otherwise compilation will produce myriad
errors. You can manually compile it as well, though that is mostly for Linux users.

Once you have it running, this is fairly standard checkers! The board is an 8x8 grid by default with pieces represented by
any letter (with kinged versions becoming captial versions of the selected letters). Non-playable spaces are represented by an * while playable
spaces are represented by a blank space (or ' ', if you want to be technical about it). The game will prompt you to select
a piece followed by a direction. If there are no valid moves for the piece or your input does not match the instructions 
provided to you, then the program will prompt you to re-select. Once a player has taken all their opponent's pieces, they will win. 

KNOWN BUGS: There is only one known bug as of the time of writing this. If a piece is kinged, the program doesn't have a way to handle
this piece not being able to move in any direction. Now, this is also HIGHLY unlikely to encounter, as both players would have to be playing
really badly for this to occur. However, it is most likely to happen if a player gets kinged, choses to leave that piece on the opponent's
border for a few turns, and their opponent VOLUNTARILY chooses to surround the kinged piece (which would open the opponent's piece up to 
being taken by the king for at least one of those turns). You would have to be absolutely atrocious at Checkers for this to
even happen. Because it involves BOTH players making MULTIPLE bad decisions in a row, it didn't make sense to implement this feature, given
the time constraints.
