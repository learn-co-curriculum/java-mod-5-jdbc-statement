import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStatement {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
    static final String USER = "postgres";
    static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        try {
            //Establish a connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            //Create a statement object to execute SQL statements through the database connection
            Statement statement = connection.createStatement();

            //Execute the update statement.  The executeUpdate method returns number of rows updated
            int nrow = statement.executeUpdate("UPDATE employee SET salary = 65000.0 WHERE id = 1");
            System.out.println(String.format("%d row updated", nrow));

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}