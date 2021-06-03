package Model;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.*;
import  Server.*;
import  Client.*;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import java.util.Observable;
import javafx.collections.ObservableArrayBase;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    Maze maze;
    int[][] mazeTable;
    int rowChar;
    int colChar;
    int colStart;
    int rowStart;
    int colGoal;
    int rowGoal;
    Solution mazeSolution;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401,1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        this.maze = null;
        rowChar = 0;
        colChar = 0;
    }

    @Override
    public void generateMaze(int row, int col) {
        int[][] mazeGrid = new int[row][col];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[((row * col) + 44) ];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        rowChar = maze.getStartPosition().getRowIndex();
                        colChar = maze.getStartPosition().getColIndex();
                        rowStart = maze.getStartPosition().getRowIndex();
                        colStart = maze.getStartPosition().getColIndex();
                        rowGoal = maze.getGoalPosition().getRowIndex();
                        colGoal = maze.getGoalPosition().getColIndex();
                        maze.print();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            mazeTable = new int[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    mazeTable[i][j] = maze.getCellValue(i, j);
                }
            }
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers("maze generated");
    }

    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        maze.print();
                        toServer.writeObject(maze);
                        toServer.flush();
                         mazeSolution = (Solution)fromServer.readObject();
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        /*mazeSolutionSteps = new ArrayList<>();
                        mazeSolutionSteps = mazeSolution.getSolutionPath();*/

                        /*for(int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            System.out.println(String.format("%s. %s", i, ((AState)mazeSolutionSteps.get(i)).toString()));
                        }*/
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers("maze solved");
    }


    @Override
    public int[][] getMaze() {
        return this.mazeTable;
    }

    public int getColStart() {
        return colStart;
    }

    public int getRowStart() {
        return rowStart;
    }

    public int getColGoal() {
        return colGoal;
    }

    public int getRowGoal() {
        return rowGoal;
    }

    @Override
    public Solution getSolution() {
        return mazeSolution;
    }


    @Override
    public void updateCharacterLocation(int direction) {
         /*
            direction = 1 -> Up
            direction = 2 -> Up Right
            direction = 3 -> Right
            direction = 4 -> Right Down
            direction = 5 -> Down
            direction = 6 -> Down Left
            direction = 7 -> Left
            direction = 8 -> Left Up
         */
        String arg = "invalid step";

        switch(direction)
        {
            case 1: //Up
                if(rowChar!=0 && mazeTable[rowChar-1][colChar]==0) {
                    rowChar--;
                    arg = "player moved";
                }
                break;
            case 2: //Up Right
                  if(rowChar!=0 && colChar!=mazeTable[0].length-1 && mazeTable[rowChar-1][colChar+1]==0) {
                      rowChar--;
                      colChar++;
                      arg = "player moved";
                  }
                break;
            case 3: //Right
                  if(colChar!=mazeTable[0].length-1 && mazeTable[rowChar][colChar+1]==0) {
                      colChar++;
                      arg = "player moved";
                  }
                break;
            case 4: //Right Down
                  if(colChar!=mazeTable[0].length-1 && rowChar!=mazeTable.length+1 && mazeTable[rowChar+1][colChar+1]==0) {
                      rowChar++;
                      colChar++;
                      arg = "player moved";
                  }
                break;
            case 5: //Down
                if(rowChar!=mazeTable.length-1 && mazeTable[rowChar+1][colChar]==0) {
                    rowChar++;
                    arg = "player moved";
                }
                break;
            case 6: //Down Left
                  if(rowChar!=mazeTable.length-1 && colChar!=0 && mazeTable[rowChar+1][colChar-1]==0) {
                      rowChar++;
                      colChar--;
                      arg = "player moved";
                  }
                break;
            case 7: //Left
                  if(colChar!=0 && mazeTable[rowChar][colChar-1]==0) {
                      colChar--;
                      arg = "player moved";
                  }
                break;
            case 8: //Left Up
                  if(colChar!=0 && rowChar!=0 && mazeTable[rowChar-1][colChar-1]==0) {
                      colChar--;
                      rowChar--;
                      arg = "player moved";
                  }
                  break;
        }
        setChanged();
        notifyObservers(arg);
    }

    @Override
    public int getRowChar() {
        return rowChar;
    }

    @Override
    public int getColChar() {
        return colChar;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public int[][] getSolutionPath() {
        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
        int[][] pathSolutionByIntArray = new int[mazeSolutionSteps.size()][2];
        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
            if(mazeSolutionSteps.get(i) instanceof MazeState){
                pathSolutionByIntArray[i][0] = ((MazeState) mazeSolutionSteps.get(i)).getPosition().getRowIndex();
                pathSolutionByIntArray[i][1] = ((MazeState) mazeSolutionSteps.get(i)).getPosition().getColIndex();
            }

        }
        return pathSolutionByIntArray;
    }
    /*
    @Override
    public void saveMaze(Maze maze) {

    }

    @Override
    public void savePlayer(int row, int col) {

    }
*/
/*
    @Override
    public Maze loadMaze(String path) {
        return null;
    }*/
}
