import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;


public class WolfPub {
    private Connection con;

    public WolfPub(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                this.con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Helper functions
     */
    private void runStatement(String query) {
        try {
            Statement stmt = this.con.createStatement();

            ResultSet rs = stmt.executeQuery(query);
            PrintCursor printer = new PrintCursor(rs);
            printer.print();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void runPreparedStatement(String query, String... rest) {
        try {
            PreparedStatement stmt = this.con.prepareStatement(query);

            int counter = 1;
            for (String arg : rest) {
                stmt.setString(counter, arg);
                counter++;
            }

            ResultSet rs = stmt.executeQuery();
            PrintCursor printer = new PrintCursor(rs);
            printer.print();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Entering new publication (book, magazine or journal)
     *  This function inlcudes  TRANSACTIONS
     */
    public static void enterPublicationInfo(){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, uname, pass);
            String title, editor, edition, ISBN, periodicity,
                    dop, doi, pptext, url, ptype;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the title of the publication:");
            title = br.readLine();
            System.out.println("Please choose type of publication: " +
                    "\n1. Book\n" +
                    "\n2. Magazine\n" +
                    "\n3. Journal\n");
            ptype = br.readLine();

            if(ptype.equals("1")){
                System.out.println("Please enter the name of editor: ");
                editor = br.readLine();
                System.out.println("Please enter ISBN number: "); //integer or string?
                ISBN = br.readLine();
                System.out.println("Please enter edition: ");
                edition = br.readLine();
                System.out.println("Please enter publication date: ");
                dop = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();

                try{
                    connection.setAutoCommit(false); //set autocommit false

                    String pubIns = "INSERT INTO Publications(title, ptype, editor, dop, url) "
                            + "VALUES(?,?,?,?,?)";

                    PreparedStatement preparedStatement = connection.prepareStatement(pubIns);
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, "book");
                    preparedStatement.setString(3, editor);
                    preparedStatement.setString(4, dop);
                    preparedStatement.setString(5, url);
                    preparedStatement.executeUpdate(); //insert new publication into Publications

                    String bookIns = "INSERT INTO BOOKS (pid, ISBN, edition)"
                            + "VALUES((SELECT pid FROM Publications WHERE title = ? AND ptype = ? ), ?, ?)";
                    PreparedStatement addBook = connection.prepareStatement(bookIns);
                    addBook.setString(1, title);
                    addBook.setString(2, "book");
                    addBook.setString(3, ISBN);
                    addBook.setString(4, edition);
                    addBook.executeUpdate();

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
            else if (ptype.equals("2")) {
                System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter the periodicity: ");
                periodicity = br.readLine();
                System.out.println("Please enter text of magazine: ");
                pptext = br.readLine();
                System.out.println("Please enter date of issue: ");
                dop = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();

                try {
                    connection.setAutoCommit(false);
                    String pubIns = "INSERT INTO Publications(title, ptype, editor, dop, url) "
                            + "VALUES(?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(pubIns);
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, "magazine");
                    preparedStatement.setString(3, editor);
                    preparedStatement.setString(4, dop);
                    preparedStatement.setString(5, url);
                    preparedStatement.executeUpdate();

                    String magIns = "INSERT INTO periodicpublication((SELECT pid FROM Publications WHERE title = ? AND ptype = ? AND dop = ? ),"
                            + " periodicity, pptype, pptext, doi) VALUES(?,?,?,?)";
                    PreparedStatement preparedStat = connection.prepareStatement(magIns);
                    preparedStat.setString(1, title);
                    preparedStat.setString(2, "magazine");
                    preparedStat.setString(3, dop);
                    preparedStat.setString(4, periodicity);
                    preparedStat.setString(5, "magazine");
                    preparedStat.setString(6, pptext);
                    preparedStat.setString(7, dop);
                    preparedStat.executeUpdate();


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
            else if (ptype.equals("3")) {
                System.out.println("Please enter the name of editor: ");
                editor = br.readLine();
                System.out.println("Please enter the periodicity: ");
                periodicity = br.readLine();
                // pptype isn't it redundant? Because we have ptype in Publications
                System.out.println("Please enter text of magazine: ");
                pptext = br.readLine();
                System.out.println("Please enter date of issue: ");
                dop = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();

                try {
                    connection.setAutoCommit(false);
                    String pubIns = "INSERT INTO Publications(title, ptype, editor, dop, url) "
                            + "VALUES(?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(pubIns);
                    preparedStatement.setString(1, title);
                    preparedStatement.setString(2, "journal");
                    preparedStatement.setString(3, editor);
                    preparedStatement.setString(4, dop);
                    preparedStatement.setString(5, url);
                    preparedStatement.executeUpdate();

                    String magIns = "INSERT INTO periodicpublication( pid, periodicity, pptype, pptext, doi) "
                            + "VALUES((SELECT pid FROM Publications WHERE title = ? AND ptype = ? AND dop = ? ),?,?,?,?)";
                    PreparedStatement preparedStat = connection.prepareStatement(magIns);
                    preparedStat.setString(1, title);
                    preparedStat.setString(2, "journal");
                    preparedStat.setString(3, dop);
                    preparedStat.setString(4, periodicity);
                    preparedStat.setString(5, "journal");
                    preparedStat.setString(6, pptext);
                    preparedStat.setString(7, dop);
                    preparedStat.executeUpdate();


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

            else if (!ptype.equals("1")||!ptype.equals("2")||!ptype.equals("3")) {
                System.out.print("\nWrong number entered please try again!\n");
                enterPublicationInfo();
            }
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }

    }

    /**
     * This method is used to update information of Publication (book, journal, magazine)
     * To update publication just insert publication ID
     * Current Information of publication will be shown
     * Then type in updated information about publication
     * Or just copy result for results you don't want to change
     * This function includes TRANSACTIONS
     */

    public static void updatePublication() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, uname, pass);
            String pid, title, editor, edition, ISBN, periodicity,
                    dop, pptext, url, ptype;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the publication you want to update:");
            pid = br.readLine();

            //printReport(String.format("SELECT * FROM publications WHERE pid = '%s'", pid));
            String stmt = "SELECT * FROM publications WHERE pid = ?" ;
            PreparedStatement preparedSta = connection.prepareStatement(stmt);
            preparedSta.setString(1, pid);

            ResultSet rsp = preparedSta.executeQuery();
            ResultSetMetaData md = rsp.getMetaData();
            int colCount = md.getColumnCount();
            rsp.next();
            if (rsp.getString("ptype").equals("journal")||
                    rsp.getString("ptype").equals("magazine")) {
                String showPub = "SELECT * FROM publications NATURAL JOIN periodicpublication WHERE pid = ?";
                PreparedStatement stat = connection.prepareStatement(showPub);
                stat.setString(1, pid);
                stat.executeQuery();

                printResult(stat);
                System.out.println("\nPlease enter new title of the publication:");
                title = br.readLine();
                System.out.println("Please enter new name of editor: ");
                editor = br.readLine();
                System.out.println("Please enter new periodicity: ");
                periodicity = br.readLine();
                System.out.println("Please enter new text of magazine: ");
                pptext = br.readLine();
                System.out.println("Please enter new URL: ");
                url = br.readLine();
                try {
                    connection.setAutoCommit(false);
                    String pUp = "UPDATE publications SET ptype = ?, title = ?, editor = ?, url = ? WHERE pid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(pUp);
                    preparedStatement.setString(1, "journal");
                    preparedStatement.setString(2, title);
                    preparedStatement.setString(3, editor);
                    preparedStatement.setString(4, url);
                    preparedStatement.setString(5, pid);
                    preparedStatement.executeUpdate();

                    String ppUp = "UPDATE periodicpublication SET periodicity = ?, pptype = ?, pptext = ?, WHERE pid = ?";
                    PreparedStatement preparedStat = connection.prepareStatement(ppUp);
                    preparedStat.setString(1, periodicity);
                    preparedStat.setString(2, "magazine");
                    preparedStat.setString(3, pptext);
                    preparedStat.setString(4, pid);
                    preparedStat.executeUpdate();


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
            if (rsp.getString("ptype").equals("book")) {
                String showBook = "SELECT * FROM publications NATURAL JOIN Books WHERE pid = ?";
                PreparedStatement stat = connection.prepareStatement(showBook);
                stat.setString(1, pid);
                stat.executeQuery();
                printResult(stat);
                System.out.println("\nPlease enter new title of the publication:");
                title = br.readLine();
                System.out.println("Please enter new name of editor: ");
                editor = br.readLine();
                System.out.println("Please enter ISBN number: ");
                ISBN = br.readLine();
                System.out.println("Please enter new edition: ");
                edition = br.readLine();
                System.out.println("Please enter new publication date: ");
                dop = br.readLine();
                System.out.println("Please enter new URL: ");
                url = br.readLine();

                try {
                    connection.setAutoCommit(false);
                    String Up = "UPDATE publications SET ptype = ?, title = ?, editor = ?, url = ? WHERE pid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(Up);
                    preparedStatement.setString(1, "book");
                    preparedStatement.setString(2, title);
                    preparedStatement.setString(3, editor);
                    preparedStatement.setString(4, url);
                    preparedStatement.setString(5, pid);
                    preparedStatement.executeUpdate();

                    String bUp = "UPDATE books SET ISBN = ?, edition = ?, dop = ? WHERE pid = ?";
                    PreparedStatement prepared = connection.prepareStatement(bUp);
                    prepared.setString(1, ISBN);
                    prepared.setString(2, edition);
                    prepared.setString(3, dop);
                    prepared.setString(4, pid);
                    prepared.executeUpdate();


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


    public static void enterBookInfo() {
        //Inserts new rows into the Customers table based on what the user inputs
        try {
            //Stores the values in the appropriate variables
            String ISBN, edition;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter ISBN of the book: ");
            ISBN = br.readLine();
            System.out.println("Please enter edition of the Book: ");
            edition = br.readLine();

            try {
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate("INSERT INTO Books(ISBN, edition) " +
                        "VALUES ( " + "'" + ISBN + "', '" + edition + "'" + ");"); //inserts a row in the Book's table with the appropriate values
                connection.commit(); //commit the transaction if there is no error
                System.out.println("\nTransaction Success!"); //print error message
            } catch (SQLException sqlE) {
                //If error is found, the transaction is rolled back and the table is returned to its previous state
                System.out.print("Transaction is being rolled back.  An Error Occurred: ");
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set the autocommit to true
            }

        }
        //catches any errors that may occur and quits
        catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }
    /**
     * Assigning editor(s) to publication
     * This function has TRANSACTIONS
     */

    public static void assignEditor() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, uname, pass);
            String pid, sid;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the publication :");
            pid = br.readLine();
            System.out.println("Please enter editor's staff_id :");
            sid = br.readLine();

            try {
                connection.setAutoCommit(false);
                String assign = "INSERT INTO edit (pid, sid) VALUES(?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(assign);
                preparedStatement.setString(1, pid);
                preparedStatement.setString(2, sid);
                preparedStatement.executeUpdate();

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

        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }


    }
    /**
     * This function shows information to editor(s) on publications
     * he/she responsible for
     * This function has TRANSACTIONS
     */

    public static void showPublicationEditor() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, uname, pass);
            String sid;

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter staff_id :");
            sid = br.readLine();

            try {
                connection.setAutoCommit(false);
                String stmt = "SELECT * FROM publications NATURAL JOIN edit WHERE sid = ?" ;
                PreparedStatement prepared = connection.prepareStatement(stmt);
                prepared.setString(1, sid);

                ResultSet rsp = prepared.executeQuery();
                ResultSetMetaData md = rsp.getMetaData();
                rsp.next();
                if (rsp.getString("ptype").equals("journal")||
                        rsp.getString("ptype").equals("magazine")) {
                    String statement = "SELECT pid, ptype, title, editor, periodicity, pptext, doi, topic"
                            + " FROM publications NATURAL JOIN edit NATURAL JOIN periodicpublication"
                            + " NATURAL JOIN issue NATURAL JOIN hastopic WHERE sid = ?" ;
                    PreparedStatement prepareds = connection.prepareStatement(statement);
                    prepareds.setString(1, sid);
                    prepareds.executeQuery();
                    printResult(prepareds);

                }
                if (rsp.getString("ptype").equals("book")) {
                    String statement = "SELECT pid, ptype, title, editor, topic, edition, ISBN, dop"
                            + " FROM publications NATURAL JOIN edit NATURAL JOIN books"
                            + " NATURAL JOIN hastopic WHERE sid = ?" ;
                    PreparedStatement prepareds = connection.prepareStatement(statement);
                    prepareds.setString(1, sid);
                    prepareds.executeQuery();
                    printResult(prepareds);

                }



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

        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }
    /**
     * Entering new Article
     * This function contains TRANSACTIONS
     */

    public static void enterArticle() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, uname, pass);
            String atitle, atext, url;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter title of article :");
            atitle = br.readLine();
            System.out.println("Please enter text of article :");
            atext = br.readLine();
            System.out.println("Please enter url of article :");
            url = br.readLine();
            try{
                connection.setAutoCommit(false); //set autocommit false

                String artIns = "INSERT INTO Articles(atitle, atext, url) "
                        + "VALUES(?,?,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(artIns);
                preparedStatement.setString(1, atitle);
                preparedStatement.setString(2, atext);
                preparedStatement.setString(3, url);
                preparedStatement.executeUpdate(); //insert new article

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

        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }


    public static void updateBookInfo() {
        //Asks user to enter new information about Book
        try {
            String pid, ISBN, edition;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the ISBN of the book you want to update: ");
            System.out.println("Please enter publication ID:");
            pid = br.readLine();
            System.out.println("Please enter the ISBN of the book you want to update:");
            ISBN = br.readLine();
            System.out.println("Please enter the new edition of the Book: ");
            edition = br.readLine();

            try {
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate(String.format("UPDATE BOOKS SET ISBN='%s', edition ='%s' WHERE pid = '%s' ", ISBN, edition, pid));
                connection.commit(); //if there is no error commit the transaction
                System.out.println("\nTransaction Success!"); //print success message
            } catch (SQLException sqlE) {
                //If error is found, the transaction is rolled back and the table is returned to its previous state
                System.out.print("Transaction is being rolled back.  An Error Occurred: ");
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        }
        //catches any errors that may occur and quits
        catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
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

        } catch (Exception e) {
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

        } catch (Exception e) {
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

        } catch (Exception e) {
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

        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void enterEmployeePayment() {
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
                System.out.println("\nTransaction Success!");
            } catch {
                System.out.print("An error occurred: ");
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }


    }


    /**
     *
     */
    public static void enterDistributorInfo() {
        //Insert new tuples into Distributors table
        try {
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

            try {
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate("INSERT INTO Distributors(dname, dtype, city, address, contact, phno, tot_balance) " +
                        "VALUES (" + "'" + dname + "' ,'" + dtype + "', '" + city + "', "
                        + address + ", '" + contact + "', '" + phno + "', " + tot_balance + "');"); //insert a tuple into the distributors' table
                connection.commit(); //commit the transaction if there is no error
                System.out.println("\nTransaction Success!");
            } catch (SQLException sqlE) {
                System.out.print("An error occurred: ");
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        } catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }


    }

    public static void updateDistributorInfo() {
        try {
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

            try {
                connection.setAutoCommit(false); //set autocommit to false
                statement.executeUpdate(String.format("UPDATE Distributors SET dname='%s', dtype='%s', city='%s', address=%s, contact='%s', phno='%s', tot_balance=%s WHERE did=%s",
                        +dname, dtype, city, address, contact, phno, tot_balance, did));
                connection.commit(); //commit the transaction if there is no error
                System.out.println("\nTransaction Success!"); //print success message
            } catch (SQLException sqlE) {
                // If there is an error, the transaction is rolled back, so that the table is returned to the previous state
                System.out.print("An error occurred: ");
                System.out.println(sqlE); //print the error message
                connection.rollback(); //rollback the transaction
                connection.setAutoCommit(true); //set autocommit to true
            }

        } catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }

    }


    public static void mainsd(String[] args) {
        int action = -1;
        int staffAction = -1;
        //Initializes the entire database. Creates the appropriate tables, sequences, inserts, etc...
        initialize();

        //While the staff action isn't to quit the program do the following:
        while (action != 9) {
            action = WolfPubMenu.loginMenu();

            System.out.println("The choice is " + action);
            //if the manager is logged in, do the following:
            if (action == 1111) {
                //Manager logged in
                //While the Manager doesn't want to log out do the following
                while (staffAction != 8) {
                    staffAction = WolfPubMenu.menuManager();
                    //if action is 0 show all the books present
                    if (staffAction == 0) {
                        showAllBooks();
                    }
                    //if action is 8 show all the staff
                    else if (staffAction == 1) {
                        showAllStaff();
                    }


                }

            }
            //Author or Editor is logged in, do the following:
            else if (action == 2222) {
                //Author/Editor logged in
                //While the Author/Editor doesn't want to log out do the following
                while (staffAction != 8) {
                    staffAction = menu.menuAuthors();
                    //if action is 0 show all the books
                    if (staffAction == 0) {
                        showAllBooks();
                    }
                    //if action is 1 show all the books by a given author
                    else if (staffAction == 1) {
                        findBooksByAuthor();
                    }

                }
            } else if (action == 3333) {
                //Distributor logged in
                //While the Distributor doesn't want to log out do the following
                while (staffAction != 8) {
                    staffAction = AplusSystemMenu.menuDistributor();
                    //if action is 0 show all the books
                    if (staffAction == 0) {
                        showAllBooks();
                    }

                }
            } else if (action == 4444) {
                // Billing staff logged in
                //While the Billing Staff doesn't want to log out do the following
                while (staffAction != 8) {
                    staffAction = AplusSystemMenu.menuBilling();
                    //if action is 0 generate the billing for the customers
                    if (staffAction == 0) {
                        generateBillingDistributors();
                    }

                }
            }
            //if action is not 9 then let the user know they have not entered an acceptable ID
            else if (action != 9) {
                System.out.println("You have entered incorrect ID please try again \n");

            } else if (action == -1) {
                System.out.println("You have entered incorrect ID please try again \n");
            }

            staffAction = -1;

        } // end of while (action != 9)

    }


    /**
     * Report generation
     */
    public void getCopiesSoldByDistributor() {
        Statement stmt = this.con.createStatement();

    }

}
