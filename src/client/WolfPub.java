import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;


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
            System.out.println(ex.getMessage());
        }
    }


    /**
     * Helper functions
     */
    private void runStatement(boolean update, String query) {
        try {
            Statement stmt = this.con.createStatement();

            if (update) {
                stmt.executeUpdate(query);
            } else {
                ResultSet rs = stmt.executeQuery(query);
                PrintCursor printer = new PrintCursor(rs);
                printer.print();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            try {
                if (!this.con.getAutoCommit())
                    this.con.rollback();
            } catch (SQLException e) {
                System.out.println("All hope has evaded us. We could not even successfully rollback :(");
                System.out.println(e.getMessage());
            }
        }
    }

    private void runPreparedStatement(boolean update, String query, String... rest) {
        try {
            PreparedStatement stmt = this.con.prepareStatement(query);

            int counter = 1;
            for (String arg : rest) {
                stmt.setString(counter, arg);
                counter++;
            }

            if (update)
                stmt.executeUpdate();
            else {
                ResultSet rs = stmt.executeQuery();
                PrintCursor printer = new PrintCursor(rs);
                printer.print();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

            try {
                if (!this.con.getAutoCommit())
                    this.con.rollback();
            } catch (SQLException e) {
                System.out.println("All hope has evaded us. We could not even successfully rollback :(");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Function for executing Prepared Statement and printing the results in clear manner
     */
    public void printResult(PreparedStatement ps) {
        try {
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); //getting metadata object
            int colCount = md.getColumnCount(); //getting the number of columns in the result set
            rs.next();
            for (int i = 1; i <= colCount; i++) { //iterating over columns
                String col_name = md.getColumnName(i);
                System.out.printf("%s : %s\n", col_name, rs.getString(col_name));
            }
        } catch (SQLException e) {
            System.out.println("The system encountered an error: " + e.getMessage());
        }
    }
    //************************* EDITING AND PUBLISHING *****************************************

    /**
     * Add topic to a publication
     */
    public void addTopic() {
        System.out.print("Enter the publication ID: ");
        Scanner scanner = new Scanner(System.in);
        String pid = scanner.nextLine();

        System.out.print("Enter the topic: ");
        String topic = scanner.nextLine();

        try {
            Statement stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT topic FROM Topics WHERE topic = \"" + topic + "\";");
            if (!rs.next()) {
                PreparedStatement ps = this.con.prepareStatement("INSERT INTO Topics VALUES (?)");
                ps.setString(1, topic);

                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }

        final String query = "INSERT INTO HasTopic VALUES (?, ?);";
        runPreparedStatement(true, query, pid, topic);
    }

    /**
     * Entering new publication (book, magazine or journal)
     * This function includes  TRANSACTIONS
     */
    public void enterPublicationInfo(){

        String title, editor, edition, ISBN, periodicity,
                dop, doi, pptext, url, ptype;
        try{

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter the title of the publication:");
            title = br.readLine();
            System.out.println("Please choose type of publication: " +
                    "\n1. Book\n" +
                    "\n2. Magazine\n" +
                    "\n3. Journal\n");
            ptype = br.readLine();

            if(ptype.equals("1")){
                System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter ISBN number: "); //integer or string?
                ISBN = br.readLine();
                System.out.println("Please enter edition: ");
                edition = br.readLine();
                System.out.println("Please enter publication date: ");
                System.out.println("The date should be in the form YYYY-MM-DDv(i.e. 2020-01-23: ");
                dop = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();

                this.con.setAutoCommit(false); //set autocommit false

                String pubIns = "INSERT INTO Publications(title, ptype, editor, url) "
                        + "VALUES(?,?,?,?)";
                runPreparedStatement(true, pubIns, title, "book", editor, url);


                String bookIns = "INSERT INTO BOOKS (pid, ISBN, edition, dop)"
                        + "VALUES((SELECT MAX(pid) FROM Publications), ?, ?, ?)";

                runPreparedStatement(true, bookIns, ISBN, edition, dop);
                
                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );
            }




            // if user wants enter magazine we have to insert it into Publications and PeriodicPublications tables
            // what about issue number? Let's just add it into Publications table
            else if (ptype.equals("2")) {
                System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter the periodicity: ");
                periodicity = br.readLine();
                System.out.println("Please enter text of magazine: "); //integer or string?
                pptext = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();


                this.con.setAutoCommit(false);
                String pubInse = "INSERT INTO Publications(title, ptype, editor, url) "
                        + "VALUES(?,?,?,?)";
                runPreparedStatement(true, pubInse, title, "magazine", editor, url);


                String magIns = "INSERT INTO periodicpublication( pid, periodicity, pptext) "
                        + "VALUES((SELECT MAX(pid) FROM Publications),?,?)";
                runPreparedStatement(true, magIns, periodicity, pptext);


                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );



            }
            else if (ptype.equals("3")) {
                System.out.println("Please enter the name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter the periodicity: ");
                periodicity = br.readLine();
                System.out.println("Please enter text of journal: ");
                pptext = br.readLine();
                System.out.println("Please enter URL: ");
                url = br.readLine();


                this.con.setAutoCommit(false); // where to make true now?
                String pubInse = "INSERT INTO Publications(title, ptype, editor, url) "
                        + "VALUES(?,?,?,?)";
                runPreparedStatement(true, pubInse, title, "magazine", editor, url);

                String jourIns = "INSERT INTO periodicpublication( pid, periodicity, pptext) "
                        + "VALUES((SELECT MAX(pid) FROM Publications),?,?)";
                runPreparedStatement(true, jourIns, periodicity, pptext);

                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );
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

    public void updatePublication() {
        String pid, title, editor, edition, ISBN, periodicity,
                dop, pptext, url, ptype;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the publication you want to update:");
            pid = br.readLine();

            String stmt = "SELECT * FROM publications WHERE pid = ?" ;
            PreparedStatement preparedSta = this.con.prepareStatement(stmt);
            preparedSta.setString(1, pid);

            ResultSet rsp = preparedSta.executeQuery();
            rsp.next();
            if (rsp.getString("ptype").equals("journal")||
                    rsp.getString("ptype").equals("magazine")) {
                String showPub = "SELECT * FROM publications NATURAL JOIN periodicpublication WHERE pid = ?";
                PreparedStatement stat = this.con.prepareStatement(showPub);
                stat.setString(1, pid);
                stat.executeQuery();
                System.out.println("\nCurrent Information of Publication: \n");
                printResult(stat);
                System.out.println("\nPlease enter new title of the publication:");
                title = br.readLine();
                System.out.println("Please enter new name of editor: ");
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter new periodicity: ");
                periodicity = br.readLine();
                System.out.println("Please enter new text of magazine: "); //integer or string?
                pptext = br.readLine();
                System.out.println("Please enter new URL: ");
                url = br.readLine();

                this.con.setAutoCommit(false);
                String pUp = "UPDATE publications SET ptype = ?, title = ?, editor = ?, url = ? WHERE pid = ?";

                runPreparedStatement(true, pUp, "journal", title, editor, url, pid);

                String ppUp = "UPDATE periodicpublication SET periodicity = ?, pptype = ?, pptext = ?, WHERE pid = ?";

                runPreparedStatement(true, ppUp, periodicity, "magazine", pptext, pid);

                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );


            }
            if (rsp.getString("ptype").equals("book")) {
                String showBook = "SELECT * FROM publications NATURAL JOIN Books WHERE pid = ?";
                PreparedStatement stat = this.con.prepareStatement(showBook);
                stat.setString(1, pid);
                stat.executeQuery();
                printResult(stat);
                System.out.println("\nPlease enter new title of the publication:");
                title = br.readLine();
                System.out.println("Please enter new name of editor: "); //maybe we add here command "press "/" if you don't
                editor = br.readLine();                                                        // editor's name"??
                System.out.println("Please enter ISBN number: "); //integer or string?
                ISBN = br.readLine();
                System.out.println("Please enter new edition: ");
                edition = br.readLine();
                System.out.println("Please enter new publication date: ");
                dop = br.readLine();
                System.out.println("Please enter new URL: ");
                url = br.readLine();

                this.con.setAutoCommit(false);
                String Up = "UPDATE publications SET ptype = ?, title = ?, editor = ?, url = ? WHERE pid = ?";

                runPreparedStatement(true, Up, "book", title, editor, url, pid);

                String bUp = "UPDATE books SET ISBN = ?, edition = ?, dop = ? WHERE pid = ?";
                runPreparedStatement(true, bUp, ISBN, edition, dop, pid);

                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );
            }
        }

        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }
    /**
     * Delete publication
     * This function has TRANSACTIONS
     */

    public void deletePublication() {
        String pid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the book:");
            pid = br.readLine();

            this.con.setAutoCommit(false); //set autocommit false

            String prepareds = "DELETE FROM publications WHERE pid = ? ";
            runPreparedStatement(true, prepareds, pid);

            this.con.commit(); //commits the transaction to the database if no error has been detected
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }

    /**
     * Assigning editor(s) to publication
     * This function has TRANSACTIONS
     */

    public void assignEditor() {

        String pid, sid;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the publication :");
            pid = br.readLine();
            System.out.println("Please enter editor's staff_id :");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String assign = "INSERT INTO edit (pid, sid) VALUES(?,?)";

            runPreparedStatement(true, assign, pid, sid);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
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

    public void showPublicationEditor() {
        String sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter staff_id :");
            sid = br.readLine();

            try {
                this.con.setAutoCommit(false);
                String stmt = "SELECT * FROM publications NATURAL JOIN edit WHERE sid = ?" ;
                PreparedStatement prepared = this.con.prepareStatement(stmt);
                prepared.setString(1, sid);

                ResultSet rsp = prepared.executeQuery();
                ResultSetMetaData md = rsp.getMetaData();
                rsp.next();
                if (rsp.getString("ptype").equals("journal")||
                        rsp.getString("ptype").equals("magazine")) {
                    String statement = "SELECT pid, ptype, title, editor, periodicity, pptext, doi, topic"
                            + " FROM publications NATURAL JOIN edit NATURAL JOIN periodicpublication"
                            + " NATURAL JOIN issue NATURAL JOIN hastopic WHERE sid = ?" ;
                    PreparedStatement prepareds = this.con.prepareStatement(statement);
                    prepareds.setString(1, sid);
                    prepareds.executeQuery();

                    printResult(prepareds);

                }
                if (rsp.getString("ptype").equals("book")) {
                    String statement = "SELECT pid, ptype, title, editor, topic, edition, ISBN, dop"
                            + " FROM publications NATURAL JOIN edit NATURAL JOIN books"
                            + " NATURAL JOIN hastopic WHERE sid = ?" ;
                    PreparedStatement prepareds = this.con.prepareStatement(statement);
                    prepareds.setString(1, sid);
                    prepareds.executeQuery();

                    printResult(prepareds);

                }
                this.con.commit(); //commits the transaction to the database if no error has been detected
                System.out.println( "\nTransaction Success!!" );
            }
            catch (SQLException sqlE) // the SQL was malformed
            {
                //If error is found, the transaction is rolled back and the table is returned to its previous state
                System.out.print( "Transaction is being rolled back.  An Error Occurred: " );
                System.out.println( sqlE.getMessage() ); // print SQL error message
                this.con.rollback(); //rollback transaction
                this.con.setAutoCommit(true); //reset autocommit to true
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

    public void enterArticle() {
        String atitle, atext, url;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter title of article :");
            atitle = br.readLine();
            System.out.println("Please enter text of article :");
            atext = br.readLine();
            System.out.println("Please enter url of article :");
            url = br.readLine();

            this.con.setAutoCommit(false); //set autocommit false

            String artIns = "INSERT INTO Articles(atitle, atext, url) "
                    + "VALUES(?,?,?)";
            runPreparedStatement(true, artIns, atitle, atext, url);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }

    /**
     * Adding Article int periodic publication
     * This function contains TRANSACTIONS
     */

    public void addArticle() {
        String aid, pid, doi;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter article ID:");
            aid = br.readLine();
            System.out.println("Please enter publication ID:");
            pid = br.readLine();
            System.out.println("Please enter date of issue of publication:");
            doi = br.readLine();

            this.con.setAutoCommit(false); //set autocommit false

            String artadd = "INSERT INTO containarticle(pid, aid, doi) "
                    + "VALUES(?,?,?)";
            runPreparedStatement(true, artadd, pid, aid, doi);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }
    /**
     * Deleting Article
     * This function contains TRANSACTIONS
     */
    public void deleteArticle() {
        String aid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter article ID you want to delete:");
            aid = br.readLine();

            this.con.setAutoCommit(false); //set autocommit false

            String delart = "DELETE FROM containarticle WHERE aid = ? ";

            runPreparedStatement(true, delart, aid);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }

    /**
     * Adding chapter for books
     * This function contains TRANSACTIONS
     */
    public void addChapter() {
        String pid, chno, chtitle, chtext, url;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the book:");
            pid = br.readLine();
            System.out.println("Please enter chapter number you want to add:");
            chno = br.readLine();
            System.out.println("Please enter chapter title:");
            chtitle = br.readLine();
            System.out.println("Please enter chapter text:");
            chtext = br.readLine();
            System.out.println("Please enter chapter url:");
            url = br.readLine();
            this.con.setAutoCommit(false); //set autocommit false

            String addch = "INSERT INTO chapters(pid, chno, chtitle, chtext, url)"
                    + "VALUES(?,?,?,?,?) ";

            runPreparedStatement(true, addch, pid, chno, chtitle, chtext, url);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }
    /**
     *Deleting chapter from Books
     * This function contains TRANSACTIONS
     */
    public void deleteChapter() {
        String pid, chno;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter publication ID of the book:");
            pid = br.readLine();
            System.out.println("Please enter chapter number you want to delete:");
            chno = br.readLine();
            this.con.setAutoCommit(false); //set autocommit false

            String delch = "DELETE FROM chapters WHERE pid = ? AND chno = ?";

            runPreparedStatement(true, delch, pid, chno);

            this.con.commit(); //commits the transaction to the database if no error has been detected
            System.out.println( "\nTransaction Success!!" );
        }
        catch (Exception e) {
            System.out.println("There is an error: " + e.getMessage());
        }
    }


//********************************** PRODUCTION ***********************************************************

    /**
     * Enter new Issue of Periodic Publication
     * This function includes TRANSACTION
     */
    public void enterIssue() {
        String pid, doi;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID: ");
            pid = br.readLine();
            System.out.println("Please enter date of this issue: ");
            doi = br.readLine();

            this.con.setAutoCommit(false);
            String issueIns = "INSERT INTO Issue (pid, doi) VALUES (?, ?);";
            runPreparedStatement(true, issueIns, pid, doi);

            this.con.commit();
            System.out.println("\nTransaction Success!");
        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    /**
     * Update date of Issue of Periodic Publication
     * This function includes TRANSACTION
     */
    public void updateIssue() {
        String pid, doi1, doi2;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID: ");
            pid = br.readLine();
            System.out.println("Please enter date of issue you want to update: ");
            doi1 = br.readLine();
            System.out.println("Please enter new date of issue: ");
            doi2 = br.readLine();

            this.con.setAutoCommit(false);
            String issueIns = "UPDATE Issue SET doi = ? WHERE pid = ? and doi = ?;";

            runPreparedStatement(true, issueIns, doi2, pid, doi1);

            this.con.commit();
            System.out.println("\nTransaction Success!");
        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }


    /**
     * Delete particular Issue of Periodic Publication
     * This function includes TRANSACTION
     */
    public void deleteIssue() {
        String pid, doi;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID: ");
            pid = br.readLine();
            System.out.println("Please enter date of the issue you want to delete: ");
            doi = br.readLine();


            this.con.setAutoCommit(false);
            String preparedQuery = "DELETE FROM Issue WHERE pid = ? and doi = ?;";
            runPreparedStatement(true, preparedQuery, pid, doi);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }


    /**
     * Update Article's title
     * This function includes TRANSACTION
     */
    public void UpdateArticleTitle() {
        String aid, atitle;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Article ID: ");
            aid = br.readLine();

            String showArticles = "SELECT * FROM Articles WHERE aid = ?";
            runPreparedStatement(false, showArticles, aid);

            System.out.println("\nPlease enter new title of the article: ");
            atitle = br.readLine();

            this.con.setAutoCommit(false);
            String articleUpd = "UPDATE Articles SET atitle = ? WHERE aid = ?;";
            runPreparedStatement(true, articleUpd, atitle, aid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }


    /**
     * Update Chapter's title
     * This function includes TRANSACTION
     */
    public void updateChapterTitle() {
        String pid, chno, chtitle;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID where you want to update chapter's title: ");
            pid = br.readLine();
            System.out.println("Please enter chapter number you want to update: ");
            chno = br.readLine();

            String showChapter = "SELECT * FROM Chapters WHERE pid = ? and chno = ?";
            runPreparedStatement(false, showChapter, pid, chno);

            System.out.println("\nPlease enter new title of the chapter: ");
            chtitle = br.readLine();

            this.con.setAutoCommit(false);
            String chapterUpd = "UPDATE Chapters SET chtitle = ? WHERE pid = ? and chno = ?;";

            runPreparedStatement(true, chapterUpd, chtitle, pid, chno);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }


    /**
     * Search for books written by particular author
     * This function includes TRANSACTION
     */
    public void findBooksByAuthor() {
        String sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in

            System.out.println("Please enter staff ID of the author: ");
            sid = br.readLine();


            this.con.setAutoCommit(false);
            String bookByAuthor = "SELECT title, ISBN, edition, dop FROM Books NATURAL JOIN Publications " +
                    "NATURAL JOIN WriteBook NATURAL JOIN Staff WHERE sid = ?";

            runPreparedStatement(false, bookByAuthor, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");


        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    /**
     * Search for books published on particular date
     * This function includes TRANSACTION
     */
    public void findBooksByDate() {
        String dop;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter book's date of publishing");
            System.out.println("The date should be in the form YYYY-MM-DD (i.e. 2020-02-20): ");
            dop = br.readLine();

            this.con.setAutoCommit(false);
            String bookByDate = "SELECT title, edition, ISBN FROM Books NATURAL JOIN Publications " +
                    "WHERE dop = ?";

            runPreparedStatement(true, bookByDate, dop);
            this.con.commit();
            System.out.println("\nTransaction Success!");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Search for books by particular topics
     * This function includes TRANSACTION
     */
    public void findBooksByTopic() {
        String topic;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter the topic of the book: ");
            topic = br.readLine();


            this.con.setAutoCommit(false);
            String bookByTopic = "SELECT title, edition, ISBN FROM Books NATURAL JOIN Publications " +
                    "NATURAL JOIN HasTopic NATURAL JOIN Topics " +
                    "WHERE topic LIKE ?";

            runPreparedStatement(false, bookByTopic, topic);

            this.con.commit();
            System.out.println("\nTransaction Success!");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Search for articles written by particular author
     * This function includes TRANSACTION
     */
    public void findArticleByAuthor() {
        String sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in

            System.out.println("Please enter staff ID of the author: ");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String articleByAuthor = "SELECT atitle, atext FROM Articles NATURAL JOIN Staff " +
                    "NATURAL JOIN WriteArticle WHERE sid = ?";

            runPreparedStatement(false, articleByAuthor, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    /**
     * Search for articles published on particular date
     * This function includes TRANSACTION
     */
    public void findArticleByDate() {
        String doi;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter article's date of publishing");
            System.out.println("The date should be in the form YYYY-MM-DD (i.e. 2020-02-20): ");
            doi = br.readLine();

            this.con.setAutoCommit(false);
            String articleByDate = "SELECT atitle, atext\n FROM Articles " +
                    "NATURAL JOIN ContainArticle NATURAL JOIN Issue" +
                    "NATURAL JOIN (SELECT pid FROM Publications) as Publication\n" +
                    "WHERE doi = ?";

            runPreparedStatement(false, articleByDate, doi);

            this.con.commit();
            System.out.println("\nTransaction Success!");


        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    /**
     * Search for articles by particular topics
     * This function includes TRANSACTION
     */
    public void findArticleByTopic() {

        String topic;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter the topic of the article: ");
            topic = br.readLine();

            this.con.setAutoCommit(false);
            String articleByTopic = "SELECT atitle, atext FROM Articles " +
                    "NATURAL JOIN ContainArticle NATURAL JOIN PeriodicPublication\n" +
                    "NATURAL JOIN HasTopic NATURAL JOIN Topics\n" +
                    "WHERE topic LIKE ?;";
            runPreparedStatement(false, articleByTopic, topic);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    /**
     * Enter new author of the particular article
     */
    public void enterArticleAuthor() {
        String aid, sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter article ID: ");
            aid = br.readLine();
            System.out.println("Please enter staff ID of the author you want to add: ");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String articleAuthor = "INSERT INTO WriteArticle (aid, sid) VALUES (?,?);";

            runPreparedStatement(true, articleAuthor, aid, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }


    /**
     * Update author of the particular article
     */
    public void updateArticleAuthor() {
        String aid, sidold, sidnew;
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter article ID you want to update: ");
            aid = br.readLine();
            String showArticles = "SELECT sid, sname, aid, atitle FROM Articles " +
                    "NATURAL JOIN WriteArticle NATURAL JOIN Staff WHERE aid = ?";
            runPreparedStatement(false, showArticles, aid);

            System.out.println("Please enter staff ID of the current author of this article you want to update: ");
            sidold = br.readLine();
            System.out.println("Please enter staff ID of the author you want to add: ");
            sidnew = br.readLine();

            this.con.setAutoCommit(false);
            String articleAuthor = "UPDATE WriteArticle SET sid = ? WHERE aid = ? and sid = ?;";
            runPreparedStatement(true, articleAuthor, sidnew, aid, sidold);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    /**
     * Adding new author to the book chapter
     */
    public void enterChapterAuthor() {
        String pid, sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID: ");
            pid = br.readLine();

            String showBook = "SELECT sid, sname, pid, title FROM Books\n" +
                    "NATURAL JOIN WriteBook NATURAL JOIN Staff\n" +
                    "NATURAL JOIN Publications WHERE pid = ?;";

            runPreparedStatement(false, showBook, pid);

            System.out.println("Please enter staff ID of the author you want to add: ");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String bookAuthor = "INSERT INTO WriteBook (pid, sid) VALUES (?,?);";
            runPreparedStatement(true, bookAuthor, pid, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }


    /**
     * Delete author of the particular book
     */
    public void deleteBookAuthor() {
        String pid, sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Publication ID: ");
            pid = br.readLine();

            String showBook = "SELECT sid, sname, pid, title FROM Books\n" +
                    "NATURAL JOIN WriteBook NATURAL JOIN Staff\n" +
                    "NATURAL JOIN Publications WHERE pid = ?;";
            runPreparedStatement(false, showBook, pid);

            System.out.println("Please enter staff ID of the author you want to delete: ");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String bookAuthor = "DELETE FROM WriteBook WHERE pid = ? and sid = ?;";
            runPreparedStatement(true, bookAuthor, pid, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }


    /**
     * Update the date of creation of an article
     */
    public void updateArticleDate() {
        String aid, doc;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Article ID you want to update: ");
            aid = br.readLine();

            String showArticles = "SELECT * FROM Articles WHERE aid = ?";
            runPreparedStatement(false, showArticles, aid);

            System.out.println("\nPlease enter new date of creation of the article: ");
            doc = br.readLine();

            this.con.setAutoCommit(false);
            String articleUpd = "UPDATE Articles SET doc = ? WHERE aid = ?;";

            runPreparedStatement(true, doc, aid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }


    /**
     * Update text of particular article
     */
    public void updateArticleText() {
        String aid, atext;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //used to read in
            System.out.println("Please enter Article ID: ");
            aid = br.readLine();

            String showArticles = "SELECT * FROM Articles WHERE aid = ?";
            runPreparedStatement(false, showArticles, aid);

            System.out.println("\nPlease enter new text of the article: ");
            atext = br.readLine();

            this.con.setAutoCommit(false);
            String articleUpd = "UPDATE Articles SET atext = ? WHERE aid = ?;";
            runPreparedStatement(true, articleUpd, atext, aid);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }






    /**
     * Enter a new payment for an employee
     */
    public void enterEmployeePayment () {
        String sid, paycheck, paydate;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter staff ID: ");
            sid = br.readLine();
            System.out.println("Please enter paycheck amount: ");
            paycheck = br.readLine();
            System.out.println("Please enter date of payment: ");
            paydate = br.readLine();

            this.con.setAutoCommit(false);
            String issueIns = "INSERT INTO Payments (sid, paycheck, paydate) VALUES (?,?,?);";
            runPreparedStatement(true, issueIns, sid, paycheck, paydate);

            this.con.commit();
            System.out.println("\nTransaction Success!");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }


    }

    /**
     * Keep track of when each payment was claimed by its adressee
     */
    public void employeePaymentDates() {
        String sid;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Please enter staff ID: ");
            sid = br.readLine();

            this.con.setAutoCommit(false);
            String empPay = "SELECT payid, paycheck, paydate FROM Staff\n" +
                    "NATURAL JOIN Payments WHERE sid = ?;";
            runPreparedStatement(false, empPay, sid);

            this.con.commit();
            System.out.println("\nTransaction Success!");
        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }


    /**
     * Distribution
     */

    public void viewAllDistributor()
    {
        final String query = "SELECT * from Distributors";
        runStatement(false,query);
    }

    public void deleteDistributor() {
        try {
            String did;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor id ( did ) ");
            did = br.readLine();
            this.con.setAutoCommit(false);
            String query = "delete from distributors where did=?";
            runPreparedStatement(true, query, did);
            this.con.commit();
            System.out.println("\nDistributor deleted!");
        }
        catch (Exception e)
        {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    public void newDistributor() {
        try {
            String dname, dtype, address, city, phno, contact, tot_balance;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor name");
            dname = br.readLine();
            System.out.println("Enter distributor type");
            dtype = br.readLine();
            System.out.println("Enter distributor address");
            address = br.readLine();
            System.out.println("Enter distributor city");
            city = br.readLine();
            System.out.println("Enter distributor phone number");
            phno = br.readLine();
            System.out.println("Enter distributor contact");
            contact = br.readLine();
            System.out.println("Enter distributor tot_balance");
            tot_balance = br.readLine();
            String sql_to_execute = "insert into distributors(dname,dtype,address,city,phno,contact,tot_balance) values (?,?,?,?,?,?,?)";
            runPreparedStatement(true,sql_to_execute,dname,dtype,address,city,phno,contact,tot_balance);
            System.out.println("Disttributor has been added");


        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    public void updateDistributor() {
        try {
            String did, dname, dtype, address, city, phno, contact, tot_balance;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor id ( did ) ");
            did = br.readLine();
            System.out.println("Enter modified distributor name");
            dname = br.readLine();
            System.out.println("Enter modified distributor type");
            dtype = br.readLine();
            System.out.println("Enter modified distributor address");
            address = br.readLine();
            System.out.println("Enter modified distributor city");
            city = br.readLine();
            System.out.println("Enter modified distributor phone number");
            phno = br.readLine();
            System.out.println("Enter modified distributor contact");
            contact = br.readLine();
            System.out.println("Enter modified distributor tot_balance");
            tot_balance = br.readLine();



            String sql_to_execute = "update DISTRIBUTORS set dname=?, dtype=?, address=?, city=?, phno=?, contact=?,tot_balance=? where did=?";
            runPreparedStatement(true,sql_to_execute,dname,dtype,address,city,phno,contact,tot_balance,did);
            System.out.println("\nThe distributor has been updated");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }
    }

    public void changeBalanceDistributor() {
        // changing the total_balance
        try {
            String balance_to_be_added, did;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor id ( did ) ");
            did = br.readLine();
            System.out.println("Enter balance to be added ( enter negative values in case of balance deduction)");
            balance_to_be_added = br.readLine();


            String sql_to_execute = "update DISTRIBUTORS set tot_balance=tot_balance + ? where did=?";
            runPreparedStatement(true, sql_to_execute, balance_to_be_added, did);
            System.out.println("\nThe distributor balance has been updated");


        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    public void newBookOrderDistributor() {
        // changing the total_balance
        try {
            String did,book_title,edition,copies,shipping_cost,odate,del_date,price;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor id ( did ) ");
            did = br.readLine();
            System.out.println("Enter the book title ");
            book_title = br.readLine();
            System.out.println("Enter the edition for the book ");
            edition = br.readLine();
            System.out.println("Enter the number of copies ");
            copies = br.readLine();
            System.out.println("Enter the price per copy ");
            price = br.readLine();
            System.out.println("Enter the shipping cost ");
            shipping_cost = br.readLine();
            System.out.println("Enter the date in (YYYY-MM-DD) format ");
            odate = br.readLine();
            System.out.println("Enter the delivery date in (YYYY-MM-DD) format ");
            del_date = br.readLine();

            this.con.setAutoCommit(false);
            String sql_to_execute = "INSERT into ORDERS(copies,odate,deldate,price,shcost) values (?,?,?,?,?)";
            runPreparedStatement(true, sql_to_execute,copies,odate,del_date,price,shipping_cost);
            String sql_to_execute2 = "insert into makeorder(oid,did) values ((select max(oid) from orders),?);";
            runPreparedStatement(true,sql_to_execute2,did);
            String sql_to_execute3="insert into consistof(oid,pid) values ((select max(oid) from orders),(select pid from books natural join publications where title = ? and edition=?));";
            runPreparedStatement(true,sql_to_execute3,book_title,edition);
            this.con.commit();

            System.out.println("\nThe distributor order has been updated");

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }

    public void billingDistributor() {
        // takes OID to bill ditributor
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String oid;
            System.out.println("Enter the order ID ");
            oid = br.readLine();


            String sql_to_execute = "select copies*price+shcost from orders where oid = ?";
            runPreparedStatement(false, sql_to_execute,oid);
            

        } catch (Exception e) {
            System.out.println("There was an error: " + e.getMessage());
        }

    }



        /**
         * Report generation
         */

    /**
     * Returns the number of copies sold by each distributor.
     */
    public void getCopiesSoldByDistributor() {
        final String query = "SELECT did, dname, MONTHNAME(odate), YEAR(odate) " +
                "FROM Distributors NATURAL JOIN MakeOrder NATURAL JOIN Orders " +
                "GROUP BY did, MONTHNAME(odate), YEAR(odate);";

        runStatement(false, query);
    }

    /**
     * Returns the total price of orders made by each distributor
     */
    public void getTotalPriceByDistributor() {
        final String query = "WITH Prices AS (" +
                "SELECT did, ROUND(price * copies + shcost, 2) AS Price, MONTHNAME(odate) AS month, YEAR(odate) AS year " +
                "FROM MakeOrder NATURAL JOIN Orders) " +
                "SELECT did, month, year, SUM(price) AS cost " +
                "FROM Prices GROUP BY did, month, year ORDER BY did;";

        runStatement(false, query);
    }

    /**
     * Returns the total revenue of the publishing house
     */
    public void getTotalRevenue() {
        final String query = "WITH Prices AS (" +
                "SELECT did, ROUND(price * copies + shcost, 2) AS Price " +
                "FROM Orders NATURAL JOIN MakeOrder NATURAL JOIN Distributors) " +
                "SELECT ROUND(SUM(price), 2) AS Revenue  " +
                "FROM Prices;";

        runStatement(false, query);
    }

    /**
     * Returns the total expenses of the publishing house
     */
    public void getTotalExpenses() {
        final String query = "WITH Costs AS ((\n" +
                "    SELECT shcost AS cost \n" +
                "    FROM Orders) UNION ALL (\n" +
                "    SELECT SUM(paycheck) AS cost\n" +
                "    FROM Payments    \n" +
                ")) \n" +
                "SELECT ROUND(SUM(cost), 2) AS TotalCost FROM Costs;";

        runStatement(false, query);
    }

    /**
     * Returns the number of distributors
     */
    public void getDistributorCount() {
        final String query = "SELECT COUNT(*) FROM Distributors;";

        runStatement(false, query);
    }

    /**
     * Returns the revenue of the publishing house by city
     */
    public void getRevenueByCity() {
        final String query = "WITH Prices AS (" +
                "SELECT ROUND(price * copies + shcost, 2) AS price, city " +
                "FROM MakeOrder NATURAL JOIN Orders NATURAL JOIN Distributors) " +
                "SELECT city, SUM(price) AS cost " +
                "FROM Prices GROUP BY city;";

        runStatement(false, query);
    }

    /**
     * Returns the revenue of the publishing house by distributor
     */
    public void getRevenueByDistributor() {
        final String query = "WITH Prices AS (" +
                "SELECT ROUND(price * copies + shcost, 2) AS price, dname " +
                "FROM MakeOrder NATURAL JOIN Orders NATURAL JOIN Distributors) " +
                "SELECT dname, SUM(price) AS cost " +
                "FROM Prices GROUP BY dname;";

        runStatement(false, query);
    }

    /**
     * Returns the revenue of the publishing house by location
     */
    public void getRevenueByLocation() {
        final String query = "WITH Prices AS (" +
                "SELECT ROUND(price * copies + shcost, 2) AS price, address " +
                "FROM MakeOrder NATURAL JOIN Orders NATURAL JOIN Distributors) " +
                "SELECT address, SUM(price) AS cost " +
                "FROM Prices GROUP BY address;";

        runStatement(false, query);
    }

    /**
     * Returns the revenue of the publishing house by staff type
     */
    public void getTotalPayByStaffType() {
        final String query = "SELECT stype, SUM(paycheck)\n" +
                "FROM Staff\n" +
                "NATURAL JOIN Payments\n" +
                "GROUP BY stype;\n";

        runStatement(false, query);
    }
}
