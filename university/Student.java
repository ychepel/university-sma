package university;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
	private final Integer NO_MARK_VALUE = -1;
	private final Integer LAST_EDUCATION_MONTH = 5;
	private final Integer DEFAULT_EDUCATION_DURATION = 5;
	
	private static Long studentCount = 0L; 
	
	private Boolean governmentFinanced;
	private Calendar entranceDate;
	private Calendar completionDate;
	private Long studentId;
	private String schoolGraduateSertificate;
	private StudentGroup studentGroup;
	private Map<Course, Integer> marks = new HashMap<>();
	
	public Student() {
		super();
		studentCount++;
		this.studentId = studentCount;
	}
	
	public Integer getAverageMark() {
		Map<Course, Integer> marks = getMarks();
		
		if (marks.size() == 0) return NO_MARK_VALUE;
	
		Integer markSum = 0;
		Integer markCount = 0;
		for(Map.Entry<Course, Integer> entry : marks.entrySet()) {
			if(entry.getValue() == NO_MARK_VALUE) continue;
			markSum += entry.getValue();
			markCount++; 
		}
		if(markCount == 0) {
			return NO_MARK_VALUE;
		}
		return markSum / markCount;
	}
	
	public Integer getGrade() {
		Integer completionYear = getCompletionDate().get(Calendar.YEAR);
		Integer currentYear = getCalendar().get(Calendar.YEAR);
		Integer currentMonth = getCalendar().get(Calendar.MONTH);
		if(completionYear < currentYear) return 0;
		
		Integer grade = DEFAULT_EDUCATION_DURATION - (completionYear - currentYear) + 1;
		if(currentMonth < LAST_EDUCATION_MONTH) grade--;
		
		return grade;
	}
	
	protected Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	public void setEntranceDate(Calendar entranceDate) {
		this.entranceDate = entranceDate;
		
		Integer completionYear = entranceDate.get(Calendar.YEAR) + DEFAULT_EDUCATION_DURATION;
		Calendar endDate = new GregorianCalendar(completionYear, LAST_EDUCATION_MONTH, 1);
		endDate.add(Calendar.DATE, -1);
		this.completionDate = endDate;
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
		if(studentGroup != null) {
			studentGroup.addStudent(this);
		}
	}
	
	public StudentGroup getStudentGroup() {
		return studentGroup;
	}
	
	public Integer getMark(Course course) {
		return marks.get(course);
	}
	
	public Long getStudentId() {
		return studentId;
	}

	public Boolean getGovernmentFinanced() {
		return governmentFinanced;
	}

	public void setGovernmentFinanced(Boolean governmentFinanced) {
		this.governmentFinanced = governmentFinanced;
	}

	public String getSchoolGraduateSertificate() {
		return schoolGraduateSertificate;
	}

	public void setSchoolGraduateSertificate(String schoolGraduateSertificate) {
		this.schoolGraduateSertificate = schoolGraduateSertificate;
	}

	public Calendar getEntranceDate() {
		return entranceDate;
	}
	
	

}
