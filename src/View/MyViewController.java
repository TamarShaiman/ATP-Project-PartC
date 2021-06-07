package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
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

    public Pane mazePane;
/*    public ScrollPane scrollPaneMaze;*/
    public BorderPane borderPane;
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
        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());

    /*    scrollPaneMaze.prefHeightProperty().bind(mazeDisplayer.heightProperty());
        scrollPaneMaze.prefWidthProperty().bind(mazeDisplayer.widthProperty());
        scrollPaneMaze.prefHeightProperty().bind(mazePane.heightProperty());
        scrollPaneMaze.prefWidthProperty().bind(mazePane.widthProperty());
        scrollPaneMaze.setContent(mazeDisplayer);
        scrollPaneMaze.setFitToWidth(true);
        scrollPaneMaze.setFitToHeight(true);*/
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

    public void keyPressed(KeyEvent keyEvent) {
        this.viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void setPlayerPosition(int row, int col){
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
                case "player moved right" -> playerMovedRight();
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

    private void playerMovedRight() {
        setPlayerPosition(viewModel.getRowChar(), viewModel.getColChar());
    }

    public void MusicCheckBox(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) actionEvent.getSource();
            {
                if (checkBox.isSelected()) {
                    mediaPlayerMusic.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayerMusic.play();
                    mediaPlayerMusic.seek(mediaPlayerMusic.getStartTime());

                }
                else{
                    mediaPlayerMusic.stop();
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

    public void scrollMaze(ScrollEvent scrollEvent) {
        double deltaY = scrollEvent.getDeltaY();
        if(scrollEvent.isControlDown()) {
            double zoomFactor = 1.05;
            if (deltaY < 0) {
                zoomFactor = 0.95;
            }

            Scale newScale = new Scale();
/*
            newScale.setX(scrollPaneMaze.getScaleX() * zoomFactor);
            newScale.setY(scrollPaneMaze.getScaleY() * zoomFactor);
            newScale.setPivotX(scrollPaneMaze.getScaleX());
            newScale.setPivotY(scrollPaneMaze.getScaleY());
*/

            newScale.setX(mazePane.getScaleX() * zoomFactor);
            newScale.setY(mazePane.getScaleY() * zoomFactor);
            newScale.setPivotX(mazePane.getScaleX());
            newScale.setPivotY(mazePane.getScaleY());
/*
            mazeDisplayer.widthProperty().bind(scrollPaneMaze.widthProperty());
            mazeDisplayer.heightProperty().bind(scrollPaneMaze.heightProperty());
            scrollPaneMaze.getTransforms().add(newScale);*/
            mazePane.getTransforms().add(newScale);
            mazeDisplayer.getTransforms().add(newScale);

/*
            scrollPaneMaze.setContent(mazeDisplayer);
*/


            scrollEvent.consume();
        }
    }
    public void setResizeEvent(Scene primeScene) {

        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());

        primeScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
            mazeDisplayer.drawMaze(viewModel.getMaze());
            System.out.println("Height: " + primeScene.getHeight() + " Width: " + primeScene.getWidth());

        });
        primeScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
            mazeDisplayer.drawMaze(viewModel.getMaze());
            System.out.println("Height: " + primeScene.getHeight() + " Width: " + primeScene.getWidth());

        });
    }

    public Pane getMazePane() {
        return mazePane;
    }

    public void setMazePane(Pane pane) {
        this.mazePane = pane;
    }

    public void mouseDragged(MouseEvent mouseEvent) {

        System.out.println("sysysys");
/*        mazeDisplayer.widthProperty().bind(mazePane.widthProperty());
        mazeDisplayer.heightProperty().bind(mazePane.heightProperty());
        scrollBarHor.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mazeDisplayer.setLayoutX(t1.doubleValue());
                mazePane.setLayoutX(t1.doubleValue());

            }
        });
        scrollBarVer.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mazeDisplayer.setLayoutY(t1.doubleValue());
                mazePane.setLayoutY(t1.doubleValue());

            }
        });
*/
        mouseEvent.consume();
    }
}

