package university.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import university.dao.DaoException;
import university.dao.StudentDao;
import university.domain.Address;
import university.domain.Student;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String studentIdString = request.getParameter("StudentId");
		StudentDao studentDao = new StudentDao();
		Student student = null;
		PrintWriter writer = response.getWriter();
		
		try {
			Long studentId = Long.parseLong(studentIdString);
			student = studentDao.getStudentById(studentId);
		} catch (DaoException e) {
			throw new ServletException("Cannot get Student by Id", e);
		}
		
		request.getRequestDispatcher("student-id.jsp").include(request, response);
		if(null == student) {
			writer.print("<p>Wrong Student Id. Please try again.</p>");
		}
		else {
			writer.print("<ul>");
			writer.print("<li><label>Id: </label>" + student.getStudentId());
			writer.print("<li><label>Name: </label>" + student.getFullName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			Date entranceDate = student.getEntranceDate().getTime();
			writer.print("<li><label>Entrance Date: </label>" + dateFormat.format(entranceDate));
			writer.print("<li><label>Grade: </label>" + student.getGrade());
			Address address = student.getAddress();
			writer.print("<li><label>Address: </label>" + address.getFullAdress());
			writer.print("<li><label>Email: </label>" + address.getEmail());
			writer.print("<li><label>Phone: </label>" + address.getPhone());
			writer.print("</ul>");
		}
		
	}

}
