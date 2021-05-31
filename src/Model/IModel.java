package Model;

import algorithms.mazeGenerators.Maze;

public interface IModel {

     int[][] generateMaze(int row, int col);
     void saveMaze(Maze maze);
     void savePlayer(int row, int col);
     Maze loadMaze(String path);
}
