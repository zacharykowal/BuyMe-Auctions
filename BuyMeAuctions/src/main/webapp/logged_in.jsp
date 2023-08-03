<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BuyMe Home Page</title>
<style>
body {
	font-family: Calibri, sans-serif;
	background-color: #424957;
	color: white;
}
</style>
</head>
<body>
<div align="center">

<h1>BuyMe Home Page</h1>
<form action =LoggedInServlet method="post">
<table>


<tr><td></td><td><input type="submit" name="logout" value="Log Out"></td></tr>

<tr><td><br></td><td><input type="submit" name="allqa" value="Browse All Q&A"></td></tr>

<tr><td><b><br>Check your alerts: </b></td><td>
<tr><td></td><td><input type="submit" name="checkalerts" value="Check Alerts"></td></tr>

<tr><td><b><br>Get alerted when an item becomes available: </b></td><td>
<tr><td>Item ID: </td><td><input type="text" name="setalertitemid"></td></tr>
<tr><td></td><td><input type="submit" name="setalertitem" value="Set Alert"></td></tr>

<tr><td><b><br>Ask a question: </b></td><td>
<tr><td>Your question: </td><td><input type="text" name="yourquestion"></td></tr>
<tr><td></td><td><input type="submit" name="askquestion" value="Submit Question"></td></tr>

<tr><td><b><br>Search questions by keyword: </b></td><td>
<tr><td>Keyword: </td><td><input type="text" name="keyword"></td></tr>
<tr><td></td><td><input type="submit" name="searchq" value="Search Questions"></td></tr>

<tr><td><b><br>Bid on auction: </b></td><td>
<tr><td>Auction ID: </td><td><input type="text" name="biddingauction"></td></tr>
<tr><td>Your initial bid amount: </td><td><input type="text" name="biddinginitial"></td></tr>
<tr><td>Your max bid: </td><td><input type="text" name="biddingmax"></td></tr>
<tr><td></td><td><input type="submit" name="submitbid" value="Submit Bid"></td></tr>

<tr><td><b><br>View history of bids for an auction: </b></td><td>
<tr><td>Auction ID: </td><td><input type="text" name="auctionhistoryid"></td></tr>
<tr><td></td><td><input type="submit" name="viewhistoryofbids" value="View History"></td></tr>

<tr><td><b><br>View auctions that a user participated in: </b></td><td>
<tr><td>Username: </td><td><input type="text" name="auctionhistoryuser"></td></tr>
<tr><td></td><td><input type="submit" name="viewuserhistory" value="View Auction History"></td></tr>

<tr><td><b><br>Start an auction: </b></td><td>
<tr><td>Set end date (YYYY-MM-DD): </td><td><input type="text" name="newauctionend"></td></tr>
<tr><td>Minimum price: </td><td><input type="text" name="newauctionmin"></td></tr>
<tr><td>Bid increments: </td><td><input type="text" name="newauctioninc"></td></tr>
<tr><td>Initial price: </td><td><input type="text" name="newauctioninit"></td></tr>
<tr><td>Item ID (check item table, if the item doesn't exist in table, create it below): </td><td><input type="text" name="newauctionitemid"></td></tr>
<tr><td></td><td><input type="submit" name="createnewa" value="Create Auction"></td></tr>

<tr><td><b><br>Create new item: </b></td><td>
<tr><td>Item brand: </td><td><input type="text" name="newitembrand"></td></tr>
<tr><td>Item subcategory (shirt, pants, or hat): </td><td><input type="text" name="newitemsc"></td></tr>
<tr><td>Item color: </td><td><input type="text" name="newitemcolor"></td></tr>
<tr><td>Item size: </td><td><input type="text" name="newitemsize"></td></tr>
<tr><td>Arm length (enter n/a if not a shirt): </td><td><input type="text" name="newitemal"></td></tr>
<tr><td>Inseam (enter n/a if not pants): </td><td><input type="text" name="newitemis"></td></tr>
<tr><td>Style (enter n/a if not a hat): </td><td><input type="text" name="newitemstyle"></td></tr>
<tr><td></td><td><input type="submit" name="createnewi" value="Create Item"></td></tr>

<tr><td><b><br>See auctions involving similar items: </b></td><td>
<tr><td>Item ID: </td><td><input type="text" name="similaritemid"></td></tr>
<tr><td></td><td><input type="submit" name="similaritembutton" value="See Similar"></td></tr>

<tr><td><b><br>Current Open Auctions</b></td><td>
<tr>
<td><b><br>Auction ID</b></td>
<td><b><br>End Date</b></td>
<td><b><br>Increment</b></td>
<td><b><br>Current Bid</b></td>
</tr>

<% 
try{
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyme", "root", "0212");
	String query = "select auction_id, end_date, increment, current_bid from auction where status = 'open'";
	Statement s = con.createStatement();
	ResultSet rs = s.executeQuery(query);
	
	while(rs.next()){
		%>
		<tr>
		
	    <td><%=rs.getInt("auction_id")%></td>
	    <td><%=rs.getString("end_date")%></td>
	    <td><%=rs.getFloat("increment")%></td>
	    <td><%=rs.getFloat("current_bid")%></td>
	    
	    </tr>
	    <%
	}
	
	%>
	<tr><td><b><br>Which items are part of each auction:</b></td><td>
	<tr>
	<td><b><br>Auction ID</b></td>
	<td><b><br>Item ID</b></td>
	</tr>
	<%
	
	String query2 = "select * from sells";
	Statement s2 = con.createStatement();
	ResultSet rs2 = s2.executeQuery(query2);
	while(rs2.next()){
		%>
		<tr>
		
	    <td><%=rs2.getInt("auction_id")%></td>
	    <td><%=rs2.getInt("item_id")%></td>
	    
	    </tr>
	    <%
	}
	
	%>
	<tr><td><b><br>Items:</b></td><td>
	<tr>
	<td><b><br>Item ID</b></td>
	<td><b><br>Brand</b></td>
	<td><b><br>Subcategory</b></td>
	<td><b><br>Color</b></td>
	<td><b><br>Size</b></td>
	<td><b><br>Arm Length</b></td>
	<td><b><br>Inseam</b></td>
	<td><b><br>Style</b></td>
	</tr>
	<%
	
	String query3 = "select * from clothingitem";
	Statement s3 = con.createStatement();
	ResultSet rs3 = s3.executeQuery(query3);
	while(rs3.next()){
		%>
		<tr>
		
	    <td><%=rs3.getInt("item_id")%></td>
	    <td><%=rs3.getString("brand")%></td>
	    <td><%=rs3.getString("subcategory")%></td>
	    <td><%=rs3.getString("color")%></td>
	    <td><%=rs3.getString("size")%></td>
	    <td><%=rs3.getString("arm_length")%></td>
	    <td><%=rs3.getString("inseam")%></td>
	    <td><%=rs3.getString("style")%></td>
	    
	    </tr>
	    <%
	}
}catch(Exception e){
	e.printStackTrace();
}
%>

<tr><td><b><br>Sort items by: </b></td><td>
<tr><td>Field (brand, subcategory, color, size, arm_length, inseam, or style): </td><td><input type="text" name="sortbyfield"></td></tr>
<tr><td>Ascending or descending (enter asc or desc): </td><td><input type="text" name="aord"></td></tr>
<tr><td></td><td><input type="submit" name="sort" value="Sort Items"></td></tr>

<tr><td><b><br>Search brands: </b></td><td>
<tr><td>Brand you are looking for: </td><td><input type="text" name="searchbrand"></td></tr>
<tr><td></td><td><input type="submit" name="searchbrandbutton" value="Search"></td></tr>

<tr><td><b><br>Search subcategories: </b></td><td>
<tr><td>Subcategory you are looking for: </td><td><input type="text" name="searchsc"></td></tr>
<tr><td></td><td><input type="submit" name="searchscbutton" value="Search"></td></tr>

<tr><td><b><br>Search colors: </b></td><td>
<tr><td>Color you are looking for: </td><td><input type="text" name="searchcolor"></td></tr>
<tr><td></td><td><input type="submit" name="searchcolorbutton" value="Search"></td></tr>

<tr><td><b><br>Search sizes: </b></td><td>
<tr><td>Size you are looking for: </td><td><input type="text" name="searchsize"></td></tr>
<tr><td></td><td><input type="submit" name="searchsizebutton" value="Search"></td></tr>





</table>



</form>
</div>
</body>
</html>