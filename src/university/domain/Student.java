package university.domain;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import university.dao.DaoException;
import university.dao.StudentDao;

public class Student extends Person {
	private final Integer NO_MARK_VALUE = -1;
	private final Integer LAST_EDUCATION_MONTH = 5;
	private final Integer DEFAULT_EDUCATION_DURATION = 5;
	
	private static Long studentCount = 0L; 
	
	private Boolean governmentFinanced = false;
	private Calendar entranceDate = new GregorianCalendar(1990, Calendar.JANUARY, 1);
	private Long studentId;
	private String schoolGraduateSertificate = "";
	private StudentGroup studentGroup = null;
	private Calendar completionDate = new GregorianCalendar(1990, Calendar.JANUARY, 1);
	private Map<Course, Integer> marks = new HashMap<>();
	
	private StudentDao studentDao = new StudentDao();
	
	private static Logger log = Logger.getLogger(Student.class);
	
	public Student() throws DaoException {
		super();
		studentCount++;
		setStudentId(studentCount);
		log.info("Create new Student with id=" + this.getStudentId());
		studentDao.createStudent(this);
	}
	
	public Student(Long studentId) throws DaoException {
		super();
		setStudentId(studentId);
		log.info("Create new Student with id=" + studentId);
	}
	
	public Integer getAverageMark() {
		log.info("Calculation average mark for Student '" + this.getFullName() + "' (id=" + this.getStudentId() + ")");
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
		log.info("Calculation grade for Student '" + this.getFullName() + "' (id=" + this.getStudentId() + ")");
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
	
	public void setEntranceDate(Calendar entranceDate) throws DomainException {
		this.entranceDate = entranceDate;
		
		Integer completionYear = entranceDate.get(Calendar.YEAR) + DEFAULT_EDUCATION_DURATION;
		Calendar endDate = new GregorianCalendar(completionYear, LAST_EDUCATION_MONTH, 1);
		endDate.add(Calendar.DATE, -1);
		this.completionDate = endDate;

		updateStudentInDB();
	}
	
	public void addMark(Course course, Integer mark) throws DomainException {
		this.marks.put(course, mark);
		updateStudentInDB();
	}
	
	public Map<Course, Integer> getMarks() {
		return marks;
	}
	
	public Calendar getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Calendar completionDate) throws DomainException {
		this.completionDate = completionDate;
		updateStudentInDB();
	}
	
	public void setStudentGroup(StudentGroup studentGroup) throws DomainException {
		this.studentGroup = studentGroup;
		updateStudentInDB();
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

	public void setGovernmentFinanced(Boolean governmentFinanced) throws DomainException {
		this.governmentFinanced = governmentFinanced;
		updateStudentInDB();
	}

	public String getSchoolGraduateSertificate() {
		return schoolGraduateSertificate;
	}

	public void setSchoolGraduateSertificate(String schoolGraduateSertificate) throws DomainException {
		this.schoolGraduateSertificate = schoolGraduateSertificate;
		updateStudentInDB();
	}

	public Calendar getEntranceDate() {
		return entranceDate;
	}
	
	public void setMarks(Map<Course, Integer> marks) {
		this.marks = marks;
	}
	
	public static void setStudentCount(Long studentCount) {
		Student.studentCount = studentCount;
	}
	
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	
	public void updateStudentInDB() throws DomainException {
		log.info("Update DB information for Student '" + this.getFullName() + "' (id=" + this.getStudentId() + ")");
		try {
			studentDao.updateStudent(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update Student information", e);
		}
	}
}
