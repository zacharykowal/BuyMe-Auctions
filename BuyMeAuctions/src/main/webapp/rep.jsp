<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BuyMe Customer Rep Home Page</title>
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

<h1>BuyMe Customer Rep Home Page</h1>
<form action =RepServlet method="post">
<table>


<tr><td></td><td><input type="submit" name="logout" value="Log Out"></td></tr>

<tr><td><br>CUSTOMER REP TOOLS: </td><td>
<tr><td><br>EDIT ACCOUNT INFORMATION:</td><td>
<tr><td>Editing username (can't be changed): </td><td><input type="text" name="editingusername"></td></tr>
<tr><td>Change password to: </td><td><input type="password" name="editingpassword"></td></tr>
<tr><td></td><td><input type="submit" name="changepass" value="Change Password"></td></tr>
<tr><td>Change email to: </td><td><input type="text" name="editingemail"></td></tr>
<tr><td></td><td><input type="submit" name="changeemail" value="Change Email"></td></tr>
<tr><td>Change home address to: </td><td><input type="text" name="editingaddress"></td></tr>
<tr><td></td><td><input type="submit" name="changeaddress" value="Change Address"></td></tr>

<tr><td><br>REMOVE BID:</td><td>
<tr><td>ID of the bid to be removed: </td><td><input type="text" name="removebidtext"></td></tr>
<tr><td></td><td><input type="submit" name="removebidbutton" value="Remove Bid"></td></tr>

<tr><td><br>REMOVE AUCTION:</td><td>
<tr><td>ID of the auction to be removed: </td><td><input type="text" name="removeauctiontext"></td></tr>
<tr><td></td><td><input type="submit" name="removeauctionbutton" value="Remove Auction"></td></tr>

<tr><td><br>REPLY TO USER QUESTION:</td><td>
<tr><td>Question ID: </td><td><input type="text" name="qid"></td></tr>
<tr><td>Your reply: </td><td><input type="text" name="reply"></td></tr>
<tr><td></td><td><input type="submit" name="replybutton" value="Reply"></td></tr>


</table>



</form>
</div>
</body>
</html>