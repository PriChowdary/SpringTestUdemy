package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.swing.text.html.Option;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    StudentAndGradeService studentService;
    @Autowired
    StudentDao studentDao;

    @BeforeEach
    public void setUpDatabase(){
  /*      jdbcTemplate.execute("Insert into student (id, firstName, lastName, email_address) " +
                "values (3,'Eric3','Roy3','roy@gmail.com')");
  */  }

    @Test
    public void createStudentService(){
        jdbcTemplate.execute("Insert into student (id, firstName, lastName, email_address) " +
                "values (3,'Eric3','Roy3','roy@gmail.com')");
        studentService.createStudent("Chad","Darby","chad@gmail.com");
        CollegeStudent student= studentDao.findByEmailAddress("chad@gmail.com");
        assertEquals("chad@gmail.com",student.getEmailAddress(),"find by Email");
    }

    @Test
    public void isStudentNullCheck(){
        assertTrue(studentService.checkIfStudentIsNull(3));
        assertTrue(studentService.checkIfStudentIsNull(1));
    }

//    @AfterEach
//    public void setUpAfterTranscation(){
//        jdbcTemplate.execute("delete from student");
//    }

    @Test
    @Disabled
    public void deleteStudentService(){
        Optional<CollegeStudent> deletedStudent = studentDao.findById(1);
        assertTrue(deletedStudent.isPresent(),"Student found");
        studentService.deleteStudent(1);
        deletedStudent = studentDao.findById(1);
        assertFalse(deletedStudent.isPresent(),"Student not found");
        }

    @Test
    @Sql("/insertData.sql")
    @Disabled
    public void getGradeBookService(){
        Iterable<CollegeStudent> collegeStudentIterable= studentService.getGradeBook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent collegeStudent:collegeStudentIterable){
            collegeStudents.add(collegeStudent);
        }
        assertEquals(5,collegeStudents.size());
        }
}
