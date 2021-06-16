package View;

import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Observer, Initializable{

    @FXML
    public Stage stage;
    public ChoiceBox<String> searchingAlgorithm;

    public RadioButton EmptyButton;
    public RadioButton SimpleButton;
    public RadioButton MyButton;
    public Slider musicSlider;
    public Slider soundSlider;

    public String mazeGenerator = null;
    public Pane paneProp;
    public TextField threadPoolSize;
    private MyViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        EmptyButton.setToggleGroup(group);
        SimpleButton.setToggleGroup(group);
        MyButton.setToggleGroup(group);
        MyButton.setSelected(true);
        searchingAlgorithm.getItems().addAll("BreadthFirstSearch", "DepthFirstSearch", "BestFirstSearch");

        try {
            Properties prop =  new Properties();
            prop.load(new FileInputStream("./resources/config.properties"));
            threadPoolSize.setText(prop.getProperty("threadPoolSize"));
            searchingAlgorithm.setValue(prop.getProperty("mazeSearchingAlgorithm"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void setSearchAlgorithm(String searchAlgorithm) {
        if(searchAlgorithm.equals("BestFirstSearch")){
            searchingAlgorithm.setValue("BestFirstSearch");}
        else if(searchAlgorithm.equals("DepthFirstSearch")){
            searchingAlgorithm.setValue("DepthFirstSearch");}
        else if(searchAlgorithm.equals("BreadthFirstSearch")){
            searchingAlgorithm.setValue("BreadthFirstSearch");}
    }

    private void setMazeGenerator() {
        if(EmptyButton.isSelected()){mazeGenerator = "EmptyMazeGenerator";}
        else if(SimpleButton.isSelected()){ mazeGenerator = "SimpleMazeGenerator";}
        else if(MyButton.isSelected()){mazeGenerator = "MyMazeGenerator";}
    }

    public MyViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver(this);}


    public void Save() throws IOException {
        setMazeGenerator();
        String searchingAlgorithmStr = searchingAlgorithm.getValue();
        String poolSize = threadPoolSize.getText().toString();
        viewModel.changeProperties(mazeGenerator,searchingAlgorithmStr,poolSize);

    }
    public Stage getStage() {
        return stage;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("updated");
    }
}