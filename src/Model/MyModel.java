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
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            });client.communicateWithServer();
            initMazeTable();

        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
        setChanged();
        notifyObservers("maze generated");
    };

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
                  }//
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

    @Override
    public void saveMaze() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Maze");
        chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Mazes Files","*.maze"));
        chooser.setInitialFileName("maze.maze");
        File file = chooser.showSaveDialog((Window)null);

        if (file != null) {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(this.maze);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

    }
    @Override
    public void savePlayer() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Player");
        chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("player position Files","*.playerPosition"));
        chooser.setInitialFileName("player.playerPosition");
        File file = chooser.showSaveDialog((Window)null);

        if (file != null) {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                int[] playerPosition = {rowChar,colChar};
                objectOutputStream.writeObject(playerPosition);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }
    }

    @Override
    public void loadMaze() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Mazes Files","*.maze"));
        chooser.setInitialFileName("maze.maze");
        chooser.setTitle("Load Maze");
        File file = chooser.showOpenDialog((Window)null);
        if (file != null) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                Maze maze = (Maze) objectInputStream.readObject();
                objectInputStream.close();
                this.maze = maze;
                initMazeTable();
                this.rowChar = maze.getStartPosition().getRowIndex();
                this.colChar = maze.getStartPosition().getColIndex();
                this.rowGoal = maze.getGoalPosition().getRowIndex();
                this.colGoal = maze.getGoalPosition().getColIndex();
            } catch (IOException var5) {
                var5.printStackTrace();
            } catch (ClassNotFoundException var6) {
                var6.printStackTrace();
            }
            this.setChanged();
            this.notifyObservers("load maze");
        }
    }

    private void initMazeTable() {
        mazeTable = new int[maze.getRowNum()][maze.getColNum()];
        for (int i = 0; i < maze.getRowNum(); i++) {
            for (int j = 0; j < maze.getColNum(); j++) {
                mazeTable[i][j] = maze.getCellValue(i, j);
            }
        }
    }

}
