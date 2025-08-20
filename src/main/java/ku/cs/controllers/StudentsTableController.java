package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;

public class StudentsTableController {
    @FXML private TableView<Student> studentsTableView;

    private StudentList studentList;
    private Datasource<StudentList> datasource;

    @FXML
    public void initialize(){
        datasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = datasource.readData();
        showTable(studentList);

        studentsTableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Student>() {

                    @Override
                    public void changed(ObservableValue<? extends Student> observableValue, Student oldValue, Student newValue) {
                        if (newValue != null){
                            try {
                                FXRouter.goTo("student-score", newValue.getId());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );
    }

    private void showTable(StudentList studentList){
        // เตรียมคอลัมน์
        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // student.getId()

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // student.getName()

        TableColumn<Student, Double> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score")); // student.getScore()

        TableColumn<Student, String> gradeColumn = new TableColumn<>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade")); // student.getGrade()

        // เพิ่ม column ไปใน table view
        studentsTableView.getColumns().clear();
        studentsTableView.getColumns().add(idColumn);
        studentsTableView.getColumns().add(nameColumn);
        studentsTableView.getColumns().add(scoreColumn);
        studentsTableView.getColumns().add(gradeColumn);

        // เพิ่มแถวข้อมูล
        studentsTableView.getItems().clear();
        studentsTableView.getItems().addAll(studentList.getStudents());
    }
}
