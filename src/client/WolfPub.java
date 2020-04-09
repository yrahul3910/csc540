
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;


public class WolfPubInit {

    /**
     *Entering new publication (book, magazine or journal)
     */


    public static void enterPublicationInfo(){
        //Inserts new tuples into the Publications table
        try{
            String title, editor, topic, edition, ISBN number,
                    dop, doi, ptext,  url, price;
            try{
            String title, editor, topics, edition, ISBN number, periodicity,
                    dop, doc, doi, pptext, atext, url, price;
            int ptype;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the title of the publication:");
            title = br.readLine();

            System.out.println("Please choose type of publication: " +
                    "\n1. Book\n" +
                    "\n2. Magazine\n" +
                    "\n3. Journal\n" +
                    "\n4. Article\n");
            ptype = br.readLine();
                if(ptype = 1){
                    System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                    editor = br.readLine();                                                        // editor's name"??
                    System.out.println("Please enter topic of the publication: ");  // the same with topic cause we allow NULLs there
                    topics = br.readLine();
                    System.out.println("Please enter the name of author: "); /*When user chooses 1, new book has to be inserted into Publications
                                                                                and BOOKS tables*/
                    author = br.readLine();
                    System.out.println("Please enter ISBN number: "); //integer or string?
                    ISBN = br.readLine();
                    System.out.println("Please enter edition: ");
                    edition = br.readLine();
                    System.out.println("Please enter publication date: ");
                    dop = br.readLine();
                    System.out.println("Please enter URL: ");
                    url = br.readLine();
                    System.out.println("Please enter price: ");
                    price = br.readLine();

                    try{
                        connection.setAutoCommit(false); //set autocommit false
                        statement.executeUpdate("INSERT INTO Publications(title, ptype, topics, editor, dop, url" +
                                "price) "+
                                "VALUES (" +"'" + title + "' ,book, '" + topics + "','"
                                + editor + "'," + dop + ", '" + url + "', '" + price +  ")"); //insert new publication into Publications
                        statement.executeUpdate("INSERT INTO BOOKS(pid, ISBN, edition)" +
                                "VALUES (" + "(SELECT pid FROM Publications WHERE title = '"  + title + "'" + "AND ptype = book )"
                                + ", '" + ISBN + "', '" + edition + "'" + ")"); // Inserting new book into BOOKS.
                                                                                //should we do two seperate transactions for fetching pid?
                        connection.commit(); //commits the transaction to the database if no error has been detected
                        System.out.println( "\nTransaction Success!!" );
                    }
                    catch (SQLException sqlE) // the SQL was malformed
                    {
                        //If error is found, the transaction is rolled back and the table is returned to its previous state
                        System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                        System.out.println( sqlE.getMessage() ); // print SQL error message
                        connection.rollback(); //rollback transaction
                        connection.setAutoCommit(true); //reset autocommit to true
                    }


                }
                // if user wants enter magazine we have to insert it into Publications and PeriodicPublications tables
                // what about issue number? Let's just add it into Publications table
                else if (ptype = 2) {
                    System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                    editor = br.readLine();                                                        // editor's name"??
                    System.out.println("Please enter topic of the publication: ");  // the same with topic cause we allow NULLs there
                    topics = br.readLine();
                    System.out.println("Please enter the periodicity: ");
                    periodicity = br.readLine();
                    // pptype isn't it redundant? Because we have ptype in Publications
                    System.out.println("Please enter text of magazine: "); //integer or string?
                    pptext = br.readLine();
                    System.out.println("Please enter date of issue: ");
                    doi = br.readLine();
                    System.out.println("Please enter publication date: ");
                    dop = br.readLine();
                    System.out.println("Please enter URL: ");
                    url = br.readLine();
                    System.out.println("Please enter price: ");
                    price = br.readLine();

                    try {
                        connection.setAutoCommit(false);

                        statement.executeUpdate("INSERT INTO Publications(title, ptype, topics, editor, dop, url" +
                                "price) "+
                                "VALUES (" +"'" + title + "' ,magazine, '" + topics + "','"
                                + editor + "'," + dop + ", '" + url + "', '" + price +  ")");
                        statement.executeUpdate("INSERT INTO PeriodicPublication(pid, periodicity, ptype, pptext, doi)" +
                                "VALUES (" + "(SELECT pid FROM Publications WHERE title = '"  + title + "'" + "AND ptype = book )"
                                + ", '" + periodicity + "', '" + ptype + "', '" + pptext + "', '" + doi + "'" + ")");
                        connection.commit(); //commits the transaction to the database if no error has been detected
                        System.out.println( "\nTransaction Success!!" );
                    }
                    catch (SQLException sqlE) // the SQL was malformed
                    {
                        //If error is found, the transaction is rolled back and the table is returned to its previous state
                        System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                        System.out.println( sqlE.getMessage() ); // print SQL error message
                        connection.rollback(); //rollback transaction
                        connection.setAutoCommit(true); //reset autocommit to true
                    }


                }
                else if (ptype = 3) {
                    System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                    editor = br.readLine();                                                        // editor's name"??
                    System.out.println("Please enter topic of the publication: ");  // the same with topic cause we allow NULLs there
                    topics = br.readLine();
                    System.out.println("Please enter the periodicity: ");
                    periodicity = br.readLine();
                    // pptype isn't it redundant? Because we have ptype in Publications
                    System.out.println("Please enter text of magazine: "); //integer or string?
                    pptext = br.readLine();
                    System.out.println("Please enter date of issue: ");
                    doi = br.readLine();
                    System.out.println("Please enter publication date: ");
                    dop = br.readLine();
                    System.out.println("Please enter URL: ");
                    url = br.readLine();
                    System.out.println("Please enter price: ");
                    price = br.readLine();
                    try {
                        connection.setAutoCommit(false);

                        statement.executeUpdate("INSERT INTO Publications(title, ptype, topics, editor, dop, url" +
                                "price) "+
                                "VALUES (" +"'" + title + "' ,journal, '" + topics + "','"
                                + editor + "'," + dop + ", '" + url + "', '" + price +  ")");
                        statement.executeUpdate("INSERT INTO PeriodicPublication(pid, periodicity, ptype, pptext, doi)" +
                                "VALUES (" + "(SELECT pid FROM Publications WHERE title = '"  + title + "'" + "AND ptype = book )"
                                + ", '" + periodicity + "', '" + ptype + "', '" + pptext + "', '" + doi + "'" + ")");
                        connection.commit(); //commits the transaction to the database if no error has been detected
                        System.out.println( "\nTransaction Success!!" );
                    }
                    catch (SQLException sqlE) // the SQL was malformed
                    {
                        //If error is found, the transaction is rolled back and the table is returned to its previous state
                        System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                        System.out.println( sqlE.getMessage() ); // print SQL error message
                        connection.rollback(); //rollback transaction
                        connection.setAutoCommit(true); //reset autocommit to true
                    }

                }
                else if (ptype = 4){
                    System.out.println("Please enter date of creation of the article: "); // or press "/" if you don't know?
                    doc = br.readLine();
                    System.out.println("Please enter text of the article: ");
                    atext = br.readLine();
                    System.out.println("Please enter URL: ");
                    url = br.readLine();
                    try {
                        connection.setAutoCommit(false);

                        statement.executeUpdate("INSERT INTO Articles(atitle, doc, atext, url)"+
                                "VALUES (" +"'" + title + "', '" + doc + "', '"
                                + atext + "', '"  + url + "')");
                        connection.commit(); //commits the transaction to the database if no error has been detected
                        System.out.println( "\nTransaction Success!!" );
                    }
                    catch (SQLException sqlE) // the SQL was malformed
                    {
                        //If error is found, the transaction is rolled back and the table is returned to its previous state
                        System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                        System.out.println( sqlE.getMessage() ); // print SQL error message
                        connection.rollback(); //rollback transaction
                        connection.setAutoCommit(true); //reset autocommit to true
                    }
                }


        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }

    }


    public static void enterBookInfo(){
        //Inserts new rows into the Customers table based on what the user inputs
        try{
            //Stores the values in the appropriate variables
            String ISBN, edition;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter ISBN of the book: ");
            ISBN = br.readLine();
            System.out.println("Please enter edition of the Book: ");
            edition = br.readLine();

            try{
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate("INSERT INTO Books(ISBN, edition) "+
                        "VALUES ( " +"'" + ISBN + "', '"+ edition + "'" + ");"); //inserts a row in the Book's table with the appropriate values
                connection.commit(); //commit the transaction if there is no error
                System.out.println( "\nTransaction Success!" ); //print error message
            }

            catch (SQLException sqlE){
                //If error is found, the transaction is rolled back and the table is returned to its previous state
                System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                System.out.println( sqlE ); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set the autocommit to true
            }

        }
        //catches any errors that may occur and quits
        catch (Exception e)
        {
            System.out.println( "There was an error: " + e.getMessage() );
        }
    }

    public static void updatePublicationInfo(){
        //Asks user to enter new information about Publication
        try{
            String pid, ISBN, edition;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID:");
            pid= br.readLine();
            System.out.println("Please choose type of publication: " +
                    "\n1. Book\n" +
                    "\n2. Magazine\n" +
                    "\n3. Journal\n" +
                    "\n4. Article\n");
            ptype = br.readLine();

            if(ptype = 1) {
                System.out.println("Please enter new name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter new topic of the publication: ");  // the same with topic cause we allow NULLs there
                topics = br.readLine();
                System.out.println("Please enter new name of author: "); /*When user chooses 1, new book has to be inserted into Publications
                                                                                and BOOKS tables*/
                author = br.readLine();
                System.out.println("Please enter ISBN number: "); //integer or string?
                ISBN = br.readLine();
                System.out.println("Please enter new edition: ");
                edition = br.readLine();
                System.out.println("Please enter new publication date: ");
                dop = br.readLine();
                System.out.println("Please enter new URL: ");
                url = br.readLine();
                System.out.println("Please enter new price: ");
                price = br.readLine();

                try {
                    connection.setAutoCommit(false); //set autocommit false
                    statement.executeUpdate("UPDATE Publications SET title = '%s', ptype = book, topics = '%s', " +
                            "editor= '%s', dop = '%s', url = '%s', price= '%s') WHERE pid = = '%s'", title, topics, editor, dop, url, price, pid);
                    statement.executeUpdate("UPDATE BOOKS SET ISBN = '%s', edition = '%s' WHERE pid = '%s')", ISBN, edition, pid);
                    connection.commit(); //commits the transaction to the database if no error has been detected
                    System.out.println("\nTransaction Success!!");
                } catch (SQLException sqlE) // the SQL was malformed
                {
                    //If error is found, the transaction is rolled back and the table is returned to its previous state
                    System.out.print("Transaction is being rolled back.  An Error Occurred: ");
                    System.out.println(sqlE.getMessage()); // print SQL error message
                    connection.rollback(); //rollback transaction
                    connection.setAutoCommit(true); //reset autocommit to true
                }

            }

        }
        //catches any errors that may occur and quits
        catch (Exception e)
        {
            System.out.println( "There was an error: " + e.getMessage() );
        }
    }




    public static void updateBookInfo(){
        //Asks user to enter new information about Book
        try{
            String pid, ISBN, edition;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the ISBN of the book you want to update: ");
            System.out.println("Please enter publication ID:");
            pid= br.readLine();
            System.out.println("Please enter the ISBN of the book you want to update:");
            ISBN = br.readLine();
            System.out.println("Please enter the new edition of the Book: ");
            edition = br.readLine();

            try{
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate(String.format("UPDATE BOOKS SET ISBN='%s', edition ='%s' WHERE pid = '%s' ", ISBN, edition, pid));
                connection.commit(); //if there is no error commit the transaction
                System.out.println( "\nTransaction Success!" ); //print success message
            }
            catch (SQLException sqlE){
                //If error is found, the transaction is rolled back and the table is returned to its previous state
                System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                System.out.println( sqlE ); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        }
        //catches any errors that may occur and quits
        catch (Exception e)
        {
            System.out.println( "There was an error: " + e.getMessage() );
        }
    }

    /**
     * Search for books written by particular author
     */
    public static void findBooksByAuthor() {
        String title, ISBN, edition, sname;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter full name of the author: ");
            sname = br.readLine();
            result = statement.executeQuery("SELECT title, ISBN, edition FROM Books NATURAL JOIN Publications " +
                    "NATURAL JOIN Staff NATURAL JOIN WriteBook WHERE sname = '" + sname + "'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for books published on particular date
     */
    public static void findBooksByDate() {
        String title, ISBN, edition, dop;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter book's date of publishing");
            System.out.println("The date should be in the form YYYY-MM-DD (i.e. 2020-02-02) ");
            dop = br.readLine();
            result = statement.executeQuery("SELECT title, edition, ISBN FROM Books NATURAL JOIN Publications " +
                    "WHERE dop = '" + dop + "'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for books by particular topics
     */
    public static void findBooksByTopic() {
        String title, edition, ISBN, topics;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter the topic of the book: ");
            topics = br.readLine();
            result = statement.executeQuery("SELECT title, edition, ISBN FROM Books NATURAL JOIN Publications " +
                    "WHERE topics LIKE '%" + topics + "%'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Search for articles written by particular author
     */
    public static void findArticleByAuthor() {
        String sname, atitle, atext;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter full name of the author: ");
            sname = br.readLine();
            result = statement.executeQuery("SELECT atitle, atext FROM Articles NATURAL JOIN Staff " +
                    "NATURAL JOIN WriteArticle WHERE sname = '" + sname + "'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for articles published on particular date
     */
    public static void findArticleByDate() {
        String atitle, atext, dop;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter article's date of publishing");
            System.out.println("The date should be in the form YYYY-MM-DD (i.e. 2020-02-02) ");
            dop = br.readLine();
            result = statement.executeQuery("SELECT atitle, atext FROM Articles NATURAL JOIN ContainArticle " +
                    "NATURAL JOIN (SELECT pid, dop FROM Publications) as Publication WHERE dop = '" + dop + "'");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for articles by particular topics
     */
    public static void findArticleByAuthor() {
        String atopics, atitle, atext;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter the topic of the article: ");
            atopics = br.readLine();
            result = statement.executeQuery("SELECT atitle, atext" +
                    "FROM Articles WHERE atopics LIKE '%" + atopics + "%';");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void enterEmployeePayment(){
        try {
            int sid;
            String paycheck, paydate;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter staff id: ");
            sid = br.readLine();
            System.out.println("Please enter paycheck amount: ");
            paycheck = br.readLine();
            System.out.println("Please enter date of payment: ");
            sid = br.readLine();

            try {
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate("INSERT INTO Payments (sid, paycheck, paydate)" +
                        "VALUES (" + "'" + sid + "', '" + paycheck + "', '" + paydate + "');");
                connection.commit(); //commit the transaction if there is no error
                System.out.println( "\nTransaction Success!" );
            }
            catch {
                System.out.print( "An error occurred: " );
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        }
        catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }


    }


    /**
     *
     *
     */
    public static void enterDistributorInfo(){
        //Insert new tuples into Distributors table
        try{
            String dname, dtype, city, address, contact, phno, tot_balance;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the distributor's name:");
            dname = br.readLine();
            System.out.println("Please enter the distributor's type (wholesale/bookstore/library):");
            dtype = br.readLine();
            System.out.println("Please enter the distributor's city:");
            city = br.readLine();
            System.out.println("Please enter the distributor's address:");
            address = br.readLine();
            System.out.println("Please enter the distributor's contact (full name):");
            contact = br.readLine();
            System.out.println("Please enter the distributor's phone number (111-111-1111):");
            phno = br.readLine();
            System.out.println("Please enter the distributor's total balance:");
            tot_balance = br.readLine();

            try{
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate("INSERT INTO Distributors(dname, dtype, city, address, contact, phno, tot_balance) "+
                        "VALUES (" + "'" + dname + "' ,'"+ dtype +  "', '" + city + "', "
                        + address + ", '"  + contact + "', '" + phno + "', " + tot_balance + "');"); //insert a tuple into the distributors' table
                connection.commit(); //commit the transaction if there is no error
                System.out.println( "\nTransaction Success!" );
            }
            catch(SQLException sqlE){
                System.out.print( "An error occurred: " );
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        }
        catch(Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }


    }

    public static void updateDistributorInfo(){
        try{
            String did, dname, dtype, city, address, contact, phno, tot_balance;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the distributor ID you want to update:");
            did = br.readLine();

            System.out.println("Please enter the new distributor's name:");
            dname = br.readLine();
            System.out.println("Please enter the new type of the distributor (wholesale/bookstore/library):");
            dtype = br.readLine();
            System.out.println("Please enter the new city of the distributor:");
            city = br.readLine();
            System.out.println("Please enter the new address of the distributor:");
            address = br.readLine();
            System.out.println("Please enter the new contact of the distributor (full name):");
            contact = br.readLine();
            System.out.println("Please enter the distributor's new phone number (111-111-1111):");
            phno = br.readLine();
            System.out.println("Please enter the distributor's total balance:");
            tot_balance = br.readLine();

            try{
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate(String.format("UPDATE Distributors SET dname='%s', dtype='%s', city='%s', address=%s, contact='%s', phno='%s', tot_balance=%s WHERE did=%s",
                        + dname, dtype, city, address, contact, phno, tot_balance, did));
                connection.commit(); //commit the transaction if there is no error
                System.out.println( "\nTransaction Success!" ); //print success message
            }
            catch(SQLException sqlE){
                // If there is an error, the transaction is rolled back, so that the table is returned to the previous state
                System.out.print( "An error occurred: " );
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        }

        catch (Exception e)
        {
            System.out.println( "There is an error: " + e.getMessage() );
        }

    }




    public static void main(String[] args) {
        int action = -1;
        int staffAction = -1;
        //Initializes the entire database. Creates the appropriate tables, sequences, inserts, etc...
        initialize();

        //While the staff action isn't to quit the program do the following:
        while (action != 9) {
            action = WolfPubMenu.loginMenu();

            System.out.println("The choice is "+ action);
            //if the manager is logged in, do the following:
            if (action == 1111) {
                //Manager logged in
                //While the Manager doesn't want to log out do the following
                while (staffAction != 8){
                    staffAction = WolfPubMenu.menuManager();
                    //if action is 0 show all the books present
                    if (staffAction == 0){
                        showAllBooks();
                    }
                    //if action is 8 show all the staff
                    else if(staffAction == 1){
                        showAllStaff();
                    }


                }

            }
            //Author or Editor is logged in, do the following:
            else if (action == 2222) {
                //Author/Editor logged in
                //While the Author/Editor doesn't want to log out do the following
                while (staffAction != 8){
                    staffAction = menu.menuAuthors();
                    //if action is 0 show all the books
                    if (staffAction == 0){
                        showAllBooks();
                    }
                    //if action is 1 show all the books by a given author
                    else if (staffAction == 1){
                        findBooksByAuthor();
                    }

                }
            }
            else if (action == 3333){
                //Distributor logged in
                //While the Distributor doesn't want to log out do the following
                while (staffAction != 8){
                    staffAction = AplusSystemMenu.menuDistributor();
                    //if action is 0 show all the books
                    if (staffAction == 0){
                        showAllBooks();
                    }

                }
            }
            else if (action == 4444){
                // Billing staff logged in
                //While the Billing Staff doesn't want to log out do the following
                while (staffAction != 8){
                    staffAction = AplusSystemMenu.menuBilling();
                    //if action is 0 generate the billing for the customers
                    if (staffAction == 0){
                        generateBillingDistributors();
                    }

                }
            }
            //if action is not 9 then let the user know they have not entered an acceptable ID
            else if (action != 9){
                System.out.println("You have entered incorrect ID please try again \n");

            } else if (action == -1) {
                System.out.println("You have entered incorrect ID please try again \n");
            }

            staffAction = -1;

        } // end of while (action != 9)

    }





}