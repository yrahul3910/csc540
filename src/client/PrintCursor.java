import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class PrintCursor {
    private ResultSet results;

    public PrintCursor(ResultSet rs) {
        this.results = rs;
    }

    public boolean print() {
        try {
            ResultSetMetaData meta = this.results.getMetaData();
            int colCount = meta.getColumnCount();

            for (int i = 0; i < colCount; i++) {
                String column = meta.getColumnLabel(i);
                System.out.print(column + "\t");
            }
            System.out.println();

            while (this.results.next()) {
                for (int i = 0; i < colCount; i++)
                    System.out.println(this.results.getString(meta.getColumnLabel(i)) + "\t");

                System.out.println();
            }

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}