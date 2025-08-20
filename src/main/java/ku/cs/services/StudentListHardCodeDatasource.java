package ku.cs.services;

import ku.cs.models.StudentList;

public class StudentListHardCodeDatasource implements Datasource<StudentList>{
    @Override
    public StudentList readData() {
        StudentList list = new StudentList();
        list.addNewStudent("6710400001", "First");
        list.addNewStudent("6710400002", "Second");
        list.addNewStudent("6710400003", "Third");
        list.addNewStudent("6710400004", "Fourth");
        list.addNewStudent("6710400005", "Fifth");
        list.addNewStudent("6710400006", "Sixth");
        list.addNewStudent("6710400007", "Seventh");
        list.addNewStudent("6710400008", "Eighth");
        list.addNewStudent("6710400009", "Ninth");
        list.addNewStudent("67104000010", "Tenth");
        return list;
    }

    @Override
    public void writeData(StudentList data) {

    }
}
