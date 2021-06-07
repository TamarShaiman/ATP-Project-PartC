package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.util.Observer;

public interface IModel {



     void generateMaze(int row, int col);
     int[][] getMaze();
     void updateCharacterLocation(int direction);
     int getRowChar();
     int getColChar();
     void assignObserver(Observer o);
     void solveMaze();
     Solution getSolution();
     int getColStart();
     int getRowStart();
     int getColGoal();
     int getRowGoal();
     void saveMaze();
     void loadMaze();
     void savePlayer();


    void exitProgram();
}
