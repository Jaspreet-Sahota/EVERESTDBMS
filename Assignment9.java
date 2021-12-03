package assignment9;

import java.sql.*;
import java.util.*;

public class Assignment9 {
    public static void main(String[] args) {
        Connection conn1 = null;
        try {
            // registers Oracle JDBC driver - though this is no longer required
            // since JDBC 4.0, but added here for backward compatibility
            Class.forName("oracle.jdbc.OracleDriver");
            String dbURL1 = "jdbc:oracle:thin:username/password@oracle12c.scs.ryerson.ca:1521:orcl12c";
            /* This XE or local database that you installed on your laptop. 1521 is the default port for database, change according to what you used during installation. 
			orcl is the sid, change according to what you setup during installation. */
            conn1 = DriverManager.getConnection(dbURL1);
            if (conn1 != null) {
                System.out.println("Connected with connection #1");
            }
            String line = "";
            Scanner scan = new Scanner(System.in);
            Boolean exit = false;
            Boolean back = false;
            String table = "";
            String criteria = "";
            String[] total;
            while (!exit) {
                back = false;
                table = "";
                criteria = "";
                System.out.println("Main Menu:");
                System.out.println("1) Drop Tables");
                System.out.println("2) Create Tables");
                System.out.println("3) Populate Tables");
                System.out.println("4) Query Tables");
                System.out.println("5) Search Tables");
                System.out.println("6) Update Records");
                System.out.println("7) Delete Records");
                System.out.println("E) End/Exit");
                try {
                    line = scan.nextLine().toLowerCase();
                } catch (Exception e) {
                    //Catch force exit of program
                }
                if (line.length() == 1) {
                    String query = "";

                    switch (line) {
                        case "e":
                            exit = true;
                            break;
                        case "1":
                            //Add all drop args into query
                            total = new String[]{
                                "Drop Table categories Cascade Constraints",
                                "Drop Table review Cascade Constraints",
                                "Drop Table orders Cascade Constraints",
                                "Drop Table item Cascade Constraints",
                                "DROP TABLE warehouse CASCADE CONSTRAINTS",
                                "DROP TABLE warehouse_storage CASCADE CONSTRAINTS",
                                "DROP TABLE warehouse_department CASCADE CONSTRAINTS",
                                "DROP TABLE warehouse_department_records CASCADE CONSTRAINTS",
                                "Drop Table member Cascade Constraints"
                            };
                            for (String que : total) {//Create all the tables listed in the array
                                try (Statement stmt1 = conn1.createStatement()) {
                                    System.out.println(que);//DEBUGGING STATEMENT, REMOVE LATER
                                    stmt1.execute(que);
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            back = true;
                            break;
                        case "2":
                            //Add all CREATEs into query
                            total = new String[]{
                                "CREATE TABLE member(" + "Member_id VARCHAR2(12) NOT NULL," + " Username VARCHAR2(32) NOT NULL," + " Password VARCHAR2(32) NOT NULL,"
                                + " Name VARCHAR2(1000) DEFAULT 'None'," + " Address VARCHAR2(1000) DEFAULT 'None'," + " Credit_card_number VARCHAR2(16) DEFAULT '0000000000000000'"
                                + "  CHECK(length(Credit_card_number)=16)," + " CVV VARCHAR2(3) DEFAULT '000' CHECK(length(CVV)=3),"
                                + " Expiry_date VARCHAR2(5) DEFAULT '00/00' CHECK(length(Expiry_date)=5)," + " Phone_number VARCHAR2(10) NOT NULL CHECK (length(Phone_number)=10),"
                                + " Email VARCHAR2(64) NOT NULL," + " Vending_location VARCHAR2(1000) DEFAULT 'None',"
                                + " UNIQUE(Username, Phone_number, Email)," + " CONSTRAINT Member_pk PRIMARY KEY (Member_id))",
                                "CREATE TABLE warehouse ("
                                + "Warehouse_id VARCHAR2(12) NOT NULL PRIMARY KEY, "
                                + "Warehouse_address VARCHAR2(100) NOT NULL UNIQUE "
                                + ")",
                                "CREATE TABLE warehouse_storage("
                                + "Warehouse_id VARCHAR2(12) NOT NULL, "
                                + "Warehouse_available_storage INT DEFAULT 1000, "
                                + "CONSTRAINT ware_address FOREIGN KEY (Warehouse_id) REFERENCES warehouse(Warehouse_id)"
                                + ")",
                                "CREATE TABLE warehouse_department ("
                                + "Warehouse_id VARCHAR2(12) NOT NULL, "
                                + "Warehouse_department_id VARCHAR2(12) NOT NULL PRIMARY KEY, "
                                + "CONSTRAINT dep_id FOREIGN KEY (Warehouse_id) REFERENCES warehouse(Warehouse_id)"
                                + ")",
                                "CREATE TABLE warehouse_department_records ("
                                + "Warehouse_department_id VARCHAR2(12) NOT NULL, "
                                + "Warehouse_manager_id VARCHAR2(12) NOT NULL PRIMARY KEY,"
                                + "Warehouse_department_records VARCHAR2(100) NOT NULL,"
                                + "CONSTRAINT ware_dep FOREIGN KEY (Warehouse_department_id) REFERENCES warehouse_department(Warehouse_department_id)"
                                + ")",
                                "CREATE TABLE categories ( " + "Category_id VARCHAR2(12) NOT NULL PRIMARY KEY, "
                                + "Category_name VARCHAR2(30) NOT NULL UNIQUE, " + "Category_description VARCHAR2(100) NOT NULL)",
                                "CREATE TABLE item(" + "Item_id VARCHAR(12) NOT NULL, " + "Item_name VARCHAR2(128) NOT NULL, " + "Category_id VARCHAR2(12) NOT NULL References categories(Category_id), " + "Description VARCHAR2(1000) NOT NULL, "
                                + "Price NUMBER NOT NULL, " + "Stock NUMBER NOT NULL, " + "Location VARCHAR2(1000) NOT NULL, " + "Warehouse_id VARCHAR2(12) NOT NULL, "
                                + "CONSTRAINT Item_pk PRIMARY KEY (Item_id), " + "CONSTRAINT Item_fk FOREIGN KEY (Warehouse_id) REFERENCES warehouse(Warehouse_id))",
                                "CREATE TABLE orders(" + " Order_id VARCHAR2(12) NOT NULL PRIMARY KEY," + " Item_id VARCHAR2(12) NOT NULL,"
                                + " Member_id VARCHAR2(12) References member(Member_id) NOT NULL," + " CONSTRAINT Order_fk FOREIGN KEY (Item_id) REFERENCES item(Item_id))",
                                "CREATE TABLE review(" + " Review_id VARCHAR2(12) NOT NULL PRIMARY KEY," + " Member_id VARCHAR2(12) References member(Member_id) NOT NULL,"
                                + " Rating NUMBER(1) NOT NULL," + " Content VARCHAR2(1000) NOT NULL," + " Item_id VARCHAR2(12) NOT NULL," + " CONSTRAINT Review_fk FOREIGN KEY(Item_id) References item(Item_id))"

                            };
                            for (String que : total) {//Create all the tables listed in the array
                                try (Statement stmt1 = conn1.createStatement()) {
                                    System.out.println(que);//DEBUGGING STATEMENT, REMOVE LATER
                                    stmt1.execute(que);
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            back = true;
                            break;
                        case "3":
                            //Populate tables with test data
                            total = new String[]{
                                "Insert Into member(Member_id, Username, Password, Name, Address, Credit_card_number, CVV, Expiry_date, Phone_number, Email, Vending_location)"
                                + "    VALUES('000000000001','username1','11111111','RealName1','11 Dundas Ave','0345828428384281','441', '11/21', '4165555551', 'user1@gmail.com', '151 Montreal Blv')",
                                "Insert INTO member(Member_id, Username, Password, Name, Address, Credit_card_number, CVV, Expiry_date, Phone_number, Email)"
                                + "    VALUES('000000000002','username2','22222222','RealName2','12 Dundas Ave','0345828428384282','442', '11/22', '4165555552', 'user2@gmail.com')",
                                "Insert INTO member(Member_id, Username, Password, Name, Address, Credit_card_number, CVV, Expiry_date, Phone_number, Email, Vending_location)"
                                + "    VALUES('000000000003','username3','33333333','RealName3','13 Dundas Ave','0345828428384283','443', '11/23', '4165555553', 'user3@gmail.com', '153 Montreal Blv')",
                                "Insert INTO member(Member_id, Username, Password, Name, Address, Credit_card_number, CVV, Expiry_date, Phone_number, Email)"
                                + "    VALUES('000000000004','username4','44444444','RealName4','14 Dundas Ave','0345828428384284','444', '11/24', '4165555554', 'user4@gmail.com')",
                                "INSERT INTO warehouse(Warehouse_id, Warehouse_address)"
                                + "    VALUES('000000000003', '03 Toronto')",
                                "INSERT INTO warehouse(Warehouse_id, Warehouse_address)"
                                + "    VALUES('000000000001', '01 Atlantic')",
                                "INSERT INTO warehouse(Warehouse_id, Warehouse_address)"
                                + "    VALUES('000000000004', '03 Toronto2')",
                                "INSERT INTO warehouse(Warehouse_id, Warehouse_address)"
                                + "    VALUES('000000000002', '02 Scarborough')",
                                "INSERT INTO warehouse_storage(Warehouse_id, Warehouse_available_storage)"
                                + "    VALUES((SELECT Warehouse_id FROM warehouse where Warehouse_address='03 Toronto'), 892)",
                                "INSERT INTO warehouse_storage(Warehouse_id, Warehouse_available_storage)"
                                + "    VALUES((SELECT Warehouse_id FROM warehouse where Warehouse_address='01 Atlantic'), 997)",
                                "INSERT INTO warehouse_storage(Warehouse_id, Warehouse_available_storage)"
                                + "    VALUES((SELECT Warehouse_id FROM warehouse where Warehouse_address='03 Toronto2'), 882)",
                                "INSERT INTO warehouse_storage(Warehouse_id, Warehouse_available_storage)"
                                + "    VALUES((SELECT Warehouse_id FROM warehouse where Warehouse_address='02 Scarborough'), 932)",
                                "INSERT INTO warehouse_department(Warehouse_id, Warehouse_department_id)"
                                + "    VALUES('000000000003', '000000000003')",
                                "INSERT INTO warehouse_department(Warehouse_id, Warehouse_department_id)"
                                + "    VALUES('000000000001', '000000000023')",
                                "INSERT INTO warehouse_department(Warehouse_id, Warehouse_department_id)"
                                + "    VALUES('000000000004', '000000000011')",
                                "INSERT INTO warehouse_department(Warehouse_id, Warehouse_department_id)"
                                + "    VALUES('000000000002', '000000000012')",
                                "INSERT INTO warehouse_department_records(Warehouse_department_id, Warehouse_manager_id, Warehouse_department_records)"
                                + "    VALUES('000000000003', 'm-001', 'This department shows has ...etc ')",
                                "INSERT INTO warehouse_department_records(Warehouse_department_id, Warehouse_manager_id, Warehouse_department_records)"
                                + "    VALUES('000000000023', 'm-002', 'This department1 shows has ...etc ')",
                                "INSERT INTO warehouse_department_records(Warehouse_department_id, Warehouse_manager_id, Warehouse_department_records)"
                                + "    VALUES('000000000011', 'm-003', 'This department2 shows has ...etc ')",
                                "INSERT INTO warehouse_department_records(Warehouse_department_id, Warehouse_manager_id, Warehouse_department_records)"
                                + "    VALUES('000000000012', 'm-004', 'This department3 shows has ...etc ')",
                                "Insert INTO categories(Category_id, Category_name, Category_description)"
                                + "    VALUES('000000000001','food','vegetables')",
                                "Insert INTO categories(Category_id, Category_name, Category_description)"
                                + "    VALUES('000000000002','electric','eletronic devices')",
                                "Insert INTO categories(Category_id, Category_name, Category_description)"
                                + "    VALUES('000000000003','furniture','couches and sectionals and more')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000001','item1','000000000001','lots of words1', 1, 11,'null','000000000001')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000002','item2','000000000001','lots of words2', 2, 12,'null','000000000001')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000003','item3','000000000001','lots of words3', 3, 13,'null','000000000001')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000004','item4','000000000001','lots of words4', 4, 14,'null','000000000001')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000005','item5','000000000002','lots of words5', 5, 15,'null','000000000002')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000006','item6','000000000002','lots of words6', 6, 16,'null','000000000002')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000007','item7','000000000002','lots of words7', 7, 17,'null','000000000002')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000008','item8','000000000002','lots of words8', 8, 18,'null','000000000002')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000009','item9','000000000003','lots of words9', 9, 19,'null','000000000003')",
                                "Insert INTO item(Item_id, Item_name, Category_id, Description, Price, Stock, Location, Warehouse_id)"
                                + "    VALUES('000000000010','item10','000000000003','lots of words10', 10, 20,'null','000000000003')",
                                "Insert INTO orders(Order_id, Item_id, Member_id)"
                                + "    VALUES('00000000001',(SELECT Item_id FROM item where Item_id='000000000001'),(SELECT Member_id FROM member where Username='username1'))",
                                "Insert INTO orders(Order_id, Item_id, Member_id)"
                                + "    VALUES('00000000002',(SELECT Item_id FROM item where Item_id='000000000002'),(SELECT Member_id FROM member where Username='username2'))",
                                "Insert INTO orders(Order_id, Item_id, Member_id)"
                                + "    VALUES('00000000003',(SELECT Item_id FROM item where Item_id='000000000003'),(SELECT Member_id FROM member where Username='username3'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000001', '000000000004',1,'the most terrible product ever',(SELECT Item_id FROM item where Item_id='000000000003'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000002', '000000000001',2,'slightly terrible product.',(SELECT Item_id FROM item where Item_id='000000000003'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000003', '000000000003',3,'the most normal product ever',(SELECT Item_id FROM item where Item_id='000000000003'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000004', '000000000002',4,'almost the best product ever!',(SELECT Item_id FROM item where Item_id='000000000002'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000005', '000000000001',5,'the best product ever',(SELECT Item_id FROM item where Item_id='000000000001'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000006', '000000000004', 4, 'aaa', (SELECT Item_id FROM item WHERE Item_id='000000000001'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000007', '000000000004', 4, 'bbb', (SELECT Item_id FROM item WHERE Item_id='000000000002'))",
                                "Insert INTO review(Review_id, Member_id, Rating, Content, Item_id)"
                                + "    VALUES('000000000008', '000000000004', 4, 'ccc', (SELECT Item_id FROM item WHERE Item_id='000000000004'))"
                            };
                            for (String que : total) {//Insert dummy data
                                try (Statement stmt3 = conn1.createStatement()) {
                                    System.out.println(que);//DEBUGGING STATEMENT, REMOVE LATER
                                    stmt3.execute(que);
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            back = true;
                            break;
                        case "4":
                            //Query tables
                            total = new String[]{
                                "SELECT DISTINCT Member_id, Username, Name, Address, Phone_number, Email, Vending_location FROM member "
                                + " WHERE Credit_card_number != '000000000000' AND CVV != '000' AND Expiry_date != '00/00'"
                                + " AND Name != 'None' AND Address != 'None'" + " ORDER BY Member_id ASC ",
                                "SELECT DISTINCT Member_id, Username, Name, Address, Phone_number, Email, Vending_location FROM member WHERE Vending_location != 'None' "
                                + " AND Credit_card_number != '000000000000' AND CVV != '000' AND Expiry_date != '00/00'"
                                + " AND Name != 'None' AND Address != 'None'" + " ORDER BY Member_id ASC ",
                                "SELECT DISTINCT Warehouse_id as ID, Warehouse_address as \"Address\""
                                + " FROM warehouse" + " ORDER BY ID ASC",
                                "SELECT DISTINCT Warehouse_manager_id as MANAGER_ID, Warehouse_department_records as \"Records\""
                                + " FROM warehouse_department_records" + " ORDER BY MANAGER_ID ASC",
                                "SELECT DISTINCT Warehouse_available_storage as Storage, Warehouse_id as \"Id\""
                                + " FROM warehouse_storage" + " WHERE Warehouse_available_storage > 900" + " ORDER BY Storage ASC",
                                "SELECT DISTINCT Order_id as ID, Item_id, Member_id FROM orders"
                                + " ORDER BY Order_id, Member_id, Item_id ASC",
                                "SELECT DISTINCT Review_id, Member_id, Rating, Content, Item_id FROM review" + " ORDER BY Item_id, Member_id ASC",
                                "SELECT Category_id AS \"ID\", Category_name AS \"Category\", Category_description AS \"Description\""
                                + " FROM categories" + " ORDER BY ID ASC",
                                "SELECT Category_id, AVG(Price) as \"Price\", ROUND(VARIANCE(Price),2) as \"Varience\" "
                                + " FROM item " + " GROUP BY Category_id" + " ORDER BY Category_id ASC",
                                "SELECT item.Item_id as \"ITEM ID\", item.Price as \"Price\""
                                + " FROM item" + " GROUP BY item.Price, item.Item_id" + " ORDER BY Price ASC",
                                "SELECT sum(item.Price) as \"Total Item Prices\", warehouse.Warehouse_id"
                                + " FROM item, warehouse" + " WHERE warehouse.Warehouse_id = item.Warehouse_id"
                                + " GROUP BY warehouse.Warehouse_id" + " ORDER BY warehouse.Warehouse_id",
                                "SELECT item.Item_name as \"Item\", item.Stock as \"Stock in Warehouse\", warehouse.Warehouse_address as \"Address\""
                                + " FROM item, warehouse" + " WHERE warehouse.Warehouse_id = item.Warehouse_id" + " GROUP BY item.Item_name, item.Stock, warehouse.Warehouse_address"
                                + " HAVING MOD(item.Stock, 2) != 1 AND item.Stock > 12 " + " ORDER BY Stock ASC",
                                "SELECT sum(item.Stock) as \"Total Items\", warehouse.Warehouse_id"
                                + " FROM item, warehouse" + " WHERE warehouse.Warehouse_id = item.Warehouse_id"
                                + " GROUP BY warehouse.Warehouse_id" + " ORDER BY warehouse.Warehouse_id ASC"
                            };
                            for (String que : total) {//Queries
                                try (Statement stmt4 = conn1.createStatement()) {
                                    System.out.println(que);//DEBUGGING STATEMENT, REMOVE LATER
                                    ResultSet rs4 = stmt4.executeQuery(que);
                                    ResultSetMetaData rsmd4 = rs4.getMetaData();
                                    int column = rsmd4.getColumnCount();
                                    for (int i = 1; i <= column; i++) {
                                        if (i == 1) {
                                            System.out.print("|");
                                        }
                                        System.out.format("%-32s|", rsmd4.getColumnName(i));
                                    }
                                    System.out.println();
                                    while (rs4.next()) {
                                        for (int j = 1; j <= column; j++) {
                                            if (j == 1) {
                                                System.out.print("|");
                                            }
                                            System.out.format("%-32s|", rs4.getString(j));
                                        }
                                        System.out.println();
                                    }
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            back = true;
                            break;
                        case "5":
                            //Search tables based on a value
                            boolean search = false;
                            while (!search) {
                                System.out.println("What table will you search?:");
                                System.out.println("1) Member");
                                System.out.println("2) Warehouse");
                                System.out.println("3) Categories");
                                System.out.println("4) Item");
                                System.out.println("5) Orders");
                                System.out.println("6) Review");
                                System.out.println("7) warehouse_storage");
                                System.out.println("8) warehouse_department");
                                System.out.println("9) warehouse_department_records");
                                table = scan.nextLine().toLowerCase();
                                System.out.println("What will you use as your criteria?:");
                                String[] choices;
                                switch (table) {
                                    case "1"://member
                                        choices = new String[]{"Member_id", "Username", "Password", "Name", "Address", "Credit_card_number", "CVV", "Expiry_date", "Email", "Vending_location"};

                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "2":
                                        choices = new String[]{"Warehouse_id", "Warehouse_address"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-36s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "3":
                                        choices = new String[]{"Category_id", "Category_name", "Category_description"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-24s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "4":
                                        choices = new String[]{"Item_id", "Item_name", "Category_id", "Description", "Price", "Stock", "Location", "Warehouse_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-24s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "5":
                                        choices = new String[]{"Order_id", "Item_id", "Member_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "6":
                                        choices = new String[]{"Review_id", "Member_id", "Rating", "Content", "Item_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "7":
                                        choices = new String[]{"Warehouse_id", "Warehouse_available_storage"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "8":
                                        choices = new String[]{"Warehouse_id", "Warehouse_department_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                    case "9":
                                        choices = new String[]{"Warehouse_department_id", "Warehouse_manager_id", "Warehouse_department_records"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        search = true;
                                        break;
                                }

                            }
                            if (!back) {
                                System.out.println("Please enter your search value:");
                                String searchValue = scan.nextLine();
                                switch (table) {
                                    case "1":
                                        //Member
                                        query = "SELECT * FROM member WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "2":
                                        //Column name
                                        query = "SELECT * FROM warehouse WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "3":
                                        //Column name
                                        query = "SELECT * FROM categories WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "4":
                                        //Column name
                                        query = "SELECT * FROM item WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "5":
                                        //Column name
                                        query = "SELECT * FROM orders WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "6":
                                        //Column name
                                        query = "SELECT * FROM review WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "7":
                                        //Column name
                                        query = "SELECT * FROM Warehouse_storage WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "8":
                                        //Column name
                                        query = "SELECT * FROM warehouse_department WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                    case "9":
                                        //Column name
                                        query = "SELECT * FROM warehouse_department_records WHERE " + criteria + "= '" + searchValue + "'";
                                        break;
                                }
                                try (Statement stmt5 = conn1.createStatement()) {
                                    System.out.println(query);//DEBUGGING STATEMENT, REMOVE LATER
                                    ResultSet rs5 = stmt5.executeQuery(query);
                                    ResultSetMetaData rsmd5 = rs5.getMetaData();
                                    int column = rsmd5.getColumnCount();
                                    for (int i = 1; i <= column; i++) {
                                        if (i == 1) {
                                            System.out.print("|");
                                        }
                                        System.out.format("%-32s|", rsmd5.getColumnName(i));
                                    }
                                    System.out.println();
                                    while (rs5.next()) {
                                        for (int j = 1; j <= column; j++) {
                                            if (j == 1) {
                                                System.out.print("|");
                                            }
                                            System.out.format("%-32s|", rs5.getString(j));
                                        }
                                        System.out.println();
                                    }
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            break;
                        case "6":
                            //Update rows
                            //Search tables based on a value
                            boolean update = false;
                            while (!update) {
                                System.out.println("What table will you update?:");
                                System.out.println("1) Member");
                                System.out.println("2) Warehouse");
                                System.out.println("3) Categories");
                                System.out.println("4) Item");
                                System.out.println("5) Orders");
                                System.out.println("6) Review");
                                System.out.println("7) warehouse_storage");
                                System.out.println("8) warehouse_department");
                                System.out.println("9) warehouse_department_records");
                                table = scan.nextLine().toLowerCase();
                                System.out.println("What will you update?:");
                                String[] choices;
                                switch (table) {
                                    case "1"://member
                                        choices = new String[]{"Member_id", "Username", "Password", "Name", "Address", "Credit_card_number", "CVV", "Expiry_date", "Email", "Vending_location"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "2":
                                        choices = new String[]{"Warehouse_id", "Warehouse_address"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "3":
                                        choices = new String[]{"Category_id", "Category_name", "Category_description"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-24s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "4":
                                        choices = new String[]{"Item_id", "Item_name", "Category_id", "Description", "Price", "Stock", "Location", "Warehouse_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-24s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "5":
                                        choices = new String[]{"Order_id", "Item_id", "Member_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "6":
                                        choices = new String[]{"Review_id", "Member_id", "Rating", "Content", "Item_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-20s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "7":
                                        choices = new String[]{"Warehouse_id", "Warehouse_available_storage"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "8":
                                        choices = new String[]{"Warehouse_id", "Warehouse_department_id"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                    case "9":
                                        choices = new String[]{"Warehouse_department_id", "Warehouse_manager_id", "Warehouse_department_records"};
                                        while (!(criteria.equalsIgnoreCase("e") || Arrays.asList(choices).contains(criteria))) {//Get valid input
                                            System.out.println("Columns(Case sensitive):");
                                            System.out.print("|");
                                            for (String elem : choices) {//Print available categories
                                                System.out.format("%-30s", elem);
                                            }
                                            System.out.println("E to return to main menu.|");
                                            criteria = scan.nextLine();
                                        }
                                        if (criteria.equalsIgnoreCase("e")) {//Return to menu
                                            back = true;
                                        }
                                        update = true;
                                        break;
                                }
                            }
                            if (!back) {
                                System.out.println("Please enter your new value:");
                                String newValue = scan.nextLine();
                                System.out.println("Please enter your update clause(e.g. \"Name='RealName1'\":");
                                String clause = scan.nextLine();
                                switch (table) {
                                    case "1":
                                        //Member
                                        query = "UPDATE member Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "2":
                                        //Column name
                                        query = "Update warehouse Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "3":
                                        //Column name
                                        query = "Update categories Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "4":
                                        //Column name
                                        query = "Update item Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "5":
                                        //Column name
                                        query = "Update orders Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "6":
                                        //Column name
                                        query = "Update review Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "7":
                                        //Column name
                                        query = "Update warehouse_storage Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "8":
                                        //Column name
                                        query = "Update warehouse_department Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                    case "9":
                                        //Column name
                                        query = "Update warehouse_department_records Set " + criteria + "='" + newValue + "' WHERE " + clause;
                                        break;
                                }
                                try (Statement stmt6 = conn1.createStatement()) {
                                    System.out.println(query);//DEBUGGING STATEMENT, REMOVE LATER
                                    stmt6.executeQuery(query);
                                } catch (SQLException e) {
                                    //System.out.println("SQLError:"+e.getErrorCode());
                                    System.out.println(e);
                                }
                            }
                            break;
                        case "7":
                            Boolean delete = false;
                            while (!delete) {
                                System.out.println("What table will you delete from?:");
                                System.out.println("1) Member");
                                System.out.println("2) Warehouse");
                                System.out.println("3) Categories");
                                System.out.println("4) Item");
                                System.out.println("5) Orders");
                                System.out.println("6) Review");
                                System.out.println("7) warehouse_storage");
                                System.out.println("8) warehouse_department");
                                System.out.println("9) warehouse_department_records");
                                table = scan.nextLine().toLowerCase();
                                String[] valid = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                                if (table.equalsIgnoreCase("e")) {
                                    back = true;
                                    delete = true;
                                }
                                if (Arrays.asList(valid).contains(table)) {
                                    if (!back) {
                                        System.out.println("Please enter your delete clause(e.g. \"Name='RealName1'\":");
                                        String clause = scan.nextLine();
                                        switch (table) {
                                            case "1":
                                                //Member
                                                query = "Delete From member" + " WHERE " + clause;
                                                break;
                                            case "2":
                                                //Column name
                                                query = "Delete From warehouse" + " WHERE " + clause;
                                                break;
                                            case "3":
                                                //Column name
                                                query = "Delete From categories" + " WHERE " + clause;
                                                break;
                                            case "4":
                                                //Column name
                                                query = "Delete From item" + " WHERE " + clause;
                                                break;
                                            case "5":
                                                //Column name
                                                query = "Delete From orders" + " WHERE " + clause;
                                                break;
                                            case "6":
                                                //Column name
                                                query = "Delete From review" + " WHERE " + clause;
                                                break;
                                            case "7":
                                                //Column name
                                                query = "Delete From warehouse_storage" + " WHERE " + clause;
                                                break;
                                            case "8":
                                                //Column name
                                                query = "Delete From warehouse_department" + " WHERE " + clause;
                                                break;
                                            case "9":
                                                //Column name
                                                query = "Delete From warehouse_department_records" + " WHERE " + clause;
                                                break;
                                        }
                                        try (Statement stmt7 = conn1.createStatement()) {
                                            delete = true;
                                            System.out.println(query);//DEBUGGING STATEMENT, REMOVE LATER
                                            stmt7.executeQuery(query);
                                        } catch (SQLException e) {
                                            //System.out.println("SQLError:"+e.getErrorCode());
                                            System.out.println(e);
                                        }
                                    }
                                }
                            }
                            break;
                    }
                    if (!exit) {
                        System.out.println("Press Enter to continue:");
                        try {
                            scan.nextLine();
                        } catch (Exception e) {
                            //Catch Force exit of program
                        }
                        //Separate next actions
                        System.out.println("---------------------------------------");
                    }
                }
            }
            try {
                if (conn1 != null && !conn1.isClosed()) {
                    conn1.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
