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
    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    Maze maze;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    @Override
    public int[][] generateMaze(int row, int col) {
        int[][] mazeGrid = new int[row][col];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        System.out.println("Hiiii");
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
                        maze.print();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
         /*    mazeGrid = new int[row][col];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    mazeGrid[i][j] = maze.getCellValue(i, j);
                }
            }*/
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
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
