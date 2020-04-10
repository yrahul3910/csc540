import java.sql.*;
import java.io.*;

public class DistributorMenu {

    private static final String jdbcURL = "jdbc:mysql://localhost:3306/setup";
    private static final String user = "root";
    private static final String password = "Google@123";

    public static void main(String[] args) {
        //newDistributor();
        //deleteDistributor();
        viewAllDistributors();
        //updateDistributor();

    }

    private static void updateDistributor() {
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

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = null;
            ResultSet result = null;
            connection = DriverManager.getConnection(jdbcURL, user, password);
            connection.setAutoCommit(false); //set autocommit to false

            String sql_to_execute = "update DISTRIBUTORS set dname=?, dtype=?, address=?, city=?, phno=?, contact=?,tot_balance=? where did=?";
            PreparedStatement statement = connection.prepareStatement(sql_to_execute);

            statement.setString(1, dname);
            statement.setString(2, dtype);
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, phno);
            statement.setString(6, contact);
            statement.setString(7, tot_balance);
            statement.setString(8, did);
            statement.execute();
            connection.commit(); //commit the transaction if there is no error
            statement.close();
            connection.close();
            System.out.println("\nThe distributor has been updated");

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    public static void newDistributor() {
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

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = null;
            ResultSet result = null;
            connection = DriverManager.getConnection(jdbcURL, user, password);
            connection.setAutoCommit(false); //set autocommit to false
            String sql_to_execute = "insert into distributors(dname,dtype,address,city,phno,contact,tot_balance) values (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql_to_execute);

            statement.setString(1, dname);
            statement.setString(2, dtype);
            statement.setString(3, address);
            statement.setString(4, city);
            statement.setString(5, phno);
            statement.setString(6, contact);
            statement.setString(7, tot_balance);
            statement.execute();
            connection.commit(); //commit the transaction if there is no error
            //    result.close();           // no result set is generated here
            statement.close();
            connection.close();
            System.out.println("\nThe distributor has been added");
        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    public static void deleteDistributor() {
        try {
            String did;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter distributor id ( did ) ");
            did = br.readLine();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = null;
            ResultSet result = null;
            connection = DriverManager.getConnection(jdbcURL, user, password);
            connection.setAutoCommit(false); //set autocommit to false
            String sql_to_execute = "delete from distributors where did=?";
            PreparedStatement statement = connection.prepareStatement(sql_to_execute);
            statement.setString(1, did);
            statement.execute();
            connection.commit(); //commit the transaction if there is no error
//            result.close();
            statement.close();
            connection.close();
            System.out.println("\nThe distributor has been removed");
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void viewAllDistributors() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            connection = DriverManager.getConnection(jdbcURL, user, password);
            statement = connection.createStatement();
            result = statement.executeQuery("SELECT * from Distributors");
            ResultSetMetaData rsmd = result.getMetaData();
            int columnsNumber = rsmd.getColumnCount();


            while (result.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print("\t");
                    String columnValue = result.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println("");
            }

        } catch (Exception E) {
            E.printStackTrace();
        }

    }

}
