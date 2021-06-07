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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        Scene primeScene = new  Scene(root, 1000, 600);
        primaryStage.setScene(primeScene);

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController(); // TODO; need to change to IView
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
