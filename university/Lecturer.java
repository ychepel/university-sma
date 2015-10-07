package university;

import java.util.Date;

public class Lecturer extends Person {
	private String scienceDegree;
	private Date hiringDate;
	private String currentPosition;
	
	public String getScienceDegree() {
		return scienceDegree;
	}
	public void setScienceDegree(String scienceDegree) {
		this.scienceDegree = scienceDegree;
	}
	public Date getHiringDate() {
		return hiringDate;
	}
	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}
	public String getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(String currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	
}
