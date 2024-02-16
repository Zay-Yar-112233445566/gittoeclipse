package com.studentmanagement.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.studentmanagement.dto.TeacherCourseStudentAssignmentsDto;
import com.studentmanagement.model.Assignments;
import com.studentmanagement.model.TeacherCourseStudentAssignments;
import com.studentmanagement.model.TeacherCourseStudents;

@Service
public interface TeacherCourseStudentAssignmentsService {
	List<TeacherCourseStudentAssignments> getAllByAssignmentsId(int id);
	TeacherCourseStudentAssignments save(TeacherCourseStudentAssignmentsDto dto);
	TeacherCourseStudentAssignments getById(int id);
	TeacherCourseStudentAssignments checked(TeacherCourseStudentAssignmentsDto dto, int id,int asmId,int tcsId );
	int countByAssignments(Assignments ass);
	TeacherCourseStudentAssignments getByTeacherCourseStudentsAndAssignments(TeacherCourseStudents tcs, Assignments ass);
}
