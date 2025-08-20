package ku.cs.labstudent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXComponent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        configRoute();
        registerComponents();

        FXRouter.bind(this, stage, "Hello World");
        FXRouter.goTo("student-grid-view");
    }

    private static void configRoute()
    {
        String viewPath = "ku/cs/views/";
        FXRouter.when("hello", viewPath + "hello-view.fxml");
        FXRouter.when("student-profile", viewPath + "student.fxml");
        FXRouter.when("student-list", viewPath + "student-list.fxml");
        FXRouter.when("students-table", viewPath + "students-table.fxml");
        FXRouter.when("student-score", viewPath + "student-score.fxml");
        FXRouter.when("student-grid-view", viewPath + "student-grid-view.fxml");
    }

    private static void registerComponents() {
        String componentPath = "ku/cs/components/";
        FXComponent.register("student-component", componentPath + "student-component.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}