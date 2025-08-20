package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;

public class StudentScoreController {
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label scoreLabel;
    @FXML private Label gradeLabel;
    @FXML private Label errorLabel;
    @FXML private TextField scoreTextField;

    private StudentList studentList;
    private Datasource<StudentList> datasource;
    private Student student;

    @FXML
    public void initialize()
    {
        datasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = datasource.readData();

        // เอาข้อมูล studentId ที่ส่งมาจากหน้าอื่น
        String studentId = (String) FXRouter.getData();

        student = studentList.findStudentById(studentId);
        showStudent(student);
        errorLabel.setText("");
    }

    private void showStudent(Student student)
    {
        idLabel.setText(student.getId());
        nameLabel.setText(student.getName());
        scoreLabel.setText(String.format("%.2f", student.getScore()));
        gradeLabel.setText(student.getGrade());
    }

    @FXML
    public void handleBackToStudentsTableButton() {
        try {
            FXRouter.goTo("students-table");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleBackToStudentGridButton() {
        try {
            FXRouter.goTo("student-grid-view");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleGiveScoreButton() {
        String scoreString = scoreTextField.getText();
        if (scoreString.trim().equals("")) {
            errorLabel.setText("score is required");
            return;
        }

        try {
            double score = Double.parseDouble(scoreString);
            if (score < 0) {
                errorLabel.setText("score must be a positive number");
                return;
            }
            studentList.giveScoreToId(student.getId(), score);

            errorLabel.setText("");
            scoreTextField.clear();
            datasource.writeData(studentList);
            showStudent(student);

        } catch (NumberFormatException e) {
            errorLabel.setText("score must be a number");
        }
    }

}
