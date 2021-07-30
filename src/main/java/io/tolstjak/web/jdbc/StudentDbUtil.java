package io.tolstjak.web.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDbUtil {

    private DataSource dataSource;

    public StudentDbUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Student> getStudents() throws Exception {

        List<Student> students = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // get a connection
            connection = dataSource.getConnection();

            // create sql statement
            String sql = "SELECT * " +
                    "FROM student " +
                    "ORDER BY last_name";
            statement = connection.createStatement();

            // execute query
            resultSet = statement.executeQuery(sql);

            // process result set
            while (resultSet.next()) {
                // retrieve data from result set row
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                // create new student object
                Student student = new Student(id, firstName, lastName, email);

                // add it to the list of students
                students.add(student);
            }

            return students;

        } finally {
            // close JDBC objects
            close(connection, statement, resultSet);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();     // doesn't really close it ... just puts back in connection pool
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student student) throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // get db connection
            connection = dataSource.getConnection();

            // create sql for insert
            String sql = "INSERT INTO student " +
                    "(first_name, last_name, email) " +
                    "values (?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);

            // set the param values for the student
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());

            // execute sql insert
            preparedStatement.execute();
        } finally {
            // clean up JDBC object
            close(connection, preparedStatement, null);
        }
    }

    public Student getStudent(int studentId) throws Exception {

        Student student;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // get connection to database
            connection = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * " +
                    "FROM student " +
                    "WHERE id = ?";

            // create prepared statement
            preparedStatement = connection.prepareStatement(sql);

            // set params
            preparedStatement.setInt(1, studentId);

            // execute statement
            resultSet = preparedStatement.executeQuery();

            // retrieve data from result set row
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                student = new Student(studentId, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student by id: " + studentId);
            }

            return student;

        } finally {
            // clean up JDBC objects
            close(connection, preparedStatement, resultSet);
        }
    }

    public void updateStudent(Student student) throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // get db connection
            connection = dataSource.getConnection();

            // create sql update statement
            String sql = "UPDATE student " +
                    "SET first_name=?, last_name=?, email=? " +
                    "WHERE id=?";

            // create prepare statement
            preparedStatement = connection.prepareStatement(sql);

            // set param values for the student
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setInt(4, student.getId());

            // execute sql statement
            preparedStatement.execute();
        } finally {
            // clean up JDBC object
            close(connection, preparedStatement, null);
        }
    }

    public void deleteStudent(int studentId) throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // get db connection
            connection = dataSource.getConnection();

            // create sql to delete student
            String sql = "DELETE FROM student " +
                    "WHERE id=?";

            // create prepare statement
            preparedStatement = connection.prepareStatement(sql);

            // set param values for the student
            preparedStatement.setInt(1, studentId);

            // execute sql statement
            preparedStatement.execute();
        } finally {
            // clean up JDBC object
            close(connection, preparedStatement, null);
        }
    }
}
