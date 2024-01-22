import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoggedInServlet
 */
@WebServlet("/LoggedInServlet")
public class LoggedInServlet extends HttpServlet {
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
				//user clicked the logout button
				clearCookie(request, response);
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("askquestion") != null) {
				//user clicked the ask question button
				String question = request.getParameter("yourquestion");
				
				if (!question.isBlank()) {
					PreparedStatement ps = con.prepareStatement("insert into question(question) values(?)");
					ps.setString(1, question);
					ps.executeUpdate();
					
					PreparedStatement ps1 = con.prepareStatement("select question_id from question where question=?");
					ps1.setString(1, question);
					ResultSet rs = ps1.executeQuery();
					rs.next();
					int qid = rs.getInt(1);
					
					PreparedStatement ps2 = con.prepareStatement("insert into asks(username, question_id) values(?, ?)");
					ps2.setString(1, loggedInUser);
					ps2.setInt(2, qid);
					ps2.executeUpdate();
					
					out.println("<font color = black size = 18> Question submitted<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}

			}else if(request.getParameter("submitbid") != null) {
				//user clicked the submit bid button
				
				try {
					int auctionId = Integer.parseInt(request.getParameter("biddingauction"));
					float initialBid = Float.parseFloat(request.getParameter("biddinginitial"));
					float maxBid = Float.parseFloat(request.getParameter("biddingmax"));
					
					PreparedStatement x = con.prepareStatement("select current_bid from auction where auction_id=?");
					x.setInt(1, auctionId);
					ResultSet r = x.executeQuery();
					r.next();
					float currentBid = r.getFloat(1);

					if(currentBid < initialBid) {
						//new bid is higher than the current leading bid
						PreparedStatement ps = con.prepareStatement("update auction set current_bid=?, current_bid_user=? where auction_id=?");
						ps.setFloat(1, initialBid);
						ps.setString(2, loggedInUser);
						ps.setInt(3, auctionId);
						ps.executeUpdate();
						
						//alert users participating in auction that a new higher bid has been placed
						PreparedStatement ps4 = con.prepareStatement("select e.username, b.upper_limit from enduser e, bid b, places p, has h, auction a where a.auction_id=? and a.auction_id = h.auction_id and h.bid_id = b.bid_id and b.bid_id = p.bid_id and p.username = e.username");
						ps4.setInt(1, auctionId);
						ResultSet rs2 = ps4.executeQuery();
						while(rs2.next()) {
							String currentUser = rs2.getString(1);
							float upperLimit = rs2.getFloat(2);
							if(upperLimit < initialBid) {
								//alert buyer that bid is above upper limit
								PreparedStatement ps5 = con.prepareStatement("insert into alert(text) values(\"bid placed on auction higher than your limit\")");
								ps5.executeUpdate();
								PreparedStatement ps6 = con.prepareStatement("insert into sentto(alert_id, username) values(last_insert_id(), ?)");
								ps6.setString(1, currentUser);
								ps6.executeUpdate();
							}else {
								//higher bid placed but not yet at the upper limit
								
								PreparedStatement ps5 = con.prepareStatement("insert into alert(text) values(\"higher bid placed on auction you are bidding on\")");
								ps5.executeUpdate();
								PreparedStatement ps6 = con.prepareStatement("insert into sentto(alert_id, username) values(last_insert_id(), ?)");
								ps6.setString(1, currentUser);
								ps6.executeUpdate();
								
								
							}
						}
					}
					
					PreparedStatement ps = con.prepareStatement("insert into bid(amount, upper_limit) values(?, ?)");
					ps.setFloat(1, initialBid);
					ps.setFloat(2, maxBid);
					ps.executeUpdate();
					
					
					PreparedStatement ps1 = con.prepareStatement("select bid_id from bid where amount=? and upper_limit=?");
					ps1.setFloat(1, initialBid);
					ps1.setFloat(2, maxBid);
					ResultSet rs = ps1.executeQuery();
					rs.next();
					int bidId = rs.getInt(1);
					
					PreparedStatement ps2 = con.prepareStatement("insert into places(bid_id, username) values(?, ?)");
					ps2.setInt(1, bidId);
					ps2.setString(2, loggedInUser);
					ps2.executeUpdate();
					
					PreparedStatement ps3 = con.prepareStatement("insert into has(bid_id, auction_id) values(?, ?)");
					ps3.setInt(1, bidId);
					ps3.setInt(2, auctionId);
					ps3.executeUpdate();
					
					
					
					out.println("<font color = black size = 18> Bid placed.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}
				
			}else if(request.getParameter("createnewa") != null) {
				//user clicked the create new auction button
				try {
					String date = request.getParameter("newauctionend");
					LocalDate.parse(date);
					float minPrice = Float.parseFloat(request.getParameter("newauctionmin"));
					float increment = Float.parseFloat(request.getParameter("newauctioninc"));
					float initial = Float.parseFloat(request.getParameter("newauctioninit"));
					int itemId = Integer.parseInt(request.getParameter("newauctionitemid"));
		
					PreparedStatement ps = con.prepareStatement("insert into auction(end_date, min_price, status, increment, initial) values(?, ?, ?, ?, ?)");
					ps.setString(1, date);
					ps.setFloat(2, minPrice);
					ps.setString(3, "open");
					ps.setFloat(4, increment);
					ps.setFloat(5, initial);
					ps.executeUpdate();
					
					//
					PreparedStatement ps1 = con.prepareStatement("select auction_id from auction where end_date=? and min_price=?");
					ps1.setString(1, date);
					ps1.setFloat(2, minPrice);
					ResultSet rs = ps1.executeQuery();
					rs.next();
					int auctionId = rs.getInt(1);
					
					PreparedStatement ps2 = con.prepareStatement("insert into sells(auction_id, item_id) values(?, ?)");
					ps2.setInt(1, auctionId);
					ps2.setInt(2, itemId);
					ps2.executeUpdate();
					
					PreparedStatement ps3 = con.prepareStatement("insert into hosts(auction_id, username) values(?, ?)");
					ps3.setInt(1, auctionId);
					ps3.setString(2, loggedInUser);
					ps3.executeUpdate();
					
					//alert users that an item they were waiting for has become available
					PreparedStatement ps4 = con.prepareStatement("select * from setsalertfor where item_id=?");
					ps4.setInt(1, itemId);
					ResultSet rs2 = ps4.executeQuery();
					while(rs2.next()) {
						PreparedStatement ps5 = con.prepareStatement("insert into alert(text) values(\"an item you were waiting for is now available\")");
						ps5.executeUpdate();
						
						PreparedStatement ps6 = con.prepareStatement("insert into sentto(alert_id, username) values(last_insert_id(), ?)");
						ps6.setString(1, rs2.getString(1));
						ps6.executeUpdate();
					}
							
					out.println("<font color = black size = 18> Auction created.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}
				
			}else if(request.getParameter("createnewi") != null) {
				//user clicked the create new item button
				String brand = request.getParameter("newitembrand");
				String subCat = request.getParameter("newitemsc");
				String color = request.getParameter("newitemcolor");
				String size = request.getParameter("newitemsize");
				String armLength = request.getParameter("newitemal");
				String inseam = request.getParameter("newitemis");
				String style = request.getParameter("newitemstyle");
				
				if (!brand.isBlank() && !subCat.isBlank() && !color.isBlank() && !size.isBlank() && !armLength.isBlank() && !inseam.isBlank() && !style.isBlank()) {
					PreparedStatement ps = con.prepareStatement("insert into clothingitem(brand, subcategory, color, size, arm_length, inseam, style) values(?, ?, ?, ?, ?, ?, ?)");
					ps.setString(1, brand);
					ps.setString(2, subCat);
					ps.setString(3, color);
					ps.setString(4, size);
					ps.setString(5, armLength);
					ps.setString(6, inseam);
					ps.setString(7, style);
					ps.executeUpdate();
					
					out.println("<font color = black size = 18> Item created.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}

			}else if(request.getParameter("checkalerts") != null) {
				//user clicked the check alerts button
				PreparedStatement ps = con.prepareStatement("select a.text from alert a, sentto s where a.alert_id = s.alert_id and s.username=?");
				ps.setString(1, loggedInUser);
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Your alerts:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getString(1) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
			}else if(request.getParameter("allqa") != null) {
				//user clicked the check all questions & answers button
				PreparedStatement ps = con.prepareStatement("select * from question");
				ResultSet rs = ps.executeQuery();
				out.print("<table width=20% border=1>");
				out.print("<caption>All questions and answers:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
			}else if(request.getParameter("searchq") != null) {
				//user clicked the search questions by keyword button
				String keyword = request.getParameter("keyword");
				PreparedStatement ps = con.prepareStatement("select question, answer from question where question like ?");
				ps.setString(1, "%" + keyword + "%");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Questions containing the keyword:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("viewhistoryofbids") != null) {
				//user clicked the history of bids button
				int auctionId = Integer.parseInt(request.getParameter("auctionhistoryid"));
				PreparedStatement ps = con.prepareStatement("select e.username, b.amount from enduser e, bid b, places p, has h, auction a where a.auction_id=? and a.auction_id = h.auction_id and h.bid_id = b.bid_id and b.bid_id = p.bid_id and p.username = e.username");
				ps.setInt(1, auctionId);
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>History of bids:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getString(1) + "</td><td>" + rs.getFloat(2) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
			}else if(request.getParameter("viewuserhistory") != null) {
				//user clicked the user history button
				String username = request.getParameter("auctionhistoryuser");
				PreparedStatement ps = con.prepareStatement("select a.auction_id from auction a, has h, places p, bid b, enduser e where e.username=? and e.username = p.username and p.bid_id = b.bid_id and b.bid_id = h.bid_id and h.auction_id = a.auction_id");
				ps.setString(1, username);
				
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Auction history for user:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("sort") != null) {
				//user clicked the sort button
				String field = request.getParameter("sortbyfield");
				String aord = request.getParameter("aord");
				
				if ((aord.equals("asc") || aord.equals("desc")) && (field.equals("brand") || field.equals("subcategory") || field.equals("color") || field.equals("size") || field.equals("arm_length") || field.equals("inseam") || field.equals("style"))) {
					PreparedStatement ps = con.prepareStatement("select * from clothingitem order by " + field + " " + aord);
					ResultSet rs = ps.executeQuery();
					
					out.print("<table width=20% border=1>");
					out.print("<caption>Items sorted:</caption>");
					
					out.print("</br></br>");
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					
					out.print("<tr>");
					for(int i = 1; i <= columnCount; i++) {
						out.print("<th>" + rsmd.getColumnName(i) + "</th>");
					}
					out.print("</tr>");
					
					while(rs.next()) {
						out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getString(5) + "</td><td>" + rs.getString(6) + "</td><td>" + rs.getString(7) + "</td><td>" + rs.getString(8) + "</td></tr>");
					}
					out.print("</table>");
					out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				} else {
					out.println("<font color = black size = 18> Invalid input.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}
				
			}else if(request.getParameter("searchbrandbutton") != null) {
				//user clicked the search brand button
				String brand = request.getParameter("searchbrand");
				PreparedStatement ps = con.prepareStatement("select * from clothingitem where brand like ?");
				ps.setString(1, "%" + brand + "%");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Items sorted:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getString(5) + "</td><td>" + rs.getString(6) + "</td><td>" + rs.getString(7) + "</td><td>" + rs.getString(8) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("searchscbutton") != null) {
				//user clicked the search subcategory button
				String sc = request.getParameter("searchsc");
				PreparedStatement ps = con.prepareStatement("select * from clothingitem where subcategory like ?");
				ps.setString(1, "%" + sc + "%");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Items sorted:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getString(5) + "</td><td>" + rs.getString(6) + "</td><td>" + rs.getString(7) + "</td><td>" + rs.getString(8) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("searchcolorbutton") != null) {
				//user clicked the search color button
				String color = request.getParameter("searchcolor");
				PreparedStatement ps = con.prepareStatement("select * from clothingitem where color like ?");
				ps.setString(1, "%" + color + "%");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Items sorted:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getString(5) + "</td><td>" + rs.getString(6) + "</td><td>" + rs.getString(7) + "</td><td>" + rs.getString(8) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("searchsizebutton") != null) {
				//user clicked the search size button
				String size = request.getParameter("searchsize");
				PreparedStatement ps = con.prepareStatement("select * from clothingitem where size like ?");
				ps.setString(1, "%" + size + "%");
				ResultSet rs = ps.executeQuery();
				
				out.print("<table width=20% border=1>");
				out.print("<caption>Items sorted:</caption>");
				
				out.print("</br></br>");
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				
				out.print("<tr>");
				for(int i = 1; i <= columnCount; i++) {
					out.print("<th>" + rsmd.getColumnName(i) + "</th>");
				}
				out.print("</tr>");
				
				while(rs.next()) {
					out.print("<tr><td>" + rs.getInt(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td>" + rs.getString(5) + "</td><td>" + rs.getString(6) + "</td><td>" + rs.getString(7) + "</td><td>" + rs.getString(8) + "</td></tr>");
				}
				out.print("</table>");
				out.println("<br><a href=logged_in.jsp>Click here to return to home page</a>");
				
			}else if(request.getParameter("setalertitem") != null) {
				//user clicked the set alert for button
				
				try {
					int itemId = Integer.parseInt(request.getParameter("setalertitemid"));
					PreparedStatement ps = con.prepareStatement("insert into setsalertfor(username, item_id) values(?, ?)");
					ps.setString(1, loggedInUser);
					ps.setInt(2, itemId);
					ps.executeUpdate();
					
					out.println("<font color = black size = 18> Alert set.<br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input <br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}

			}else if(request.getParameter("similaritembutton") != null) {
				//user clicked the similar item button
				try {
					int itemId = Integer.parseInt(request.getParameter("similaritemid"));
					PreparedStatement ps = con.prepareStatement("select color, subcategory from clothingitem where item_id=?");
					ps.setInt(1, itemId);
					ResultSet rs = ps.executeQuery();
					rs.next();
					String color = rs.getString(1);
					String subCat = rs.getString(2);
					
					PreparedStatement ps2 = con.prepareStatement("select * from clothingitem where color=? and subcategory=?");
					ps2.setString(1, color);
					ps2.setString(2, subCat);
					ResultSet rs2 = ps2.executeQuery();
					
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
					
					out.print("<table width=20% border=1>");
					out.print("<caption>Similar Items:</caption>");
					
					out.print("</br></br>");
					ResultSetMetaData rsmd = rs2.getMetaData();
					int columnCount = rsmd.getColumnCount();
					
					out.print("<tr>");
					for(int i = 1; i <= columnCount; i++) {
						out.print("<th>" + rsmd.getColumnName(i) + "</th>");
					}
					out.print("</tr>");
					
					while(rs2.next()) {
						out.print("<tr><td>" + rs2.getInt(1) + "</td><td>" + rs2.getString(2) + "</td><td>" + rs2.getString(3) + "</td><td>" + rs2.getString(4) + "</td><td>" + rs2.getString(5) + "</td><td>" + rs2.getString(6) + "</td><td>" + rs2.getString(7) + "</td><td>" + rs2.getString(8) + "</td></tr>");
					}
					
					PreparedStatement ps3 = con.prepareStatement("select t.item_id, a.auction_id, a.end_date, a.increment, a.current_bid from auction a, (select * from clothingitem where color=? and subcategory=?) t , sells s where\n"
							+ " t.item_id = s.item_id and s.auction_id = a.auction_id and a.status = \"open\"");
					ps3.setString(1, color);
					ps3.setString(2, subCat);
					ResultSet rs3 = ps3.executeQuery();
					
					out.print("<table width=20% border=1>");
					out.print("<caption>Open auctions involving these items:</caption>");
					
					out.print("</br></br>");
					ResultSetMetaData rsmd2 = rs3.getMetaData();
					int columnCount2 = rsmd2.getColumnCount();
					
					out.print("<tr>");
					for(int i = 1; i <= columnCount2; i++) {
						out.print("<th>" + rsmd2.getColumnName(i) + "</th>");
					}
					out.print("</tr>");
					
					while(rs3.next()) {
						out.print("<tr><td>" + rs3.getInt(1) + "</td><td>" + rs3.getInt(2) + "</td><td>" + rs3.getString(3) + "</td><td>" + rs3.getFloat(4) + "</td><td>" + rs3.getFloat(5) + "</td></tr>");
					}
				} catch (Exception e) {
					out.println("<font color = black size = 18> Invalid input <br>");
					out.println("<a href=logged_in.jsp>Click here to return to home page</a>");
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
