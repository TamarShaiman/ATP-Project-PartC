package Model;

import algorithms.mazeGenerators.Maze;

import java.util.Observer;

public interface IModel {

/*     int[][] generateMaze(int row, int col);
     void SolveMaze();*/
/*
     void generateMaze(int row, int col);
*/

/*     void saveMaze(Maze maze);
     void savePlayer(int row, int col);
     Maze loadMaze(String path);*/


     void generateRandomMaze(int row, int col);
     int[][] getMaze();
     void updateCharacterLocation(int direction);
     int getRowChar();
     int getColChar();
     void assignObserver(Observer o);
     void solveMaze(int [][] maze);
     void getSolution();

}
