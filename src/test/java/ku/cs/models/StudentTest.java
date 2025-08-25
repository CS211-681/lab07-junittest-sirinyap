package ku.cs.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test
    @DisplayName("ทดสอบการเพิ่มคะแนน 45.15 คะแนน")
    void testAddScore(){
        Student s = new Student("6xxxxxxxx", "Tanjiro");
        s.addScore(45.5);
        assertEquals(45.5,s.getScore());
    }

    @Test
    @DisplayName("ทดสอบการเพิ่มคะแนน 85 คะแนน และให้ Object คำนวนเกรดออกมา")
    void testCalculateScore(){
        Student s = new Student("6xxxxxxxxx", "Bakugo");
        s.addScore(85);
        assertEquals("A", s.grade());
    }

    @Test
    @DisplayName("ทดสอบการเปลี่ยนชื่อ midoriya")
    void testChangeName(){
        Student s = new Student("6xxxxxxxx", "wow");
        s.changeName("midoriya");
        assertEquals("midoriya", s.getName());
    }

    @Test
    @DisplayName("ทดสอบ method isId")
    void testIsId(){
        Student s = new Student("6710405214", "Naruto");
        assertEquals(true,s.isId("6710405214"));
    }

    @Test
    @DisplayName("ทดสอบ method isNameContain")
    void testIsNameContain(){
        Student s = new Student("6xxxxxxxxx", "Paiboon");
        assertEquals(true,s.isNameContains("Paiboon"));
    }

    @Test
    @DisplayName("ทดสอบ method getId")
    void testGetId(){
        Student s = new Student("6710405214", "Sutep");
        assertEquals("6710405214",s.getId());
    }

    @Test
    @DisplayName("ทดสอบ method getName")
    void testGetName(){
        Student s = new Student("6710405214", "Nampueng");
        assertEquals("Nampueng",s.getName());

    }

    @Test
    @DisplayName("ทดสอบ method getScore")
    void testGetScore(){
        Student s = new Student("6710405214", "Ram",90);
        assertEquals(90,s.getScore());
    }

    @Test
    @DisplayName("ทดสอบ method getGrade")
    void testGetGrade(){
        Student s = new Student("6710405214", "Namfon",90);
        assertEquals("A",s.getGrade());

    }

}