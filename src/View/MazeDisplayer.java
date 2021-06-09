package View;

import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    private int startRow;
    private int startCol;
    private int goalRow;
    private int goalCol;
    private Solution solution;
    boolean showSolution = false;


    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePath = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNamePlayerRight1 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerRight2 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerLeft1 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerLeft2 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerFront1 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerFront2 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerBack1 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerBack2 = new SimpleStringProperty();
    StringProperty imageFileNamePlayerBack = new SimpleStringProperty();

    StringProperty imageFileNameBreadCrumb= new SimpleStringProperty();
    StringProperty imageFileNameVoid = new SimpleStringProperty();


    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }
    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        this.showSolution = true;
        draw(false, true,0,0,0,0);
    }

    public void setPlayerPosition(int row, int col) {
        int prevRow = this.playerRow;
        int prevCol = this.playerCol;
        this.playerRow = row;
        this.playerCol = col;
        boolean hasMoved = !(prevCol == col && prevRow == row);
        draw(hasMoved, showSolution, row, col, prevRow, prevCol);
    }

    private boolean checkBorder(int row, int col) {
        if(row >= 0  && row < maze.length && col >= 0  && col < maze[0].length){
            return true;
        }
        return false;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getImageFileNamePath() {
        return imageFileNamePath.get();
    }

    public String getImageFileNameBreadCrumb() {
        return imageFileNameBreadCrumb.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public String imageFileNamePathProperty() {
        return imageFileNamePath.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNamePath(String imageFileNamePath) {
        this.imageFileNamePath.set(imageFileNamePath);
    }

    public void setImageFileNameBreadCrumb(String imageFileNameBreadCrumb) {
        this.imageFileNameBreadCrumb.set(imageFileNameBreadCrumb);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getImageFileNamePlayerRight1() {
        return imageFileNamePlayerRight1.get();
    }

    public String getImageFileNamePlayerRight2() {
        return imageFileNamePlayerRight2.get();
    }

    public String getImageFileNamePlayerLeft1() {
        return imageFileNamePlayerLeft1.get();
    }

    public String getImageFileNamePlayerLeft2() {
        return imageFileNamePlayerLeft2.get();
    }

    public String getImageFileNamePlayerFront1() {
        return imageFileNamePlayerFront1.get();
    }

    public String getImageFileNamePlayerFront2() {
        return imageFileNamePlayerFront2.get();
    }

    public String getImageFileNamePlayerBack1() {
        return imageFileNamePlayerBack1.get();
    }

    public String getImageFileNamePlayerBack2() {
        return imageFileNamePlayerBack2.get();
    }

    public String getImageFileNamePlayerBack() {
        return imageFileNamePlayerBack.get();
    }

    public String getImageFileNameVoid() {
        return imageFileNameVoid.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerRight1(String imageFileNamePlayer) {
        this.imageFileNamePlayerRight1.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerRight2(String imageFileNamePlayer) {
        this.imageFileNamePlayerRight2.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerLeft1(String imageFileNamePlayer) {
        this.imageFileNamePlayerLeft1.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerLeft2(String imageFileNamePlayer) {
        this.imageFileNamePlayerLeft2.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerFront1(String imageFileNamePlayer) {
        this.imageFileNamePlayerFront1.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerFront2(String imageFileNamePlayer) {
        this.imageFileNamePlayerFront2.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerBack1(String imageFileNamePlayer) {
        this.imageFileNamePlayerBack1.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerBack2(String imageFileNamePlayer) {
        this.imageFileNamePlayerBack2.set(imageFileNamePlayer);
    }

    public void setImageFileNamePlayerBack(String imageFileNamePlayer) {
        this.imageFileNamePlayerBack.set(imageFileNamePlayer);
    }

    public void setImageFileNameVoid(String imageFileName) {
        this.imageFileNameVoid.set(imageFileName);
    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw(false, this.showSolution,0,0, 0,0);
    }

    private void draw(boolean hasMoved, boolean showSolution, int playerRow, int playerCol, int prevRow, int prevCol) {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);


            drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
            if(showSolution)
                drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
            if (hasMoved)
                drawMovement(graphicsContext, cellHeight, cellWidth, playerRow, playerCol, prevRow, prevCol, rows, cols);
            else
                drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
        }
    }

    private void drawMovement(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int playerRow, int playerCol, int prevRow, int prevCol, int rows, int cols) {
        if (maze != null) {
            double[] coordinates  = {0,0,0,0}; //{x1, y1, x2, y2}
            Image[] images = {null, null}; //{image1, image2}

            calculateXandY(prevRow, prevCol, playerRow, playerCol, cellHeight, cellWidth, coordinates, images);

            if (images[0] != null && images[1] != null) {
                Timer timer1 = new Timer();
                graphicsContext.drawImage(images[0], coordinates[0], coordinates[1], cellWidth, cellHeight);

                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //graphicsContext.drawImage(images[1], coordinates[2], coordinates[3], cellWidth, cellHeight);
                        drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                        drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                        graphicsContext.drawImage(images[1], coordinates[2], coordinates[3], cellWidth, cellHeight);
                    }
                }, 100);

/*                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                        drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                    }
                }, 1000);*/


                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        /*if(prevRow == playerRow+1 && prevCol == playerCol) //player went up
                            drawPlayerBack(graphicsContext, cellHeight, cellWidth);
                        else
                            drawPlayer(graphicsContext, cellHeight, cellWidth);*/
                        drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                        drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                        if(prevRow == playerRow+1 && prevCol == playerCol) //player went up
                            drawPlayerBack(graphicsContext, cellHeight, cellWidth);
                        else
                            drawPlayer(graphicsContext, cellHeight, cellWidth);

                    }
                }, 150);
                //draw(false, showSolution, 0,0,0,0);
            }
        }
    }

    private void calculateXandY(int prevRow, int prevCol, int playerRow, int playerCol, double cellHeight, double cellWidth, double[] coordinates, Image [] images) {
        if(prevRow == playerRow && prevCol == playerCol-1) { //right
            coordinates[0] = getPlayerCol() * cellWidth - cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight;
            coordinates[2] = getPlayerCol() * cellWidth - cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerRight1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerRight2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow && prevCol == playerCol+1) { //Left
            coordinates[0] = getPlayerCol() * cellWidth + cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight;
            coordinates[2] = getPlayerCol() * cellWidth + cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerLeft1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerLeft2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow+1 && prevCol == playerCol) { //Up
            coordinates[0] = getPlayerCol() * cellWidth;
            coordinates[1] = getPlayerRow() * cellHeight + cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth;
            coordinates[3] = getPlayerRow() * cellHeight + cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerBack1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerBack2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow-1 && prevCol == playerCol) { //Down
            coordinates[0] = getPlayerCol() * cellWidth;
            coordinates[1] = getPlayerRow() * cellHeight - cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth;
            coordinates[3] = getPlayerRow() * cellHeight - cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerFront1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerFront2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow-1 && prevCol == playerCol-1) { //Right Down
            coordinates[0] = getPlayerCol() * cellWidth - cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight - cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth - cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight - cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerRight1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerRight2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow+1 && prevCol == playerCol-1) { //Right Up
            coordinates[0] = getPlayerCol() * cellWidth - cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight + cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth - cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight + cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerRight1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerRight2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow-1 && prevCol == playerCol+1) { //Left Down
            coordinates[0] = getPlayerCol() * cellWidth + cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight - cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth + cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight - cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerLeft1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerLeft2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }

        else if(prevRow == playerRow+1 && prevCol == playerCol+1) { //Left Up
            coordinates[0] = getPlayerCol() * cellWidth + cellWidth * 2 / 3;
            coordinates[1] = getPlayerRow() * cellHeight + cellWidth * 2 / 3;
            coordinates[2] = getPlayerCol() * cellWidth + cellWidth * 1 / 3;
            coordinates[3] = getPlayerRow() * cellHeight + cellWidth * 1 / 3;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayerLeft1()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayerLeft2()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }


        else{ //nothing
            coordinates[0] = getPlayerCol() * cellWidth;
            coordinates[1] = getPlayerRow() * cellHeight;
            coordinates[2] = getPlayerCol() * cellWidth;
            coordinates[3] = getPlayerRow() * cellHeight;
            try {
                images[0] = new Image(new FileInputStream(getImageFileNamePlayer()));
                images[1] = new Image(new FileInputStream(getImageFileNamePlayer()));
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    private void drawPlayerBack(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayerBack()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        System.out.println("drawing solution");
        graphicsContext.setFill(Color.YELLOW);


        Image BreadCrumbImage = null;
        try{
            BreadCrumbImage = new Image(new FileInputStream(getImageFileNameBreadCrumb()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no path image file");
        }

        for (AState step: this.solution.getSolutionPath()) {
            int stepRow = ((MazeState)step).getPosition().getRowIndex();
            int stepCol = ((MazeState)step).getPosition().getColIndex();
            double y = stepRow * cellWidth;
            double x = stepCol * cellHeight;
            if (BreadCrumbImage == null)
                graphicsContext.fillRect(x,y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(BreadCrumbImage, x, y, cellWidth, cellHeight);
        }


    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawMazePaths(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image pathImage = null;
        Image solutionImage = null;
        try{
            pathImage = new Image(new FileInputStream(getImageFileNamePath()));
            solutionImage = new Image(new FileInputStream((getImageFileNameBreadCrumb())));
        } catch (FileNotFoundException e) {
            System.out.println("There is no path image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 0){
                    //if it is a path:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(pathImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(pathImage, x, y, cellWidth, cellHeight);
                    /*if (this.showSolution){
                        graphicsContext.setFill(Color.YELLOW);
                        if(solutionImage == null)
                            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                        else
                            graphicsContext.drawImage(solutionImage, x, y, cellWidth, cellHeight);
                    }*/
                    if (this.showSolution)
                        drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                }
            }
        }
    }
}
