package com.studentmanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeacherCourseStudentAssignmentsDto {

	@NotNull(message = "Submitted date cannot be null")
	private String submittedDate;

	private int grade;

	@Size(max = 255, message = "Feedback must be less than {max} characters")
	private String feedback;

	private int status;

	@Size(max = 1000, message = "Assignment content must be less than {max} characters")
	private String assignmentContent;

	private int assignmentsId;

	@NotNull(message = "Teacher course students ID cannot be null")
	private int teacherCourseStudentsId;

	public String getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(String submittedDate) {
		this.submittedDate = submittedDate;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAssignmentContent() {
		return assignmentContent;
	}

	public void setAssignmentContent(String assignmentContent) {
		this.assignmentContent = assignmentContent;
	}

	public int getAssignmentsId() {
		return assignmentsId;
	}

	public void setAssignmentsId(int assignmentsId) {
		this.assignmentsId = assignmentsId;
	}

	public int getTeacherCourseStudentsId() {
		return teacherCourseStudentsId;
	}

	public void setTeacherCourseStudentsId(int teacherCourseStudentsId) {
		this.teacherCourseStudentsId = teacherCourseStudentsId;
	}

}
