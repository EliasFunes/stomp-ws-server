package com.qrSignInServer.controllers;

import com.qrSignInServer.models.Student;
import com.qrSignInServer.services.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/test")
public class SimpleController {

    @Autowired
    SimpleService simpleService;

//    Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping(value = "/student/{studentId}")
    public @ResponseBody
    Student getTestData(@PathVariable Integer studentId) {
        Student student = new Student();
        student.setName("Peter");
        student.setId(studentId);
        return student;
    }

    @GetMapping(value = "/jdbc_test")
    public @ResponseBody String jdbcTest() {
        return simpleService.getData();
    }
}
