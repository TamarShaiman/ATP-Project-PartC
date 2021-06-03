package Model;

import algorithms.mazeGenerators.Maze;

import java.util.Observer;

public interface IModel {



     void generateRandomMaze(int row, int col);
     int[][] getMaze();
     void updateCharacterLocation(int direction);
     int getRowChar();
     int getColChar();
     void assignObserver(Observer o);
     void solveMaze();
     int[][] getSolution();
     int getColStart();
     int getRowStart();
     int getColGoal();
     int getRowGoal();

/*     void saveMaze(Maze maze);
     void savePlayer(int row, int col);
     Maze loadMaze(String path);*/

}
