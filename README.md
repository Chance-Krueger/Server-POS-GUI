# Server-POS-GUI
Java GUI simulating a restaurant server's tasks. Run LoginFrame to start. Log in or create an account to seat tables, take orders, print/pay bills, and view tips. Manager login (management / Manager!123) shows sales data. Uses MVC, HashMaps, enums, and proper encapsulation.

This project simulates a restaurant from the perspective of a server.

In order to run the project, click run on LoginFrame (this class has the main method). From there, the GUI is loaded and the user can create account or log in. Then, once they
are authenticated, they are presented with a menu. This menu allows the server to seat a table, take and order, print the bill, pay a bill and see their tips.

We created a special log in for the Manager. They have a set username and password (for simplicity of showing what the manager is able to see). To run the program from the
perspective of the Manager, use username: "management" and password: "Manager!123".

DESIGN:
This program is split into Model, View, and Controller.
Data Structures/ Class Design: 
    -HashMap in UserDatabase that maps the usernames to User objects (allowing for fast retrieval or users when they log in)                                                                                   
    -Question ENUM for the security question (to avoid primitive obsession)
    -Table ENUM for the tables (to avoid primitve obsession)                                                                 
    -HashMap in Server that maps a Table to an ArrayList of Bills (this allows for easy look up of Tables for a Server)
    -ArrayList of closed tabs (an ArrayList is okay because we don't need look up, we always need to loop through it to get all needed data)
    -HashMap in Menu for all the drinks, appetizers, entrees, desserts, sides, and modifications and their costs (HashMap ensures look up is fast when taking orders)
    -HashMap in Bill that maps items to costs (for fast look up when we need all of a particular item)
Use of Interfaces:
    -In Management, we needed to sort the items two ways (by frequency count and by highest revenue items). To do this, we imported the Comparator interface and implemented
    the required compare()/ Collections.sort
Encapsulation:
    -When ever we use getters for the HashMaps or ArrayLists, we either return an unmodifiable Map or a copy. For the data structures that contain objects that are mutable
    we do a deep copy to ensure that there are no escaping references. For example, for getUser() in UserDatabse, we return a copy of User because Users are mutable. In
    getBillTable, which gets the Bills for the Table, we return an ArrayList that was a copy of Bills. 
