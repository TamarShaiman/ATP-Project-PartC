package Model;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.*;
import  Server.*;
import  Client.*;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class MyModel implements IModel {
    /*Server mazeGeneratingServer;
    Server solveSearchProblemServer;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        CommunicateWithServer_SolveSearchProblem();
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }
    private static void CommunicateWithServer_MazeGenerating(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public  void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    int[][] mazeGrid = null;
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[((row*col)+44)/8];
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);

                        maze.print();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                    //return mazeGrid;

                }
            });

            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(50, 50);
                        maze.print();
                        toServer.writeObject(maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();

                        for(int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            System.out.println(String.format("%s. %s", i, ((AState)mazeSolutionSteps.get(i)).toString()));
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }


*/
    @Override
    public int[][] generateMaze(int row, int col) {
        /*solveSearchProblemServer.start();
        mazeGeneratingServer.start();*/
       // CommunicateWithServer_MazeGenerating(10,10);

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
