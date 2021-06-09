package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import ViewModel.*;


public class MyViewController implements Initializable, IView, Observer {

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    private MyViewModel viewModel;
    private int [][] maze;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    Boolean soundCheck = true;

    String musicFileMusic = "./resources/Music/Music1.mp3";// For example
    File file = new File(musicFileMusic);
    Media music = new Media(file.toURI().toString());
    MediaPlayer mediaPlayerMusic = new MediaPlayer(music);

    String musicFileSound = "./resources/Sound/failureSound.mp3";// For example
    File fileSound = new File(musicFileSound);
    Media sound = new Media(fileSound.toURI().toString());
    MediaPlayer mediaPlayerSound = new MediaPlayer(sound);


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
        this.viewModel.addObserver(this);
    }

    public void generateMaze() {

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        viewModel.generateMaze(rows,cols);
    }

    public void solveMaze() {
        this.viewModel.solveMaze();
    }

    public void unsolveMaze() {
        this.mazeDisplayer.showSolution = false;
        drawMaze();
    }

    public void keyPressed(KeyEvent keyEvent) {
        this.viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void setPlayerPosition(int row, int col){//
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }


    public void drawMaze()
    {
        mazeDisplayer.drawMaze(maze);
    }

    @Override

    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {

            String change = (String)arg;
            switch(change){

                case "maze generated" -> mazeGenerated();
                case "player moved" -> playerMoved();
                case "maze solved" -> mazeSolved();
                case "invalid step" -> invalidStep();
                case "load maze" -> loadedMaze();
            }
        }
    }

    private void loadedMaze() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        playerMoved();
    }

    private void invalidStep() {
        if(soundCheck){
        mediaPlayerSound.play();
        mediaPlayerSound.seek(mediaPlayerSound.getStartTime());
        }
        System.out.println("invalid");

    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void mazeGenerated() {
        mazeDisplayer.drawMaze(viewModel.getMaze());
        playerMoved();
    }

    private void playerMoved() {
        setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
    }

    public void MusicCheckBox(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) actionEvent.getSource();
            {
                if (checkBox.isSelected()) {
                    mediaPlayerMusic.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayerMusic.play();
                }
                else{
                    mediaPlayerMusic.stop();
                }
            }
        }
    }

    public void solveCheckBox(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) actionEvent.getSource();
            {
                if (checkBox.isSelected()) {
                    solveMaze();
                }
                else{
                    unsolveMaze();
                }
            }
        }
    }

    public void saveMaze(ActionEvent actionEvent) {
        this.viewModel.saveMaze();
    }

    public void loadMaze(ActionEvent actionEvent) {
        this.viewModel.loadMaze();
    }

    public void SoundCheckBox(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) actionEvent.getSource();
            {
                if (checkBox.isSelected()) {
                    soundCheck = true;
                }
                else{
                    soundCheck = false;
                }
            }
        }
    }
}

