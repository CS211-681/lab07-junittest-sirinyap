package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.FXComponent;
import ku.cs.services.StudentListFileDatasource;

public class StudentGridViewController {
    @FXML private ScrollPane studentListScrollPane;
    @FXML private TextField nameFiltererTextField;

    private Datasource<StudentList> datasource;
    private StudentList studentList;

    @FXML
    public void initialize(){
        datasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = datasource.readData();

        showGrid(studentList);
    }

    private void showGrid(StudentList studentList){
        try {
            VBox vBox = new VBox();
            vBox.setSpacing(10);

            for (Student student : studentList.getStudents()) {
                FXComponent.addTo(vBox, "student-component", student);
            }

            studentListScrollPane.setContent(vBox);

            studentListScrollPane.setFitToWidth(true);
            studentListScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            studentListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchButton() {
        String search = nameFiltererTextField.getText();
        if (search.isBlank() || search.isEmpty()) {
            showGrid(studentList);
        } else {
            StudentList filteredList = studentList.filterByName(search);
            showGrid(filteredList);
        }
    }
}
