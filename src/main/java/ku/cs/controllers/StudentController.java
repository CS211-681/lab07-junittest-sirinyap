package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.models.Student;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label scoreLabel;

    @FXML
    public void initialize() {
        Student student = new Student("6710401234", "Tony Stark");
        student.addScore(60);

        showStudent(student);
    }

    public void showStudent(Student student) {
        nameLabel.setText(student.getName());
        scoreLabel.setText(String.format("%.2f", student.getScore()));
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
