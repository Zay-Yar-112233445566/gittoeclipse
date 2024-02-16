package com.studentmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentmanagement.model.Admins;
import com.studentmanagement.model.Assignments;
import com.studentmanagement.model.Students;
import com.studentmanagement.model.TeacherCourseStudentAssignments;
import com.studentmanagement.model.TeacherCourseStudents;
import com.studentmanagement.model.TeacherCourses;

@Repository
public interface TeacherCourseStudentAssignmentsRepository extends JpaRepository<TeacherCourseStudentAssignments, Integer> {

	List <TeacherCourseStudentAssignments> findAllByAssignmentsId(int id);
	TeacherCourseStudentAssignments findById(int id);
	int countByAssignments(Assignments ass);
	TeacherCourseStudentAssignments getByTeacherCourseStudentsAndAssignments(TeacherCourseStudents tcs, Assignments ass);
}
