package com.test.shiv.jta.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentWithCoursesDTO {

    private Long studentId;
    private String fullName;
    private List<String> courseNames;
}
