package Model;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.*;
import  Server.*;
import  Client.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MyModel implements IModel {
  /*  Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
    Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());*/



    public int[][] generateRandomMaze(int rows, int cols){
        int[][] maze = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = (int) Math.round(Math.random());
            }
        }
        return maze;
    }

    @Override
    public int[][] generateMaze(int row, int col) {
        /*solveSearchProblemServer.start();
        mazeGeneratingServer.start();*/
        AMazeGenerator mazeGenerator =  new MyMazeGenerator();
        Maze maze = mazeGenerator.generate(row, col);
        int[][] mazeGrid = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                mazeGrid[i][j] = maze.getCellValue(i,j);
            }
        }
        return mazeGrid;

    }



    @Override
    public void saveMaze(Maze maze) {

    }

    @Override
    public void savePlayer(int row, int col) {

    }

    @Override
    public Maze loadMaze(String path) {
        return null;
    }
}
