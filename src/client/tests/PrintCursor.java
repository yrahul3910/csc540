import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static java.lang.Math.min;

public class PrintCursor {
    private ResultSet results;

    public PrintCursor(ResultSet rs) {
        this.results = rs;
    }

    public boolean print() {
        try {
            ResultSetMetaData meta = this.results.getMetaData();
            int colCount = meta.getColumnCount();
            int displaySize = 0;

            for (int i = 1; i <= colCount; i++) {
                String column = meta.getColumnLabel(i);
                displaySize = min(meta.getColumnDisplaySize(i), 20);
                String format = "%" + String.valueOf(displaySize) + "s";
                System.out.printf(format, column);
            }
            System.out.println();

            while (this.results.next()) {
                for (int i = 1; i < colCount; i++) {
                    displaySize = min(meta.getColumnDisplaySize(i), 20);
                    String format = "%" + String.valueOf(displaySize) + "s";
                    System.out.printf(format, this.results.getString(meta.getColumnLabel(i)) + "\t\t");
                }

                System.out.println();
            }

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}