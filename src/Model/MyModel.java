package Model;
import algorithms.mazeGenerators.*;

import java.util.Arrays;

public class MyModel {
    /*public static void main(String[] args) {
        AMazeGenerator generator = new MyMazeGenerator();
        Maze maze = generator.generate(5, 5);
        maze.print();*/
        //System.out.println(Arrays.deepToString(maze));


    public int[][] generateRandomMaze(int rows, int cols){
        int[][] maze = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = (int) Math.round(Math.random());
            }
        }
        return maze;
    }
}
