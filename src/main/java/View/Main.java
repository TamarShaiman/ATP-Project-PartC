package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resources/MyView.fxml"));
/*        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("src/main/resources/MyView.fxml"));
        Parent root = fxmlLoader.load();*/
        FXMLLoader fxmlLoader = new FXMLLoader(new File("src/main/resources/MyView.fxml").toURI().toURL());
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hansel and Gretel Maze Game");
        Scene primeScene = new  Scene(root, 800, 600);
        primaryStage.setScene(primeScene);

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        View.MyViewController view = fxmlLoader.getController(); // TODO; need to change to IView
        view.setViewModel(viewModel);
        view.mediaPlayerMusic.setAutoPlay(true);

        view.setResizeEvent(primeScene);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                view.exitProgram();
            }
        });



        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
