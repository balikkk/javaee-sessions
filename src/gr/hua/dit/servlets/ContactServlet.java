package gr.hua.dit.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gr.hua.dit.beans.Contact;
import gr.hua.dit.beans.User;

@WebServlet(name = "Contact", urlPatterns = { "/Contact" })
public class ContactServlet extends HttpServlet {

	private static final long serialVersionUID = 6227417619618196810L;
		

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String pageTitle = "";
		String contact_id = request.getParameter("contact_id");
		String user_id = request.getParameter("user_id");
		
		HttpSession session = request.getSession();
	

		
		try {
			if (contact_id != null) {
				try {
					int id = Integer.parseInt(contact_id);
					int uid = Integer.parseInt(user_id);
					System.out.println("user id " + user_id);
					System.out.println("contact id " + contact_id);

					ps = con.prepareStatement(
							"select id, name, surname, phone, birthdate, user_id from Contacts where id = ? and user_id = ?");
					ps.setString(1, String.valueOf(id));
					ps.setString(2, String.valueOf(uid));
					pageTitle = "My Contacts Page";
				} catch (NumberFormatException e) {
					System.out.println("Wrong number");
				}
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {

					Contact contact = new Contact(rs.getInt("id"), rs.getString("name"), rs.getString("surname"),
							rs.getInt("phone"), rs.getDate("birthdate"), rs.getInt("user_id"));
					session.setAttribute("Contact", contact);
					session.setAttribute("pageTitle", "Contact Page");
					response.sendRedirect("protected/contact.jsp");
				} else {
					IOException e = new IOException("You have not acces to change this Contact");
					throw e;
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Database connection problem");
			throw new ServletException("DB Connection problem.");
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("SQLException in closing PreparedStatement or ResultSet");
			}

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String contact_id = request.getParameter("id");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String phone = request.getParameter("phone");
		String birthdate = request.getParameter("birthdate");
		String user_id = request.getParameter("user_id");

		Connection con = (Connection) getServletContext().getAttribute("DBConnection");
		PreparedStatement ps = null;
		HttpSession session = request.getSession();
		


		if (user_id != null) {
			if (name != null && phone != null) {
				try {
					Contact contact = new Contact(name, surname, Integer.parseInt(phone), Date.valueOf(birthdate),
							Integer.parseInt(user_id));
					
					if (contact_id != null) {
					System.out.println("in update");
					ps = con.prepareStatement("update Contacts set name=?, surname=?, phone=?, birthdate=? where id = ?");
					ps.setString(1, String.valueOf(name));
					ps.setString(2, String.valueOf(surname));
					ps.setString(3, String.valueOf(phone));
					ps.setString(4, String.valueOf(birthdate));
					ps.setString(5, String.valueOf(contact_id));
					System.out.println(ps);

					}
					else {
						System.out.println("in insert");
						ps = con.prepareStatement("insert into  Contacts (name, surname, phone, birthdate, user_id) values (?, ?, ?, ?, ?)");
						ps.setString(1, String.valueOf(name));
						ps.setString(2, String.valueOf(surname));
						ps.setString(3, String.valueOf(phone));
						ps.setString(4, String.valueOf(birthdate));
						ps.setString(5, String.valueOf(user_id));
					}
					
					int r= ps.executeUpdate();
					
					if (r > 0) {
						//session.setAttribute("message", "Update Success");
						response.sendRedirect("Contacts");
					}
					else {
						System.out.println("Contact not updated");
						throw new ServletException("Contact not updated.");
					}

				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Database connection problem");
					throw new ServletException("DB Connection problem.");
				} finally {
					try {
						
						ps.close();
					} catch (SQLException e) {
						System.out.println("SQLException in closing PreparedStatement or ResultSet");
					}

				}
			} else {
				System.out.println("No name and phone provided");
				throw new ServletException("No name and phone provided");
			}

		} else {
			System.out.println("Not user id provided");
			throw new ServletException("Not user id provided");
		}

	}

}
