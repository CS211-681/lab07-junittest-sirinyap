package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.StudentHardCodeDatasource;
import ku.cs.services.StudentListFileDatasource;
import ku.cs.services.StudentListHardCodeDatasource;

public class StudentListController {
    @FXML private ListView<Student> studentListView;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Label scoreLabel;

    @FXML private Label errorLabel;
    @FXML private TextField giveScoreTextField;

    private StudentList studentList;
    private Student selectedStudent;

    private Datasource<StudentList> datasource;

    @FXML
    public void initialize(){
        errorLabel.setText("");
        clearStudentInfo();

//        datasource = new StudentListHardCodeDatasource();
        datasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = datasource.readData();  // polymorph
        showList(studentList);

        studentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student oldValue, Student newValue) {
                if (newValue != null) {
                    selectedStudent = newValue;
                    showStudentInfo(selectedStudent);
                } else {
                    selectedStudent = null;
                    clearStudentInfo();
                }
            }
        });
    }

    private void showList(StudentList studentList) {
        studentListView.getItems().clear();
        studentListView.getItems().addAll(studentList.getStudents());
    }

    private void showStudentInfo(Student student) {
        idLabel.setText(student.getId());
        nameLabel.setText(student.getName());
        scoreLabel.setText(String.format("%.2f", student.getScore()));
    }

    private void clearStudentInfo() {
        idLabel.setText("");
        nameLabel.setText("");
        scoreLabel.setText("");
    }

    @FXML
    public void onGiveScoreButtonClick() {
        if (selectedStudent != null) {
            String scoreText = giveScoreTextField.getText();
            String errorMessage = "";
            try {
                double score = Double.parseDouble(scoreText);
                studentList.giveScoreToId(selectedStudent.getId(), score);
                showStudentInfo(selectedStudent);
                datasource.writeData(studentList);
            } catch (NumberFormatException e) {
                errorMessage = "Please insert number value";
                errorLabel.setText(errorMessage);
            } finally {
                if (errorMessage.equals("")) {
                    giveScoreTextField.setText("");
                }
            }
        } else {
            giveScoreTextField.setText("");
            errorLabel.setText("");
        }
    }
}
