package ku.cs.services;

import ku.cs.models.StudentList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentHardCodeDatasourceTest {
    @Test
    @DisplayName("ทดสอบ method readData")
    void testReadData(){
        StudentHardCodeDatasource data = new StudentHardCodeDatasource();
        StudentList list =data.readData();
        assertEquals("First",list.findStudentById("6710400001").getName());

    }
}