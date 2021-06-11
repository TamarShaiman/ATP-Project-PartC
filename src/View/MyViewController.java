package View;

import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

import ViewModel.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class MyViewController implements Initializable, IView, Observer {

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    private MyViewModel viewModel;
    private int[][] maze;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    public Pane mazePane;
    /*    public ScrollPane scrollPaneMaze;*/
    public BorderPane borderPane;

    public Menu exitMenu;

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
        mazeDisplayer.widthProperty().bind(borderPane.widthProperty());
        mazeDisplayer.heightProperty().bind(borderPane.heightProperty());

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
        if(rows > 0 && cols > 0)
            viewModel.generateMaze(rows, cols);
        else
            invalidMazeSize();
    }

    public void solveMaze() {
        this.viewModel.solveMaze();
    }

    public void keyPressed(KeyEvent keyEvent) {
        this.viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


    public void setPlayerPosition(int row, int col) {//
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }


    public void drawMaze() {
        mazeDisplayer.drawMaze(maze);
    }

    @Override

    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {

            String change = (String) arg;
            switch (change) {

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
        if (soundCheck) {
            mediaPlayerSound.play();
            mediaPlayerSound.seek(mediaPlayerSound.getStartTime());
        }
        System.out.println("invalid");

    }

    private void mazeSolved() {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void mazeGenerated() {
        mazeDisplayer.setGoalCol(viewModel.getColGoal());
        mazeDisplayer.setGoalRow(viewModel.getRowGoal());
        mazeDisplayer.setStartCol(viewModel.getColStart());
        mazeDisplayer.setStartRow(viewModel.getRowStart());
        mazeDisplayer.drawMaze(viewModel.getMaze());
        mazeDisplayer.showSolution = false;
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
                } else {
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
                } else {
                    soundCheck = false;
                }
            }
        }
    }

    public void scrollMaze(ScrollEvent scrollEvent) {
        double deltaY = scrollEvent.getDeltaY();
        if (scrollEvent.isControlDown()) {
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
            mazeDisplayer.drawMaze(viewModel.getMaze());
            System.out.println("Width: " + primeScene.getWidth() + " Height: " + primeScene.getHeight());
        });
        primeScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.drawMaze(viewModel.getMaze());
            System.out.println("Width: " + primeScene.getWidth() + " Height: " + primeScene.getHeight());

        });
    }

    public Pane getMazePane() {
        return mazePane;
    }

    public void setMazePane(Pane pane) {
        this.mazePane = pane;
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (viewModel.getMaze() != null) {
            // calculate mouse position by X and Y
            int rows = viewModel.getMaze().length;
            int cols = viewModel.getMaze()[0].length;
            int maxOfRowsOrCols = Math.max(rows, cols);
            double newX = calculateMouse(maxOfRowsOrCols, mazeDisplayer.getWidth(), viewModel.getMaze().length, mouseEvent.getX(), mazeDisplayer.getWidth() / maxOfRowsOrCols);
            double newY = calculateMouse(maxOfRowsOrCols, mazeDisplayer.getHeight(), viewModel.getMaze()[0].length, mouseEvent.getY(), mazeDisplayer.getHeight() / maxOfRowsOrCols);
            double viewModelX = viewModel.getColChar();
            double viewModelY = viewModel.getRowChar();
            KeyCode keyCode = chooseDirection(newX, newY, viewModelX, viewModelY);
            this.viewModel.moveCharacter(keyCode);


        }
        mouseEvent.consume();
    }

    private KeyCode chooseDirection(double newX, double newY, double viewModelX, double viewModelY) {
        KeyCode keyCode = KeyCode.NUMPAD0;
        if (newX == viewModelX && newY > viewModelY) {
            keyCode = KeyCode.NUMPAD2;
        } else if (newX == viewModelX && newY < viewModelY) {
            keyCode = KeyCode.NUMPAD8;
        } else if (newX > viewModelX && newY == viewModelY) {
            keyCode = KeyCode.NUMPAD6;
        } else if (newX < viewModelX && newY == viewModelY) {
            keyCode = KeyCode.NUMPAD4;
        }
        return keyCode;
    }

    private double calculateMouse(int maxOfRowsOrCols, double sizeOfMaze, int length, double mouseMove, double dist) {
        double cellSize = sizeOfMaze / maxOfRowsOrCols;
        double start = (sizeOfMaze / 2 - (cellSize * length / 2)) / cellSize;
        double mouseCalc = (int) ((mouseMove) / (dist) - start);
        return mouseCalc;

    }

    public void exitProgram() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit Hansel and Gretel maze game ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (viewModel != null) {
                viewModel.exitProgram();
                System.exit(0);
            }
        } else {
            mazeDisplayer.drawMaze(viewModel.getMaze());
        }
    }

    public void openProp(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Properties");
        FXMLLoader propFXML = new FXMLLoader(getClass().getResource("/View/Properties.fxml"));
        Parent root = propFXML.load();
        PropertiesController propController = propFXML.getController();
        propController.setViewModel(viewModel);
        propController.setStage(stage);
        Scene scene = new Scene(root, 650, 250);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
/*
        scene.getStylesheets().addAll(this.getClass().getResource("prop.css").toExternalForm());
*/
        stage.show();
/*
        stage.showAndWait();
*/
        /*if(!stage.isShowing()) {
            viewModel.changeConfig();
        }*/

    }

    public void newMaze(ActionEvent actionEvent) {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int columns = Integer.valueOf(textField_mazeColumns.getText());
        if (rows >= 1 && columns >= 1) {
            viewModel.generateMaze(rows, columns);
        } else {
            invalidMazeSize();
        }
    }

    private void invalidMazeSize() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Hii ! You should insert positive numbers... ");
        alert.show();
    }
}

