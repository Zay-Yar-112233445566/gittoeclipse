package com.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.studentmanagement.dto.AssignmentsDto;
import com.studentmanagement.dto.TeacherCourseStudentAssignmentsDto;
import com.studentmanagement.dto.TeachersDto;
import com.studentmanagement.model.Admins;
import com.studentmanagement.model.Assignments;
import com.studentmanagement.model.TeacherCourseStudentAssignments;
import com.studentmanagement.model.TeacherCourseStudents;
import com.studentmanagement.model.TeacherCourses;
import com.studentmanagement.model.Teachers;
import com.studentmanagement.service.AssignmentsService;
import com.studentmanagement.service.TeacherCourseStudentAssignmentsService;
import com.studentmanagement.service.TeacherCourseStudentsService;
import com.studentmanagement.service.TeacherCoursesService;
import com.studentmanagement.service.TeachersService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
	
	@Autowired
	TeacherCoursesService teacherCoursesService;

	@Autowired
	private TeachersService teacherService;
	
	@Autowired
	TeacherCourseStudentsService teacherCourseStudentsService;
	
	@Autowired
	TeacherCourseStudentAssignmentsService teacherCourseStudentAssignmentsService;
	
	@Autowired
	AssignmentsService assignmentsService;
	
	@GetMapping("/signIn")
	public String showSignUpForm(Model model) {
		Teachers teachers = new Teachers();
		model.addAttribute("teachers", teachers);
		return "teacher/teacherLogin";
	}
	
	@PostMapping("/validate")
	public String validateUser(@ModelAttribute Teachers teachers,HttpSession session,Model model) {
		Teachers validateTeacher=teacherService.findByEmailAndPassword(teachers);
		if( validateTeacher != null) {
			session.setAttribute("teachersEmail", teachers.getEmail());
			session.setAttribute("teacherSession", validateTeacher);
			return "redirect:/teachers/dashboard";			
		}else {
			System.out.println("Not register user");
			return "teacher/teacherLogin";
		}
	}
	@GetMapping("/dashboard")
	public String showdashboard(Model model,HttpSession session) {
		Teachers teacher=(Teachers) session.getAttribute("teacherSession");
		List<TeacherCourses> teacherCoureses=teacherCoursesService.getByTeachers(teacher);
		model.addAttribute("teacherCourses",teacherCoureses);
		for(TeacherCourses tc:teacherCoureses) {
			System.out.println(tc.getName());
		}
		return "teacher/dashboard";
	}
	
	@GetMapping("/profile")
	public String profile(HttpSession session,Model model) {
		// Retrieve the user ID from the session
	    String email = (String) session.getAttribute("teachersEmail");

	    // Use the user ID to retrieve the user object from the database
	    Teachers teacher = teacherService.getByEmail(email);
	    
	    // Add the user object to the model
	    model.addAttribute("teacher", teacher);
	    return "teacher/profile";
	}
	
	@GetMapping("/teacherCourses")
	public String showTeacherCourses(Model model,HttpSession session) {
		Teachers t=(Teachers) session.getAttribute("teacherSession");
		model.addAttribute("teacherCourses",teacherCoursesService.getByTeachers(t));
		return "teacher/dashboard";
	}
	
	@GetMapping("/teacherCourses/viewDetail")
	public String viewDetail(@RequestParam int id, Model model) {
		TeacherCourses tc = teacherCoursesService.getById(id);
		model.addAttribute("teacherCourses", tc);
		model.addAttribute("assignments",assignmentsService.getAllByTeacherCourses(teacherCoursesService.getById(id)));
		model.addAttribute("teacherCourseStudents",teacherCourseStudentsService.findByTeacherCourses(tc));
		return "teacher/viewDetailTeacherCourseStudent";
	}
//	<a th:href="@{/teachers/teacherCourses/viewDetail(id=${teacherCourse.id})}" class="card radius-10 border-start border-0 border-4 border-info mb-4 d-flex align-items-stretch">
//		  
	@GetMapping("/teacherCourses/assignment/create")
	public String showAssignmentsCreate(Model model, @RequestParam int id ) {
		int teacherCoursesId=id;
		AssignmentsDto dto = new AssignmentsDto();
		dto.setTeacherCoursesId(teacherCoursesId);
		model.addAttribute("teacherCoursesId",teacherCoursesId);
		model.addAttribute("assignmentsDto", dto);
		return "teacher/assignmentsCreate";
	}
	
	@PostMapping("/teacherCourses/assignment/create")
	public String teacherCourseAssignmentsCreate( @ModelAttribute("assignmentsDto") AssignmentsDto dto,@RequestParam("id") String id) {
			dto.setTeacherCoursesId(Integer.parseInt(id));
	        assignmentsService.create(dto);
	        return "redirect:/teachers/teacherCourses/viewDetail?id="+id; 
	}
	
	@GetMapping("/teacherCourses/assignment/viewDetail")
	public String showAssignmentsDetail(Model model, @RequestParam int id ,@RequestParam ("tcId") int teacherCourseId) {
		int classStudentCount=teacherCourseStudentsService.countByTeacherCourses(teacherCoursesService.getById(teacherCourseId));
		int answeredStudentCount=teacherCourseStudentAssignmentsService.countByAssignments(assignmentsService.getById(id));
		System.out.println("classStudentCount "+classStudentCount);
		System.out.println("answeredStudentCount "+answeredStudentCount);
		
		Assignments assignments = assignmentsService.getById(id);
		List<TeacherCourseStudentAssignments> tcsaList=teacherCourseStudentAssignmentsService.getAllByAssignmentsId(id);
		model.addAttribute("assignments", assignments);
		model.addAttribute("tcsaList",tcsaList);
		model.addAttribute("classStudentCount",classStudentCount);
		model.addAttribute("answeredStudentCount",answeredStudentCount);
		return "teacher/assignmentDetails";
	}
	
	//<form method="post" th:action="@{/students/assignments/check(id=${tcsa.id},asmId=${assignments.id},tcsId=${tcs.id})}"
		@PostMapping("/assignments/check")
		public String assignmentCheck(@ModelAttribute("dto")TeacherCourseStudentAssignmentsDto dto,@RequestParam("id")int id,
				@RequestParam ("asmId") int asmId,@RequestParam ("tcsId") int tcsId,@RequestParam("tcId") int tcId) {
			teacherCourseStudentAssignmentsService.checked(dto,id,asmId,tcsId);
			return "redirect:/teachers/teacherCourses/assignment/viewDetail?id="+asmId+"&tcId="+tcId; 
		}
	//Teachers------------
	@GetMapping("/create")
	public String showTeacherCreate(Model model) {
		TeachersDto teacherDto = new TeachersDto();
		model.addAttribute("teacherDto",teacherDto);
		return "admin/teacherCreate";
	}
	
	@PostMapping("/create")
	public String createTeacher(@Valid  @ModelAttribute("teacherDto") TeachersDto teacherDto, BindingResult result) {
	    if (result.hasErrors()) {
	        return "admin/teacherCreate";
	    } else {
	        teacherService.create(teacherDto);
	        return "redirect:/teachers/create";
	    }
	}	
	@GetMapping({"/list","/cancel"})
	public String teacherList(Model model) {
		List<Teachers> teacher = teacherService.getAll();
		model.addAttribute("teachers",teacher);
		return "admin/teacherList";
	}
	
	@GetMapping("/edit")
	public String showEditTeacherPage(Model model,@RequestParam int id) {
		
		Teachers teacher = teacherService.getById(id);
		
		TeachersDto teacherDto = new TeachersDto();
		teacherDto.setName(teacher.getName());
		teacherDto.setQualification(teacher.getQualification());
		teacherDto.setPhone(teacher.getPhone());
		teacherDto.setEmail(teacher.getEmail());
		teacherDto.setAddress(teacher.getAddress());
		teacherDto.setServiceYear(teacher.getServiceYear());
		
		model.addAttribute("teachers",teacher);
		model.addAttribute("teacherDto",teacherDto);
		return "admin/editTeacher";
	}
	

	@PostMapping("/edit")
    public String editTeacher(Model model,@RequestParam int id,@Valid  @ModelAttribute("teacherDto") TeachersDto teacherDto, BindingResult result) {
		try {
			Teachers teacher = teacherService.getById(id);
			model.addAttribute("teachers",teacher);
			model.addAttribute("teacherDto",teacherDto);
			if (result.hasErrors()) {
	            return "admin/editTeacher";
	        }else {
				teacherService.edit(id, teacherDto);
				List<Teachers> teachers = teacherService.getAll();
				model.addAttribute("teachers",teachers);
		        return "admin/teacherList";
	        }
		}
		catch(Exception ex) {
			System.out.println("Exception: "+ex.getMessage());
			return "admin/teacherList";
		}
        
    }
	@GetMapping("/delete")
	public String deleteTeacher(@RequestParam int id,Model model) {
		teacherService.deleteByID(id);
		List<Teachers> teacher = teacherService.getAll();
		model.addAttribute("teachers",teacher);
		return "admin/teacherList";
	}
	
	@GetMapping("/viewDetail")
	public String viewDetailTeacher(@RequestParam int id,Model model) {
		Teachers teacher = teacherService.getById(id);
		model.addAttribute("teacher",teacher);
		return "admin/viewDetailTeacher";
	}
	
//	/teachers/assignment/check(id=${tcsa.id},asmid=${tcsa.assignments.id},tcsId=${tcsa.teacherCourseStudents.id})}" class="btn btn-primary btn-sm radius-30 px-4"	
	@GetMapping("/assignments/check")
	public String showAssignmentCheckForm(Model model,@RequestParam("id") int id,@RequestParam("asmId") int assignmentsId,@RequestParam("tcsId") int teacherCourseStudentsId) {
		Assignments assignments=assignmentsService.getById(assignmentsId);
		TeacherCourseStudents tcs=teacherCourseStudentsService.getById(teacherCourseStudentsId);
		TeacherCourseStudentAssignments tcsa=teacherCourseStudentAssignmentsService.getById(id);
		TeacherCourseStudentAssignmentsDto dto=new TeacherCourseStudentAssignmentsDto();
		
		model.addAttribute("dto",dto);
		model.addAttribute("assignments",assignments);
		model.addAttribute("tcs",tcs);
		model.addAttribute("tcsa",tcsa);
		
		return "teacher/assignmentsCheck";
	}
	
	
}
