package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
public class GradeBookControllerTest {
    @Autowired
    private static MockHttpServletRequest request;

    @BeforeAll
    public static void setup(){
        request = new MockHttpServletRequest();
        request.setParameter("firstName","Chad");
        request.setParameter("lastName","Darby");
        request.setParameter("emailAddress","chad@gmail.com");
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private StudentAndGradeService studentAndGradeServiceMock;

    @BeforeEach
    public void beforeEach(){
        jdbcTemplate.execute("Insert into student (id, firstName, lastName, email_address) " +
                "values (1,'Eric','Roy','eric@gmail.com')");
    }
    @Test
    public void getStudentsHttpRequest()throws Exception{
        CollegeStudent student1= new GradebookCollegeStudent(
                "Eric","Roby","eric@gmail.com");
        CollegeStudent student2= new GradebookCollegeStudent(
                "Chad","Darby","chad@gmail.com");

        List<CollegeStudent> collegeStudentList= new ArrayList<>(Arrays.asList(student1,student2));
        when(studentAndGradeServiceMock.getGradeBook()).thenReturn(collegeStudentList);
        assertIterableEquals(collegeStudentList,studentAndGradeServiceMock.getGradeBook());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");
    }

    @Test
    public void createStudentHttpRequest() throws Exception{
        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstName",request.getParameterValues("firstName"))
                .param("lastName",request.getParameterValues("lastName"))
                .param("emailAddress",request.getParameterValues("lastName")))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav,"index");
    }

    @AfterEach
    public void setUpAfterTransaction(){
        jdbcTemplate.execute("delete from student");
    }
}
