# TryHard_MLG_TTT_PostWW2 Nub-Powner

1v1 me tictactoe you nub

## Setup
1) Download the source code and move to the directory
```sh
$ git clone https://github.com/PiJoules/TryHardTicTacToe.git
$ cd TryHardTicTacToe
```
2) Compile the class and jar files
```sh
$ ant
```
3) Test out the game by running
```sh
$ ant test
```
This will show a game where both of the players make random moves.

4) You can add players by including the java file of the player in the `InsertPlayersHere` directory and running the command `ant battle` with the flags `-DPlayer1=name` or `-DPlayer2=name` where the `name`s are the names of the java files without the extensions. For example, let's say we have the files `FirstPlayer.java` and `Player2.java`. After placing both these files in the `InsertPlayersHere`, the command would be
```sh
$ ant battle -DPlayer1=FirstPlayer -DPlayer2=Player2
```
Excluding either flag would have the included player battle the default RandomPlayer.