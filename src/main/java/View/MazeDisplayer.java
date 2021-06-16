package View;

import algorithms.mazeGenerators.Position;
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
    private String playerName = "Hansel";
    boolean showSolution = false;
    public boolean won = false;

    //images:
    //backgrounds:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePath = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNameBreadCrumb= new SimpleStringProperty();
    StringProperty imageFileNameVictory= new SimpleStringProperty();
    StringProperty imageFileNameBackground = new SimpleStringProperty();

    //Gretel:
    StringProperty imageFileNameGretel = new SimpleStringProperty();
    StringProperty imageFileNameGretelRight1 = new SimpleStringProperty();
    StringProperty imageFileNameGretelRight2 = new SimpleStringProperty();
    StringProperty imageFileNameGretelLeft1 = new SimpleStringProperty();
    StringProperty imageFileNameGretelLeft2 = new SimpleStringProperty();
    StringProperty imageFileNameGretelFront1 = new SimpleStringProperty();
    StringProperty imageFileNameGretelFront2 = new SimpleStringProperty();
    StringProperty imageFileNameGretelBack1 = new SimpleStringProperty();
    StringProperty imageFileNameGretelBack2 = new SimpleStringProperty();
    StringProperty imageFileNameGretelBack = new SimpleStringProperty();

    //Hansel:
    StringProperty imageFileNameHansel = new SimpleStringProperty();
    StringProperty imageFileNameHanselRight1 = new SimpleStringProperty();
    StringProperty imageFileNameHanselRight2 = new SimpleStringProperty();
    StringProperty imageFileNameHanselLeft1 = new SimpleStringProperty();
    StringProperty imageFileNameHanselLeft2 = new SimpleStringProperty();
    StringProperty imageFileNameHanselFront1 = new SimpleStringProperty();
    StringProperty imageFileNameHanselFront2 = new SimpleStringProperty();
    StringProperty imageFileNameHanselBack1 = new SimpleStringProperty();
    StringProperty imageFileNameHanselBack2 = new SimpleStringProperty();
    StringProperty imageFileNameHanselBack = new SimpleStringProperty();

    //StringProperty imageFileNameVoid = new SimpleStringProperty();




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
        if (solution != null) {
            this.solution = solution;
            this.showSolution = true;
            draw(false, true, 0, 0, 0, 0);
        }
    }

    public void setPlayerPosition(int row, int col) {
        int prevRow = this.playerRow;
        int prevCol = this.playerCol;
        this.playerRow = row;
        this.playerCol = col;
        boolean hasMoved = !(prevCol == col && prevRow == row);
        draw(hasMoved, showSolution, row, col, prevRow, prevCol);
    }

    public String getImageFileBackground() {
        return imageFileNameBackground.get();
    }

    public String getImageFileNameBreadCrumb() {
        return imageFileNameBreadCrumb.get();
    }

    public String getImageFileNameVictory() {
        return imageFileNameVictory.get();
    }

    public void setImageFileNameVictory(String imageFileNameVictory) {
        this.imageFileNameVictory.set(imageFileNameVictory);
    }

    public void setImageBackground(String imageFileNameBackground) {
        this.imageFileNameBackground.set(imageFileNameBackground);
    }

    public void setImageFileNameBreadCrumb(String imageFileNameBreadCrumb) {
        this.imageFileNameBreadCrumb.set(imageFileNameBreadCrumb);
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getImageFileNameGoal() { return imageFileNameGoal.get(); }

    public String getImageFileNamePath() {
        return imageFileNamePath.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public String imageFileNameGoalProperty() {
        return imageFileNameGoal.get();
    }

    public String imageFileNamePathProperty() {
        return imageFileNamePath.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    public void setImageFileNamePath(String imageFileNamePath) {
        this.imageFileNamePath.set(imageFileNamePath);
    }

    public String getImageFileNameGretel() {
        return imageFileNameGretel.get();
    }

    public String getImageFileNameHansel() {
        return imageFileNameHansel.get();
    }

    public String getImageFileNameGretelRight1() {
        return imageFileNameGretelRight1.get();
    }

    public String getImageFileNameHanselRight1() {
        return imageFileNameHanselRight1.get();
    }

    public String getImageFileNameGretelRight2() {
        return imageFileNameGretelRight2.get();
    }

    public String getImageFileNameHanselRight2() {
        return imageFileNameHanselRight2.get();
    }

    public String getImageFileNameGretelLeft1() {
        return imageFileNameGretelLeft1.get();
    }

    public String getImageFileNameHanselLeft1() {
        return imageFileNameHanselLeft1.get();
    }

    public String getImageFileNameGretelLeft2() {
        return imageFileNameGretelLeft2.get();
    }

    public String getImageFileNameHanselLeft2() {
        return imageFileNameHanselLeft2.get();
    }

    public String getImageFileNameGretelFront1() {
        return imageFileNameGretelFront1.get();
    }

    public String getImageFileNameHanselFront1() {
        return imageFileNameHanselFront1.get();
    }

    public String getImageFileNameGretelFront2() {
        return imageFileNameGretelFront2.get();
    }

    public String getImageFileNameHanselFront2() {
        return imageFileNameHanselFront2.get();
    }


    public String getImageFileNameGretelBack1() {
        return imageFileNameGretelBack1.get();
    }

    public String getImageFileNameHanselBack1() {
        return imageFileNameHanselBack1.get();
    }

    public String getImageFileNameGretelBack2() {
        return imageFileNameGretelBack2.get();
    }

    public String getImageFileNameHanselBack2() {
        return imageFileNameHanselBack2.get();
    }

    public String getImageFileNameGretelBack() {
        return imageFileNameGretelBack.get();
    }

    public String getImageFileNameHanselBack() {
        return imageFileNameHanselBack.get();
    }

//    public String getImageFileNameVoid() { return imageFileNameVoid.get(); }

    public String imageFileNamePlayerProperty() {
        if (this.playerName.equals("Gretel"))
            return imageFileNameGretel.get();
        else if (this.playerName.equals("Hansel"))
            return imageFileNameHansel.get();
        return null;
    }

    public void setImageFileNameGretel(String imageFileNamePlayer) {
        imageFileNameGretel.set(imageFileNamePlayer);
    }

    public void setImageFileNameHansel(String imageFileNamePlayer) {
        imageFileNameHansel.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelRight1(String imageFileNamePlayer) {
        imageFileNameGretelRight1.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselRight1(String imageFileNamePlayer) {
        imageFileNameHanselRight1.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelRight2(String imageFileNamePlayer) {
        imageFileNameGretelRight2.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselRight2(String imageFileNamePlayer) {
        imageFileNameHanselRight2.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelLeft1(String imageFileNamePlayer) {
        imageFileNameGretelLeft1.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselLeft1(String imageFileNamePlayer) {
        imageFileNameHanselLeft1.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelLeft2(String imageFileNamePlayer) {
        imageFileNameGretelLeft2.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselLeft2(String imageFileNamePlayer) {
        imageFileNameHanselLeft2.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelFront1(String imageFileNamePlayer) {
        imageFileNameGretelFront1.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselFront1(String imageFileNamePlayer) {
        imageFileNameHanselFront1.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelFront2(String imageFileNamePlayer) {
        imageFileNameGretelFront2.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselFront2(String imageFileNamePlayer) {
        imageFileNameHanselFront2.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelBack1(String imageFileNamePlayer) {
        imageFileNameGretelBack1.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselBack1(String imageFileNamePlayer) {
        imageFileNameHanselBack1.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelBack2(String imageFileNamePlayer) {
        imageFileNameGretelBack2.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselBack2(String imageFileNamePlayer) {
        imageFileNameHanselBack2.set(imageFileNamePlayer);
    }

    public void setImageFileNameGretelBack(String imageFileNamePlayer) {
        imageFileNameGretelBack.set(imageFileNamePlayer);
    }

    public void setImageFileNameHanselBack(String imageFileNamePlayer) {
        imageFileNameHanselBack.set(imageFileNamePlayer);
    }

//    public void setImageFileNameVoid(String imageFileName) {
//        this.imageFileNameVoid.set(imageFileName);
//    }

    public void drawMaze(int[][] maze) {
        this.maze = maze;
        draw(false, this.showSolution, 0,0,0, 0);
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

            if (this.won) {
                drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                drawPlayer(graphicsContext, cellHeight, cellWidth);
                if (showSolution)
                    drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                drawVictory();
            }
            else {

                drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                if (hasMoved)
                    drawMovement(graphicsContext, cellHeight, cellWidth, playerRow, playerCol, prevRow, prevCol, rows, cols);
                else
                    drawPlayer(graphicsContext, cellHeight, cellWidth);
                if (showSolution)
                    drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                if (won)
                    drawVictory();
                drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            }
        }
    }

    private void drawMovement(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int playerRow, int playerCol, int prevRow, int prevCol, int rows, int cols) {
        if (maze != null) {
            if (playerCol == this.getGoalCol() && playerRow == this.getGoalRow()) {
                //draw(false,  showSolution,  playerRow,  playerCol,  prevRow,  prevCol);
//                this.won = true;
                drawVictory();
            } else {
                double[] coordinates = {0, 0, 0, 0}; //{x1, y1, x2, y2}
                Image[] images = {null, null}; //{image1, image2}

                calculateXandY(prevRow, prevCol, playerRow, playerCol, cellHeight, cellWidth, coordinates, images);

                if (images[0] != null && images[1] != null) {
                    Timer timer1 = new Timer();
                    if (showSolution)
                        drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                    graphicsContext.drawImage(images[0], coordinates[0], coordinates[1], cellWidth, cellHeight);

                    timer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                            if (showSolution)
                                drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                            graphicsContext.drawImage(images[1], coordinates[2], coordinates[3], cellWidth, cellHeight);
                        }
                    }, 90);


                    timer1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            drawMazePaths(graphicsContext, cellHeight, cellWidth, rows, cols);
                            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
                            if (showSolution)
                                drawSolution(graphicsContext, cellHeight, cellWidth, rows, cols);
                            if (prevRow == playerRow + 1 && prevCol == playerCol) //player went up
                                drawPlayerBack(graphicsContext, cellHeight, cellWidth);
                            else
                                drawPlayer(graphicsContext, cellHeight, cellWidth);
                        }
                    }, 200);
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelRight2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselRight2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelLeft2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselLeft2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelBack1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelBack2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselBack1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselBack2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelFront1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelFront2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselFront1()));
                    //System.out.println("hello");
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselFront1()));
                    //System.out.println("olah");
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelRight2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselRight2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelRight2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselRight1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselRight2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelLeft2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselLeft2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretelLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretelLeft2()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHanselLeft1()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHanselLeft2()));
                }
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
                if (this.playerName.equals("Gretel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameGretel()));
                    images[1] = new Image(new FileInputStream(getImageFileNameGretel()));
                }
                if (this.playerName.equals("Hansel")) {
                    images[0] = new Image(new FileInputStream(getImageFileNameHansel()));
                    images[1] = new Image(new FileInputStream(getImageFileNameHansel()));
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("There is no player image file");
            }
        }
    }

    public void drawVictory(){
        GraphicsContext graphicsContext = this.getGraphicsContext2D();
        graphicsContext.setFill(Color.RED);
        Image victoryImage = null;
        try {
            victoryImage = new Image(new FileInputStream(getImageFileNameVictory()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no victory image file");
        }
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        if (victoryImage == null)
            graphicsContext.fillRect(0,0, canvasWidth , canvasHeight);
        else
            graphicsContext.drawImage(victoryImage, 0,0, canvasWidth , canvasHeight);
    }


    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            if (this.playerName.equals("Gretel"))
                playerImage = new Image(new FileInputStream(getImageFileNameGretel()));
            if (this.playerName.equals("Hansel"))
                playerImage = new Image(new FileInputStream(getImageFileNameHansel()));
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
            if (this.playerName.equals("Gretel"))
                playerImage = new Image(new FileInputStream(getImageFileNameGretelBack()));
            if (this.playerName.equals("Hansel"))
                playerImage = new Image(new FileInputStream(getImageFileNameHanselBack()));
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
            System.out.println("There is no solution image file");
        }

        for (AState step: this.solution.getSolutionPath()) {
            int stepRow = ((MazeState)step).getPosition().getRowIndex();
            int stepCol = ((MazeState)step).getPosition().getColIndex();
            if (stepRow == this.playerRow && stepCol == this.playerCol){
                continue;
            }
/*            double y = stepRow * cellWidth;
            double x = stepCol * cellHeight;*/
            double y = stepRow * cellHeight;
            double x = stepCol * cellWidth;
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
        Image goalImage = null;

        try {
            pathImage = new Image(new FileInputStream(getImageFileNamePath()));
            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no path image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0) {
                    //if it is a path:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if (pathImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(pathImage, x, y, cellWidth, cellHeight);
                }
            }
        }
        if (goalImage != null) {
            double x = goalCol * cellWidth;
            double y = goalRow * cellHeight;
            graphicsContext.drawImage(goalImage, x, y, cellWidth, cellHeight);
        }
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getGoalRow() {
        return goalRow;
    }

    public void setGoalRow(int goalRow) {
        this.goalRow = goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }

    public void setGoalCol(int goalCol) {
        this.goalCol = goalCol;
    }

    public void setPlayer(String playerName) {
        this.playerName = playerName;
    }
}
