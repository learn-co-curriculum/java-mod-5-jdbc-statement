# JDBC - Statement

## Learning Goals

- Create a `Statement` object to execute SQL statements on the database.
- Call the `execute` method to execute SQL `CREATE TABLE` and `DROP TABLE` statements.
- Call the `executeUpdate` method to execute SQL `INSERT`, `UPDATE`, and `DELETE` statements.

## Introduction

The previous lesson covered how to use the `DriverManager` class
and `Connection` interface to connect to a PostgreSQL database.
Now we will see how to use the `Statement` interface to execute an SQL statement:

- **Statement** : An interface that executes SQL statements through a database connection.

The `Statement` interface has several methods for executing
different types of SQL statements:

- `boolean execute (String SQL)` - Execute a DDL statement such as `CREATE TABLE` and `DROP TABLE`.
- `int executeUpdate (String SQL)` - Execute a DML statement such as `INSERT`, `UPDATE`, and `DELETE`.
   The method returns the number of rows affected.
- `ResultSet executeQuery (String SQL)` - Execute a DQL statement such as `SELECT`. 
   The method returns a result set, which encapsulates a data table.

This lesson will demonstrate the `execute` and `executeUpdate` methods.
The `executeQuery` method will be demonstrated in a separate lesson.

## Code along

[Fork and clone](https://github.com/learn-co-curriculum/java-mod-5-jdbc-statement) this lesson to run the sample Java programs.

NOTE: This lesson assumes you created a new PostgreSQL database named `employee_db` as instructed
in the previous lesson.

## Execute DROP TABLE and CREATE TABLE statements

The steps to execute an SQL CREATE TABLE statement are:

1. Create a `Connection` object.  
   `Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);`
2. Use the `Connection` object to create a `Statement` object .  
   `Statement statement = connection.createStatement();`
3. Call the `execute` method on the `Statement` object, passing a string containing an SQL DDL statement.    
   `statement.execute("CREATE TABLE employee (id INTEGER PRIMARY KEY, email TEXT, office TEXT, salary DECIMAL)");`

**NOTE:** Omit the semicolon in the SQL string passed into the `execute` method.

The  `CreateTableStatement` class below demonstrates how to use a `Statement` object
to execute `DROP TABLE` and `CREATE TABLE` statements. The method
calls `getConnection`, `createStatement`, and `execute` all may throw an `SQLException` and
are encapsulated in a `try-catch` block.

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableStatement {

   static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
   static final String USER = "postgres";
   static final String PASSWORD = "postgres";

   public static void main(String[] args) {
      try (   //Establish a connection to the database
              Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
              //Create a statement object to execute SQL statements on the database
              Statement statement = connection.createStatement();
      ) {
         //Drop the employee table if it exists
         statement.execute("DROP TABLE IF EXISTS employee");

         //Create the employee table
         statement.execute("CREATE TABLE employee (id INTEGER PRIMARY KEY, email TEXT, office TEXT, salary DECIMAL)");
         System.out.println("Employee table created");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

   }
}
```

Run `CreateTableStatement.main` to create the `employee` table.
The output should confirm the table was successfully created.
You can confirm this by querying the table in the **pgAdmin** query tool panel.
The panel should display the empty `employee` table:

![new employee table](https://curriculum-content.s3.amazonaws.com/6036/jdbc-statement/empty_table.png)

### try-with-resources with multiple resources

Notice the `try-with-resources` statement opened two resources: (1) a `Connection` object, and (2) a `Statement` object:

```java
try (
        //Establish a connection to the database
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        //Create a statement object to execute SQL statements on the database
        Statement statement = connection.createStatement();
) {
         //Drop the employee table if it exists
         statement.execute("DROP TABLE IF EXISTS employee");

         //Create the employee table
         statement.execute("CREATE TABLE employee (id INTEGER PRIMARY KEY, email TEXT, office TEXT, salary DECIMAL)");
         System.out.println("Employee table created");
} catch (SQLException e) {
         System.out.println(e.getMessage());
}
```

The resources will be closed in the opposite order that they were opened.  Thus, the `Statement` object will
close first, then the `Connection` object.

## Execute an INSERT statement

The steps to execute an SQL INSERT statement are:

1. Create a `Connection` object.  
   `Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);`
2. Use the `Connection` object to create a `Statement` object .  
   `Statement statement = connection.createStatement();`
3. Call the `executeUpdate` method on the `Statement` object, passing a string containing an SQL INSERT statement.   
   The `executeUpdate` method returns the number of rows inserted.   NOTE: Use backslash to escape the single quotes for string values.    
   `int nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (1, \'emp1@company.com\', \'b150\', 60000.0)");` 

The `InsertStatement` class shown below connects to the database, creates a statement object through the connection, and then
calls the `executeUpdate` method three times to insert three rows into the `employee` table:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertStatement {

   static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
   static final String USER = "postgres";
   static final String PASSWORD = "postgres";

   public static void main(String[] args) {
      try (   //Establish a connection to the database
              Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
              //Create a statement object to execute SQL statements on the database
              Statement statement = connection.createStatement();
      ) {
         //Execute the insert statement to insert a new row.  The executeUpdate method returns number of rows inserted.
         //Escape the single quotes using a backslash.
         int nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (1, \'emp1@company.com\', \'b150\', 60000.0)");
         System.out.println(String.format("%d row inserted", nrow));

         nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (2, \'emp2@company.com\', \'a120\', 87000.0)");
         System.out.println(String.format("%d row inserted", nrow));

         nrow = statement.executeUpdate("INSERT INTO employee (id, email, office, salary) VALUES (3, \'emp3@company.com\', \'b200\', 150000.0)");
         System.out.println(String.format("%d row inserted", nrow));
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
}
```

Run `InsertStatement.main` and confirm the output:

```text
1 row inserted
1 row inserted
1 row inserted
```

Query the table in the query tool to confirm the 3 new rows:

![insert 3 rows](https://curriculum-content.s3.amazonaws.com/6036/jdbc-statement/insert_rows.png
)

## Execute an UPDATE statement

The steps to execute an SQL UPDATE statement are:

1. Create a `Connection` object.  
   `Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);`
2. Use the `Connection` object to create a `Statement` object .  
   `Statement statement = connection.createStatement();`
3. Call the `executeUpdate` method on the `Statement` object, passing a string containing an SQL UPDATE statement.   
   The `executeUpdate` method returns the number of rows updated.      
   `int nrow = statement.executeUpdate("UPDATE employee SET salary = 65000.0 WHERE id = 1");`

The `UpdateStatement` class shown below connects to the database, creates a statement object through the connection,
and then calls the `executeUpdate` method to update the salary for the employee with `id = 1`:


```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateStatement {

   static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
   static final String USER = "postgres";
   static final String PASSWORD = "postgres";

   public static void main(String[] args) {
      try (   //Establish a connection to the database
              Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
              //Create a statement object to execute SQL statements on the database
              Statement statement = connection.createStatement();
      ) {
         //Execute the update statement.  The executeUpdate method returns number of rows updated
         int nrow = statement.executeUpdate("UPDATE employee SET salary = 65000.0 WHERE id = 1");
         System.out.println(String.format("%d row updated", nrow));
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
}
```

Run `UpdateStatement.main` and confirm the output:

```text
1 row updated
```

Query the table in the query tool to confirm the salary for the employee with `id = 1` was updated:

![update employee row](https://curriculum-content.s3.amazonaws.com/6036/jdbc-statement/update_1_row.png)


## Execute a DELETE statement

The steps to execute an SQL DELETE statement are:

1. Create a `Connection` object.  
   `Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);`
2. Use the `Connection` object to create a `Statement` object .  
   `Statement statement = connection.createStatement();`
3. Call the `executeUpdate` method on the `Statement` object, passing a string containing an SQL DELETE statement.   
   The `executeUpdate` method returns the number of rows deleted.    
   `int nrow = statement.executeUpdate("DELETE FROM employee WHERE id = 1");`

The `DeleteStatement` class shown below connects to the database, creates a statement object through the connection,
and then calls the `executeUpdate` method to delete the row for the employee with `id = 1`:


```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteStatement {

   static final String DB_URL = "jdbc:postgresql://localhost:5432/employee_db";
   static final String USER = "postgres";
   static final String PASSWORD = "postgres";

   public static void main(String[] args) {
      try (   //Establish a connection to the database
              Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
              //Create a statement object to execute SQL statements on the database
              Statement statement = connection.createStatement();
      ) {
         //Execute the delete statement.  The method executeUpdate returns number of rows deleted
         int nrow = statement.executeUpdate("DELETE FROM employee WHERE id = 1");
         System.out.println(String.format("%d row deleted", nrow));
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
}
```

Run `DeleteStatement.main` and confirm the output:

```text
1 row deleted
```

Query the table in the query tool to confirm the row for the employee with `id = 1` was deleted.

![empty table](https://curriculum-content.s3.amazonaws.com/6036/jdbc-statement/delete_row.png)


## Conclusion

Once a connection to the database has been established, we can create a `Statement` object to execute
one or more SQL statements.  The `execute` method is used with DDL statements such as `CREATE TABLE` and
`DROP TABLE`.  The `executeUpdate` method is used with DML statements such as `INSERT`, `UPDATE`, and `DELETE`.


## Resources

- [JDBC API](https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/download/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- [java.sql.DriverManager](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/DriverManager.html)
- [java.sql.Connection](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Connection.html)   
- [java.sql.Statement](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/Statement.html)   
- [java.sql.ResultSet](https://docs.oracle.com/en/java/javase/11/docs/api/java.sql/java/sql/ResultSet.html)   


