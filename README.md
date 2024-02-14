# BuyMe Auctions

This project was created using Java, JSP, HTML, and MySQL to simulate the operations of an auction website similar to eBay. There are three different types of users: admins, customer reps, and regular end users. Each type of user can interact with the website in a number of ways.

## Admins
- Create new customer rep accounts
- Generate sales reports using various criteria

## Customer Reps
- Edit user account information
- Delete bids and auctions
- Reply to questions on the site Q&A board

## End Users
- Create & bid on auctions
- Search for items and auctions
- Ask questions on the site Q&A board

# Running the Project

This project was created using Eclipse IDE. To run it:

1. Import the project into the Eclipse workspace on your local machine
2. In Eclipse, create a new Tomcat v8.5 server (Window -> Show View -> Servers, click "create new server" in the servers view, go through with the setup and then right click on the server to start it)
3. Import the provided schema file into MySQL and start the MySQL server
4. Configure the build path. In the project explorer, navigate to Java Resources -> Libraries -> JRE System Library. Right click on JRE System Library, then Build Path -> Configure Build Path. Select the Server Runtime Tomcat library and apply.
5. The environment is now set up. Navigate to src -> main -> webapp. Right click on login.jsp -> Run As -> Run on Server. Select the Tomcat server from earlier and the home page should open.

Login to the website using one of the usernames and passwords (provided in the MySQL database) to see everything in action!





