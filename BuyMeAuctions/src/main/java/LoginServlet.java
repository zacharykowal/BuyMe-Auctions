import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String YOUR_MYSQL_PASSWORD = "0212";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyme", "root", YOUR_MYSQL_PASSWORD);
			
			//Check all auctions, close them if today's date matches the end date
			Date today = new Date();
			Calendar c1 = Calendar.getInstance();
			c1.setTime(today);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			PreparedStatement p = con.prepareStatement("select auction_id, end_date, min_price, current_bid, status from auction");
			ResultSet s = p.executeQuery();
			while(s.next()) {
				int auctionId = s.getInt(1);
				String endDate = s.getString(2);
				float minPrice = s.getFloat(3);
				float currentBid = s.getFloat(4);
				String status = s.getString(5);
				if(status.equals("open")) {
					try {
						Date auctionEnd = sdf.parse(endDate);
						Calendar c2 = Calendar.getInstance();
						c2.setTime(auctionEnd);
						if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
								c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
								c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
							if(currentBid < minPrice) {
								//reserve is not met
								PreparedStatement p1 = con.prepareStatement("update auction set status=\"closed\", winner=\"no winner\" where auction_id=?");
								p1.setInt(1, auctionId);
								p1.executeUpdate();
							} else {
								//reserve is met, declare winner
								PreparedStatement p1 = con.prepareStatement("update auction set status=\"closed\", winner=current_bid_user, winning_bid=current_bid where auction_id=?");
								p1.setInt(1, auctionId);
								p1.executeUpdate();
								
								PreparedStatement p2 = con.prepareStatement("insert into alert(text) values(\"you won an auction\")");
								p2.executeUpdate();
								
								PreparedStatement p3 = con.prepareStatement("insert into sentto(alert_id, username) values(last_insert_id(), ?)");
								PreparedStatement p4 = con.prepareStatement("select winner from auction where auction_id=?");
								p4.setInt(1, auctionId);
								ResultSet rs = p4.executeQuery();
								rs.next();
								String username = rs.getString(1);
								p3.setString(1, username);
								p3.executeUpdate();
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			
			
			//if the user clicked the log in button
			if(request.getParameter("createbutton") == null) {
				String name = request.getParameter("username");
				String pass = request.getParameter("password");
				
				PreparedStatement ps = con.prepareStatement("select username from users where username=? and password=?");
				ps.setString(1, name);
				ps.setString(2, pass);
				
				ResultSet rs = ps.executeQuery();
				
				//If the username and password match an entry in the users table,
				//check what type of user they are and redirect them to the appropriate landing page.
				//create cookie to maintain the current logged in user
				if(rs.next()) {
					PreparedStatement ps1 = con.prepareStatement("select username from EndUser where username=?");
					ps1.setString(1, name);
					ResultSet rs1 = ps1.executeQuery();
					if(rs1.next()) {
						//user is a regular end user
						Cookie c = new Cookie("currentUser", name);
						c.setMaxAge(365 * 24 * 60 * 60);
						response.addCookie(c);
						RequestDispatcher rd = request.getRequestDispatcher("logged_in.jsp");
						rd.forward(request, response);
					}
					PreparedStatement ps2 = con.prepareStatement("select username from Admin where username=?");
					ps2.setString(1, name);
					ResultSet rs2 = ps2.executeQuery();
					if(rs2.next()) {
						//user is an admin
						RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
						rd.forward(request, response);
					}
					PreparedStatement ps3 = con.prepareStatement("select username from CustomerRep where username=?");
					ps3.setString(1, name);
					ResultSet rs3 = ps3.executeQuery();
					if(rs3.next()) {
						//user is a customer rep
						Cookie c = new Cookie("currentUser", name);
						c.setMaxAge(365 * 24 * 60 * 60);
						response.addCookie(c);
						RequestDispatcher rd = request.getRequestDispatcher("rep.jsp");
						rd.forward(request, response);
					}
				}
				//username and password do not match any entries in the users table
				else {
					out.println("<font color = black size = 18> Incorrect username/password<br>");
					out.println("<a href=login.jsp>Try Again</a>");
				}
			//the user clicked the create account button
			} else {
				String newUsername = request.getParameter("newusername");
				String newPassword = request.getParameter("newpassword");
				String newEmail = request.getParameter("newemail");
				String newAddress = request.getParameter("newaddress");
				
				if (!newUsername.isBlank() && !newPassword.isBlank() && !newEmail.isBlank() && !newAddress.isBlank()) {
					PreparedStatement ps4 = con.prepareStatement("insert into Users(username, email, password, address) values(?, ?, ?, ?)");
					ps4.setString(1, newUsername);
					ps4.setString(2, newEmail);
					ps4.setString(3, newPassword);
					ps4.setString(4, newAddress);
					
					PreparedStatement ps5 = con.prepareStatement("insert into EndUser(username, isbuyer, isseller) values(?, false, false)");
					ps5.setString(1, newUsername);
					
					try {
						ps4.executeUpdate();
						ps5.executeUpdate();
						out.println("<font color = black size = 18> Account created<br>");
						out.println("<a href=login.jsp>Click here to login</a>");
					} catch(Exception e) {
						out.println("<font color = black size = 18> Input error<br>");
						out.println("<a href=login.jsp>Click here to try again</a>");
					}
				} else {
					out.println("<font color = black size = 18> Input error<br>");
					out.println("<a href=login.jsp>Click here to try again</a>");
				}
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
