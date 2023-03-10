package com.masai.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.masai.exception.CourseException;
import com.masai.exception.StudentException;
import com.masai.model.Student;
import com.masai.utility.DBUtil;

public class StudentDaoImpl implements StudentDao {

	@Override
	public String registerStudent(Student student) throws StudentException {
		String result = "Not Inserted....";
		
		try(Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps= conn.prepareStatement("insert into student(name, address, email, password) values(?, ?, ?, ?)");
			
			ps.setString(1, student.getName());
			ps.setString(2, student.getAddress());
			ps.setString(3, student.getEmail());
			ps.setString(4, student.getPassword());
			
			int x = ps.executeUpdate();
			
			if (x > 0) {
				result = "Student Registered Successfully....";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentException(e.getMessage());
		}
		
		return result;
	}

	@Override
	public Student signIn(String username, String password) throws StudentException {
		Student student = null;
		
		try (Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps = conn.prepareStatement("select * from student where email = ? AND password = ?");
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				int r = rs.getInt("roll");
				String n = rs.getString("name");
				String a = rs.getString("address");
				String e = rs.getString("email");
				String p = rs.getString("password");
				
				student = new Student(r, n, a, e, p);
				
				// OR
//				student = new Student(rs.getInt("roll"), rs.getString("name"), rs.getString("address"), rs.getString("email"), rs.getString("password"));
			}
			else {
				throw new StudentException("Invalid Username or Password");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StudentException(e.getMessage());
		}
		
		return student;
	}

	@Override
	public String enrollStudentInCourse(int roll, int cid) throws StudentException, CourseException {
		String message = "Not Enrolled....!";
		
		try (Connection conn = DBUtil.provideConnection()) {
			PreparedStatement ps1 = conn.prepareStatement("select * from student where roll = ?");
			
			ps1.setInt(1, roll);
			
			ResultSet rs1 = ps1.executeQuery();
			
			if (rs1.next()) {
				PreparedStatement ps2 = conn.prepareStatement("select * from course where cid = ?");
				
				ps2.setInt(1, cid);
				
				ResultSet rs2 = ps2.executeQuery();
				
				if (rs2.next()) {
					PreparedStatement ps3 = conn.prepareStatement("insert into student_course values(?, ?)");
					
					ps3.setInt(1, cid);
					ps3.setInt(2, roll);
					
					int x = ps3.executeUpdate();
					
					if (x > 0) {
						message = "Student Enrolled Successfully....!";
					}
				}
				else {
					throw new CourseException("Invalid Course Id "+cid);
				}
			}
			else {
				throw new StudentException("Student does not found with roll "+roll);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
			throw new CourseException(e.getMessage());
		}
		
		return message;
	}
	
}
