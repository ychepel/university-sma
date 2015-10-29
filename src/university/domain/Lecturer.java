package university.domain;

import org.apache.log4j.Logger;

public class Lecturer extends Person {
	private String scienceDegree;
	private String currentPosition;
	private Integer lecturerId;
	
	private static Logger log = Logger.getLogger(Lecturer.class);
	
	public String getScienceDegree() {
		return scienceDegree;
	}
	public void setScienceDegree(String scienceDegree) {
		this.scienceDegree = scienceDegree;
	}
	public String getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}
	public void setLecturerId(int i) {
		this.lecturerId = i;
	}
	public Integer getLecturerId() {
		return lecturerId;
	}
	public Lecturer() {
		super();
		log.info("Create new Lecturer");
	}
	
	
}
