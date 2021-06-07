package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ZoomEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        Scene primeScene = new Scene(root, 750, 550);
        primaryStage.setScene(primeScene);


        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController(); // TODO; need to change to IView
        view.setViewModel(viewModel);
        view.mediaPlayerMusic.setAutoPlay(true);
        view.mediaPlayerMusic.seek(view.mediaPlayerMusic.getStartTime()); //TODO: make sure the music repeat

        view.setResizeEvent(primeScene);
        primaryStage.setResizable(true);

        primaryStage.show();


    }
    public static void main(String[] args) {
        launch(args);
    }
}
