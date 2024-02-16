package com.studentmanagement.controller;

import java.net.http.HttpRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studentmanagement.model.Assignments;
import com.studentmanagement.model.Students;
import com.studentmanagement.model.TeacherCourseStudentAssignments;
import com.studentmanagement.model.TeacherCourseStudents;
import com.studentmanagement.model.Teachers;
import com.studentmanagement.service.AssignmentsService;
import com.studentmanagement.service.StudentsService;
import com.studentmanagement.service.TeacherCourseStudentAssignmentsService;
import com.studentmanagement.service.TeacherCourseStudentsService;
import com.studentmanagement.service.TeacherCoursesService;
import com.studentmanagement.dto.TeacherCourseStudentAssignmentsDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/students")
public class StudentsUserController {
	@Autowired
	TeacherCourseStudentsService teacherCourseStudentsService;
	
	@Autowired
	StudentsService  studentsService;
	
	@Autowired
	AssignmentsService assigmentsService;
	
	@Autowired
	TeacherCoursesService teacherCoursesService;
	
	@Autowired
	TeacherCourseStudentAssignmentsService teacherCourseStudentAssignmentsService;
	
	@GetMapping("/users")
	public String showStudentDashBoard(Model model,HttpSession session) {
		Students stu=(Students)session.getAttribute("studentSession");
		List<TeacherCourseStudents> tcs=teacherCourseStudentsService.findAllByStudents(studentsService.getById(stu.getId()));
		model.addAttribute("teacherCourseStudets",tcs);
		return "student/dashboard";
	}	
	@GetMapping("/signIn")
	public String showSignUpForm(Model model) {
		Students students = new Students();
		model.addAttribute("students", students);
		return "student/studentLogin";
	}
	
	@PostMapping("/validate")
	public String validateUser(@ModelAttribute Students students,HttpSession session) {
		Students validateStudent=studentsService.findByEmailAndPassword(students);
		if( validateStudent != null) {
			session.setAttribute("studentSession", validateStudent);
			session.setAttribute("studentsEmail", students.getEmail());
			return "redirect:/students/users";			
		}else {
			System.out.println("Not register user");
			return "student/studentLogin";
		}
	}
	
	@GetMapping("/profile")
	public String profile(HttpSession session,Model model) {
		// Retrieve the user ID from the session
	    String email = (String) session.getAttribute("studentsEmail");

	    // Use the user ID to retrieve the user object from the database
	    Students student = studentsService.getByEmail(email);
	    
	    // Add the user object to the model
	    model.addAttribute("student", student);
	    return "student/profile";
	}
	
	@GetMapping("/courses/viewDetail")
	//students/courses/viewDetail(id=${teacherCourseStudet.teacherCourses.id})
	public String showTeacherCourseStudentsDetail(Model model,@RequestParam int id,@RequestParam("tcsId") int tcsId) {
		List<Assignments> assignments=assigmentsService.getAllByTeacherCourses(teacherCoursesService.getById(id));
		TeacherCourseStudents tcs=teacherCourseStudentsService.getById(tcsId);
		//TeacherCourseStudentAssignments tcsa=teacherCourseStudentAssignmentsService.getByAssignmentsAndTeacherCourseStudents(tcsId)
		
		model.addAttribute("teacherCourseStudents",tcs);
		model.addAttribute("assignments",assignments);
		
		return "student/assignmentsList";
	}	

	@GetMapping("/assignments/answer")
	public String showAssignmentAnswerForm(Model model, @RequestParam("id") int assignmentId,
			@RequestParam("tcsid") int teacherCourseStudentId,@RequestParam("tcId") int tcId) {
		Assignments assignments=assigmentsService.getById(assignmentId);
		TeacherCourseStudents tcs=teacherCourseStudentsService.getById(teacherCourseStudentId);
		TeacherCourseStudentAssignments tcsa=teacherCourseStudentAssignmentsService.getByTeacherCourseStudentsAndAssignments(tcs, assignments);
		TeacherCourseStudentAssignmentsDto dto=new TeacherCourseStudentAssignmentsDto();
		model.addAttribute("assignments",assignments);
		model.addAttribute("teacherCourseStudentId",teacherCourseStudentId);
		model.addAttribute("tcId",tcId);
		model.addAttribute("dto",dto);
		model.addAttribute("tcsa",tcsa);
		return "student/assignmentAnswer";
	}
	
	@PostMapping("/assignments/answer")
	public String submitAnswerForm(@ModelAttribute TeacherCourseStudentAssignmentsDto dto,@RequestParam("assignmentId") int assignmentId,
			@RequestParam("teacherCourseStudentId") int tcsId,@RequestParam("tcId") int tcId) {
		dto.setAssignmentsId(assignmentId);
		dto.setTeacherCourseStudentsId(tcsId);
		teacherCourseStudentAssignmentsService.save(dto);
		return "redirect:/students/courses/viewDetail?id="+tcId+"&tcsId="+tcsId;
	}
	
}
