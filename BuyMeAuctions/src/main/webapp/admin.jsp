<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BuyMe Admin Home Page</title>
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

<h1>BuyMe Admin Home Page</h1>
<form action =AdminServlet method="post">
<table>


<tr><td></td><td><input type="submit" name="logout" value="Log Out"></td></tr>

<tr><td><br>ADMIN TOOLS: </td><td>
<tr><td><br>Create a customer rep account: </td><td>
<tr><td>User Name: </td><td><input type="text" name="newrepusername"></td></tr>
<tr><td>Password: </td><td><input type="password" name="newreppassword"></td></tr>
<tr><td>Email: </td><td><input type="text" name="newrepemail"></td></tr>
<tr><td>Home Address: </td><td><input type="text" name="newrepaddress"></td></tr>
<tr><td></td><td><input type="submit" name="createrep" value="Create Account"></td></tr>

<tr><td><br>Generate sales reports for: </td><td>
<tr><td>Total earnings </td><td><input type="submit" name="totalearnings" value="Generate"></td></tr>
<tr><td>Earnings per item </td><td><input type="submit" name="peritem" value="Generate"></td></tr>
<tr><td>Earnings per item type </td><td><input type="submit" name="peritemtype" value="Generate"></td></tr>
<tr><td>Earnings per end user </td><td><input type="submit" name="perenduser" value="Generate"></td></tr>
<tr><td>Best-selling items </td><td><input type="submit" name="bestselling" value="Generate"></td></tr>
<tr><td>Best buyers </td><td><input type="submit" name="bestbuyers" value="Generate"></td></tr>
</table>



</form>
</div>
</body>
</html>