package io.tolstjak.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    private StudentDbUtil studentDbUtil;

    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        // create our StudentDbUtil ... and pass in the connection pool / dataSource
        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // read the "command" parameter
            String command = req.getParameter("command");

            // if the command is missing, then default to listing students
            if (command == null) {
                command = "LIST";
            }

            // route to the appropriate method
            switch (command) {
                case "LIST":
                    listStudents(req, resp);
                    break;

                case "ADD":
                    addStudent(req, resp);
                    break;

                case "LOAD":
                    loadStudent(req, resp);
                    break;

                case "UPDATE":
                    updateStudent(req, resp);
                    break;

                case "DELETE":
                    deleteStudent(req, resp);
                    break;

                default:
                    listStudents(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // read student id from form data
        int studentId = Integer.parseInt(req.getParameter("studentId"));

        // delete student from database
        studentDbUtil.deleteStudent(studentId);

        // send them back to "list students" page
        listStudents(req, resp);
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // read student info from form data
        int id = Integer.parseInt(req.getParameter("studentId"));
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");

        // create a new student object
        Student student = new Student(id, firstName, lastName, email);

        // perform update on database
        studentDbUtil.updateStudent(student);

        // send them back to the "list students" page
        listStudents(req, resp);
    }

    private void loadStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // read student id from form data
        int studentId = Integer.parseInt(req.getParameter("studentId"));

        // get student from database (db util)
        Student student = studentDbUtil.getStudent(studentId);

        // place student in the request attribute
        req.setAttribute("student", student);

        // send to jsp page: update-student-form.jsp
        RequestDispatcher dispatcher = req.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(req, resp);
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // read student info from form data
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");

        // create a new student object
        Student student = new Student(firstName, lastName, email);

        // add the student to the database
        studentDbUtil.addStudent(student);

        // send back to the main page (the student list)
        listStudents(req, resp);
    }

    private void listStudents(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // get students from DB util
        List<Student> students = studentDbUtil.getStudents();

        // add students to the request
        req.setAttribute("student_list", students);

        // send to JSP page (view)
        RequestDispatcher dispatcher = req.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(req, resp);
    }
}
