package ku.cs.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentListTest {

    @Test
    @DisplayName("ทดสอบ method addNewStudent ด้วยการ findStudentById เพื่อดูว่ามีนักเรียนคนนั้นเพิ่มเข้าแล้ว")
    void testAddNewStudent() {
        StudentList students = new StudentList();
        students.addNewStudent("6710405214","Nampueng",90);

        assertEquals("Nampueng",students.findStudentById("6710405214").getName());
        assertEquals("6710405214",students.findStudentById("6710405214").getId());
        assertEquals(90,students.findStudentById("6710405214").getScore());

    }

    @Test
    @DisplayName("ทดสอบ method findStudentById ")
    void testFindStudentById() {
        StudentList students = new StudentList();
        students.addNewStudent("6710405214","Preampassorn",85);
        assertEquals("6710405214",students.findStudentById("6710405214").getId());
    }

    @Test
    @DisplayName("ทดสอบ method filterByName")
    void testFilterByName() {
        StudentList students = new StudentList();
        students.addNewStudent("6710405214","Namfon",88);
        StudentList filteredList = new StudentList();
        filteredList = students.filterByName("fon");
        assertEquals("Namfon",filteredList.findStudentById("6710405214").getName());

    }

    @Test
    @DisplayName("ทดสอบ method giveScoreToId")
    void testGiveScoreToId() {
        StudentList students = new StudentList();
        students.addNewStudent("6710405214","Apple",77);
        students.giveScoreToId("6710405214",3);
        assertEquals(80,students.findStudentById("6710405214").getScore());
    }

    @Test
    @DisplayName("ทดสอบ method viewGradeOfId")
    void testViewGradeOfId() {
        StudentList students = new StudentList();
        students.addNewStudent("6710405214","Paiboon",80);
        assertEquals("A",students.viewGradeOfId("6710405214"));
    }
}