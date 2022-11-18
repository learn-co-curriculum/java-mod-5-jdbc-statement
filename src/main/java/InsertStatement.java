import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertStatement {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
    static final String USER = "postgres";
    static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        try {
            //Establish a connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            //Create a statement object to execute SQL statements through the database connection
            Statement statement = connection.createStatement();

            //Execute the insert statement to insert a new row.  The executeUpdate method returns number of rows inserted.
            //Escape the single quotes using a backslash.
            int nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (1, \'emp1@company.com\', \'b150\', 60000.0)");
            System.out.println(String.format("%d row inserted", nrow));

            nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (2, \'emp2@company.com\', \'a120\', 87000.0)");
            System.out.println(String.format("%d row inserted", nrow));

            nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (3, \'emp3@company.com\', \'b200\', 150000.0)");
            System.out.println(String.format("%d row inserted", nrow));

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}