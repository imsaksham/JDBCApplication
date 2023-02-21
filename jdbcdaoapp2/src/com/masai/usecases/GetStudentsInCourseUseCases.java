package com.masai.usecases;

import java.util.List;
import java.util.Scanner;

import com.masai.dao.CourseDao;
import com.masai.dao.CourseDaoImpl;
import com.masai.exception.CourseException;
import com.masai.model.StudentCourseDTO;

public class GetStudentsInCourseUseCases {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter course name to get details of Students: ");
		String cname = sc.next();
		
		
		CourseDao cdao = new CourseDaoImpl();
		
		try {
			List<StudentCourseDTO> dtos = cdao.getAllStudentsByCourseName(cname);
			
			dtos.forEach(dto -> System.out.println(dto));
		} catch (CourseException e) {
			System.out.println(e.getMessage());
		}
	}
}
