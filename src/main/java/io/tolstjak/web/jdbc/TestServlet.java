package io.tolstjak.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {

    // Define datasource/connection pool for Resource Injection
    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Step 1: Set up the PrintWriter
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        // Step 2: Get a connection to the database
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            if (dataSource == null) {
                System.out.println("DataSource is null!");
            }
            myConn = dataSource.getConnection();

            // Step 3: Create a SQL statements
            String sql = "SELECT * FROM student";
            myStmt = myConn.createStatement();

            // Step 4: Execute SQL query
            myRs = myStmt.executeQuery(sql);

            // Step 5: Process result set
            while (myRs.next()) {
                String email = myRs.getString("email");
                out.println(email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
