

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int NUM_ITEM_TYPES = 3;
	public static final String YOUR_MYSQL_PASSWORD = "0212";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyme", "root", YOUR_MYSQL_PASSWORD);
			
			if(request.getParameter("logout") != null) {
				//admin clicked the logout button
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("createrep") != null) {
				//admin clicked the create rep button
				String newUsername = request.getParameter("newrepusername");
				String newPassword = request.getParameter("newreppassword");
				String newEmail = request.getParameter("newrepemail");
				String newAddress = request.getParameter("newrepaddress");
				
				if(newUsername.isBlank() || newPassword.isBlank() || newEmail.isBlank() || newAddress.isBlank()) {
					out.println("<font color = black size = 18> Invalid input.<br>");
					out.println("<a href=admin.jsp>Click here to try again</a>");
				} else {
					PreparedStatement ps = con.prepareStatement("insert into Users(username, email, password, address) values(?, ?, ?, ?)");
					ps.setString(1, newUsername);
					ps.setString(2, newEmail);
					ps.setString(3, newPassword);
					ps.setString(4, newAddress);
					
					PreparedStatement ps2 = con.prepareStatement("insert into CustomerRep(username) values(?)");
					ps2.setString(1, newUsername);
					
					try {
						ps.executeUpdate();
						ps2.executeUpdate();
						out.println("<font color = black size = 18> Customer rep account created<br>");
						out.println("<a href=admin.jsp>Click here to return to admin home page</a>");
					}catch(Exception e){
						e.printStackTrace();
						out.println("<font color = black size = 18> Input error<br>");
						out.println("<a href=admin.jsp>Click here to try again</a>");
					}
				}
			}else if(request.getParameter("totalearnings") != null) {
				//admin clicked the generate total earnings button
				PreparedStatement ps = con.prepareStatement("select sum(winning_bid) from auction");
				ResultSet rs = ps.executeQuery();
				rs.next();
				float sum = rs.getFloat(1);
				float totalEarnings = (float) (0.12 * sum);
				out.println("<font color = black size = 18> Total earnings:<br>");
				out.println(totalEarnings);
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}else if(request.getParameter("peritem") != null) {
				//admin clicked the generate earnings per item button
				PreparedStatement ps = con.prepareStatement("select sum(winning_bid) from auction");
				ResultSet rs = ps.executeQuery();
				rs.next();
				float sum = rs.getFloat(1);
				float totalEarnings = (float) (0.12 * sum);
				
				PreparedStatement ps1 = con.prepareStatement("select count(*) from clothingitem");
				ResultSet rs1 = ps1.executeQuery();
				rs1.next();
				int itemCount = rs1.getInt(1);
				float perItem = totalEarnings / itemCount;
				
				out.println("<font color = black size = 18> Earnings per item:<br>");
				out.println(perItem);
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}else if(request.getParameter("peritemtype") != null) {
				//admin clicked the generate earnings per item type button
				PreparedStatement ps = con.prepareStatement("select sum(winning_bid) from auction");
				ResultSet rs = ps.executeQuery();
				rs.next();
				float sum = rs.getFloat(1);
				float totalEarnings = (float) (0.12 * sum);
				
				float perType = totalEarnings / NUM_ITEM_TYPES;
				out.println("<font color = black size = 18> Earnings per item type:<br>");
				out.println(perType);
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}else if(request.getParameter("perenduser") != null) {
				//admin clicked the generate earnings per user button
				PreparedStatement ps = con.prepareStatement("select sum(winning_bid) from auction");
				ResultSet rs = ps.executeQuery();
				rs.next();
				float sum = rs.getFloat(1);
				float totalEarnings = (float) (0.12 * sum);
				
				PreparedStatement ps1 = con.prepareStatement("select count(*) from enduser");
				ResultSet rs1 = ps1.executeQuery();
				rs1.next();
				int userCount = rs1.getInt(1);
				
				float perUser = totalEarnings / userCount;
				out.println("<font color = black size = 18> Earnings per end user:<br>");
				out.println(perUser);
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}else if(request.getParameter("bestselling") != null) {
				//admin clicked the best selling items button
				PreparedStatement ps = con.prepareStatement("select s.item_id, count(s.auction_id) as amount_sold from sells s, auction a where s.auction_id = a.auction_id and a.status = \"closed\" group by s.item_id");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Best selling items:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getInt(2) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}else if(request.getParameter("bestbuyers") != null) {
				//admin clicked the best buyers button
				PreparedStatement ps = con.prepareStatement("select winner as buyer, count(winner) as auctions_won from auction where status = \"closed\" group by winner");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Best buyers (most auctions won):</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getString(1) + "</td><td>" + rs.getInt(2) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=admin.jsp>Click here to return to admin home page</a>");
			}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
