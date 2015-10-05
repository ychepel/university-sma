package university;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
	private final Integer LENGTH_OF_APPRENTICESHIP = 5;
	
	private Boolean governmentFinanced;
	private Calendar entranceDate;
	private Calendar completionDate;
	private Long studentId;
	private String schoolGraduateSertificate;
	private StudentGroup studentGroup;
	private Map<Course, Integer> marks = new HashMap<>();
	
	public Integer getAverageMark() {
		Map<Course, Integer> marks = getMarks();
		
		if (marks.size() == 0) return 0;
	
		Integer markSum = 0;
		Integer markCount = 0;
		for(Map.Entry<Course, Integer> entry : marks.entrySet()) {
			if(entry.getValue() == -1) continue;
			markSum += entry.getValue();
			markCount++; 
		}
		if(markCount == 0) {
			return -1;
		}
		return markSum / markCount;
	}
	
	public Integer getGrade() {
		Integer completionYear = getCompletionDate().get(Calendar.YEAR);
		Integer currentYear = getCalendar().get(Calendar.YEAR);
		Integer currentMonth = getCalendar().get(Calendar.MONTH) + 1;
		if(completionYear < currentYear) return 0;
		
		Integer grade = LENGTH_OF_APPRENTICESHIP - (completionYear - currentYear) + 1;
		if(currentMonth <= 5) grade--;
		
		return grade;
	}
	
	protected Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	public void addMark(Course course, Integer mark) {
		this.marks.put(course, mark);
	}
	
	public Map<Course, Integer> getMarks() {
		return marks;
	}
	
	public Calendar getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Calendar completionDate) {
		this.completionDate = completionDate;
	}
	
	public void setStudentGroup(StudentGroup studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	public StudentGroup getStudentGroup() {
		return studentGroup;
	}
}
