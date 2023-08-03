

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RepServlet
 */
@WebServlet("/RepServlet")
public class RepServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String YOUR_MYSQL_PASSWORD = "0212";
	
	private void clearCookie(HttpServletRequest request, HttpServletResponse response) {
	    Cookie cookies[] = request.getCookies();
	    if (cookies != null) {
	        for (Cookie c : cookies) {
	            c.setValue("");
	            c.setPath("/");
	            c.setMaxAge(0);
	            response.addCookie(c);
	        }
	    }
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyme", "root", YOUR_MYSQL_PASSWORD);
			
			//get the username of the current session
			String loggedInUser = null;
			Cookie cookies[] = request.getCookies();
			for(Cookie c : cookies) {
				if(c.getName().equals("currentUser")) {
					loggedInUser = c.getValue();
				}
			}
			
			//cookie error, redirect to home page to log in again
			if(loggedInUser.equals(null)) {
				clearCookie(request, response);
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
			}
			
			if(request.getParameter("logout") != null) {
				//rep clicked the logout button
				clearCookie(request, response);
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("changepass") != null) {
				//rep clicked the change password button
				String user = request.getParameter("editingusername");
				String pass = request.getParameter("editingpassword");
				
				if(!user.isBlank() && !pass.isBlank()) {
					PreparedStatement ps = con.prepareStatement("update Users set password = ? where username = ?");
					ps.setString(1, pass);
					ps.setString(2, user);
					ps.executeUpdate();
					out.println("<font color = black size = 18> Account info successfully updated<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}

			}else if(request.getParameter("changeemail") != null) {
				//rep clicked the change email button
				String user = request.getParameter("editingusername");
				String email = request.getParameter("editingemail");
				
				if (!user.isBlank() && !email.isBlank()) {
					PreparedStatement ps = con.prepareStatement("update Users set email = ? where username = ?");
					ps.setString(1, email);
					ps.setString(2, user);
					ps.executeUpdate();
					out.println("<font color = black size = 18> Account info successfully updated<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}
				
			}else if(request.getParameter("changeaddress") != null) {
				//rep clicked the change address button
				String user = request.getParameter("editingusername");
				String address = request.getParameter("editingaddress");
				
				if (!user.isBlank() && !address.isBlank()) {
					PreparedStatement ps = con.prepareStatement("update Users set address = ? where username = ?");
					ps.setString(1, address);
					ps.setString(2, user);
					ps.executeUpdate();
					out.println("<font color = black size = 18> Account info successfully updated<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}

			}else if(request.getParameter("removebidbutton") != null) {
				//rep clicked the remove bid button
				
				try {
					int bidId = Integer.parseInt(request.getParameter("removebidtext"));
					PreparedStatement ps = con.prepareStatement("delete from bid where bid_id =?");
					ps.setInt(1, bidId);
					ps.executeUpdate();
					out.println("<font color = black size = 18> Bid removed<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}

			}else if(request.getParameter("removeauctionbutton") != null) {
				//rep clicked the remove auction button
				
				try {
					int auctionId = Integer.parseInt(request.getParameter("removeauctiontext"));
					PreparedStatement ps = con.prepareStatement("delete from auction where auction_id =?");
					ps.setInt(1, auctionId);
					ps.executeUpdate();
					out.println("<font color = black size = 18> Auction removed<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}

			}else if(request.getParameter("replybutton") != null) {
				//rep clicked the reply button
				
				try {
					int questionId = Integer.parseInt(request.getParameter("qid"));
					String reply = request.getParameter("reply");
					
					if (!reply.isBlank()) {
						PreparedStatement ps = con.prepareStatement("update question set answer=? where question_id=?");
						PreparedStatement ps1 = con.prepareStatement("insert into answers(username, question_id) values(?, ?)");
						ps.setString(1, reply);
						ps.setInt(2, questionId);
						ps.executeUpdate();
						ps1.setString(1, loggedInUser);
						ps1.setInt(2, questionId);
						ps1.executeUpdate();
						out.println("<font color = black size = 18> Question answered<br>");
						out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
					} else {
						out.println("<font color = black size = 18> Invalid input<br>");
						out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
					}
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=rep.jsp>Click here to return to customer rep home page</a>");
				}

			}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
