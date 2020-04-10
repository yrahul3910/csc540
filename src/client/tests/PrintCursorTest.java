import java.sql.*;

public class PrintCursorTest {
    public static boolean testPrint() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/setup?serverTimezone=UTC", "root", "password");

                Statement statement = con.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM Staff");

                PrintCursor printer = new PrintCursor(rs);
                return printer.print();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        testPrint();
    }
}
