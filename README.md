# Server-POS-GUI
## Description
This Java GUI application simulates a restaurant server's tasks. Run LoginFrame to start. Users can log in or create an account to seat tables, take orders, print/pay bills, and view tips. A special manager login (management / Manager!123) provides access to sales data and item statistics. The project follows the Model-View-Controller (MVC) design and makes use of HashMaps, enums, and encapsulation best practices.

## What I Learned: 

    - Principles of encapsulation and data protection
    
    - Designing and implementing graphical user interfaces (GUIs)
    
    - Collaborating effectively in an Agile team environment
    
    - Writing clean, maintainable, and well-structured code

## How to Run:

    - To run the project, execute the LoginFrame class (contains the main method). This will launch the GUI where users can 
    create an account or log in. Once authenticated, servers can:

        - Seat tables
        - Take orders
        - Print and pay bills
        - View tips

## To access the manager view, log in with:

    - Username: management
    - Password: Manager!123
    
    This account provides access to sales and item analytics.

## Design Overview:
    Architecture: 
        - The project follows the Model-View-Controller (MVC) design pattern for clean separation of concerns.

    Data Structures & Class DesignL
        - HashMap in UserDatabase maps usernames to User objects for fast login validation.
        - Question and Table enums reduce primitive obsession.
        - HashMap in Server maps Table to ArrayList<Bill> for quick table lookup.
        - Closed tabs are stored in an ArrayList, ideal for iteration.
        - HashMap in Menu stores items (e.g., drinks, entrees) and their prices for fast lookup.
        - HashMap in Bill maps item names to costs for efficient bill calculations.

    Use of Interfaces:
        - In the management view, we sort items by frequency and revenue using the Comparator interface with Collections.sort().

    Encapsulation:
        - All getters for internal collections return either unmodifiable views or deep copies to avoid leaking references. For example:
            - getUser() in UserDatabase returns a copy of the User object.
            - getBillTable() returns a copy of the ArrayList of bills.
        - Escaping references is used intentionally and safely in backend code. Since client code never interacts directly with 
        Server objects, and access is managed through Manager and RestaurantModelServer, controlled reference exposure allows the 
        manager to coordinate all server activity without compromising encapsulation.

## Future Changes:
    - Making it a Jar executable
    - GUI:
        - Instead of typing for a table to print the current/pay bill, take orders, etc. 
            - Make a map of all available tables that the server has, click the table, and then give options.
        - Make GUI more colorful
        - Design GUI to be a CVM Design Pattern.
    - Add real-time, multi-user support so multiple users can use the system simultaneously with synchronized updates.
    - Add more functionality to management
        
