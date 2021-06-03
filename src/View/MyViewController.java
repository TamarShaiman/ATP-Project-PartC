package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import algorithms.mazeGenerators.*;
import ViewModel.*;
import Model.*;


public class MyViewController implements Initializable, IView, Observer {
    public MyModel generator;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    private MyViewModel viewModel;
    private int [][] maze;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();



    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void generateMaze() {
        if(generator == null) {
            generator = new MyModel();
            viewModel = new MyViewModel(generator);
            this.viewModel.addObserver(this);
        }

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        viewModel.generateMaze(rows,cols);
/*
        mazeDisplayer.drawMaze(maze);
        setPlayerPosition(0, 0);*/
    }

    public void solveMaze() {
        this.viewModel.solveMaze();
/*        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Solving maze...");
        alert.show();
       generator.solveMaze();*/

    }



    public void keyPressed(KeyEvent keyEvent) {
/*        int row = mazeDisplayer.getPlayerRow();
        int col = mazeDisplayer.getPlayerCol();*/

        /*switch (keyEvent.getCode()) {
            case UP -> row -= 1;
            case DOWN -> row += 1;
            case RIGHT -> col += 1;
            case LEFT -> col -= 1;
            case NUMPAD8 -> row -= 1;
            case NUMPAD2 -> row += 1;
            case NUMPAD6 -> col += 1;
            case NUMPAD4 -> col -= 1;
            case NUMPAD9 -> {
                row -= 1;  col += 1;
            }
            case NUMPAD3 -> {
                row += 1;  col += 1;
            }
            case NUMPAD1 -> {
                row += 1;  col -= 1;
            }
            case NUMPAD7 -> {
                row -= 1;  col -= 1;
            }

        }*/

        this.viewModel.moveCharacter(keyEvent);
//        setPlayerPosition(row, col);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void setPlayerPosition(int row, int col){
        if(mazeDisplayer.setPlayerPosition(row, col) ) {
            setUpdatePlayerRow(row);
            setUpdatePlayerCol(col);
        }
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = this.viewModel.getMaze();
                drawMaze();

            }
            else {
                int[][] viewModelMaze = this.viewModel.getMaze();
                if (this.maze != this.viewModel.getMaze()){
                    this.maze = viewModelMaze;
                    drawMaze();
                }

                else{
                    int rowChar = mazeDisplayer.getPlayerRow();
                    int colChar = mazeDisplayer.getPlayerCol();
                    if (rowChar != this.viewModel.getRowChar() || colChar != this.viewModel.getColChar()) {
                        if (this.viewModel.getRowChar() == this.viewModel.getRowGoal() && this.viewModel.getColChar() == this.viewModel.getColGoal()) { //character has reached the goal
                            System.out.println("Goal! woohoo!!");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Goal! woohoo!!");
                            alert.show();
                        } else {
                            setUpdatePlayerRow(this.viewModel.getRowChar());
                            setUpdatePlayerCol(this.viewModel.getColChar());
                            mazeDisplayer.setPlayerPosition(this.viewModel.getRowChar(),this.viewModel.getColChar());
                        }
                        drawMaze();

                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("invalid step");
                        alert.show();
                    }
                }
            }
        }
    }
}

