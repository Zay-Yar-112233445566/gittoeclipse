package com.studentmanagement.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentmanagement.dto.TeacherCourseStudentAssignmentsDto;
import com.studentmanagement.model.Assignments;
import com.studentmanagement.model.TeacherCourseStudentAssignments;
import com.studentmanagement.model.TeacherCourseStudents;
import com.studentmanagement.repository.TeacherCourseStudentAssignmentsRepository;

@Service
public class TeacherCourseStudentAssignmentsServiceImpl implements TeacherCourseStudentAssignmentsService {

	@Autowired 
	private TeacherCourseStudentAssignmentsRepository repo;
	
	@Autowired
	private AssignmentsService asmService;
	
	@Autowired
	private TeacherCourseStudentsService tcsService;

	@Override
	public List<TeacherCourseStudentAssignments> getAllByAssignmentsId(int id) {
		return repo.findAllByAssignmentsId(id);
	}

	@Override
	public TeacherCourseStudentAssignments save(TeacherCourseStudentAssignmentsDto dto) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = sdf.format(date);
		TeacherCourseStudentAssignments entity=new TeacherCourseStudentAssignments();
		entity.setAssignmentContent(dto.getAssignmentContent());
		entity.setAssignments(asmService.getById(dto.getAssignmentsId()));
		entity.setTeacherCourseStudents(tcsService.getById(dto.getTeacherCourseStudentsId()));
		entity.setSubmittedDate(formattedDate);
		entity.setStatus(1);
		return repo.save(entity);
		
	}

	@Override
	public TeacherCourseStudentAssignments getById(int id) {
		return repo.findById(id);
	}

	@Override
	public TeacherCourseStudentAssignments checked(TeacherCourseStudentAssignmentsDto dto, int id, int asmId,
			int tcsId) {
		
		TeacherCourseStudentAssignments entity=repo.findById(id);
		entity.setFeedback(dto.getFeedback());
		entity.setGrade(dto.getGrade());
		entity.setStatus(2);
		return repo.save(entity);
	}

	@Override
	public int countByAssignments(Assignments ass) {
		return repo.countByAssignments(ass);
	}

	@Override
	public TeacherCourseStudentAssignments getByTeacherCourseStudentsAndAssignments(TeacherCourseStudents tcs,
			Assignments ass) {
		return repo.getByTeacherCourseStudentsAndAssignments(tcs, ass);
	}
	
}
