<%--
  Created by IntelliJ IDEA.
  User: Tolstjak
  Date: 4/19/2021
  Time: 11:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--------------------Without using JSTL--------------------%>
<%--<%@ page import="java.util.*" %>--%>
<%--<%@ page import="io.tolstjak.web.jdbc.Student" %>--%>
<%--<%--%>
<%--    // get the students from the request object (sent by servlet)--%>
<%--    List<Student> students = (List<Student>) request.getAttribute("student_list");--%>
<%--%>--%>
<%----------------------------------------------------------%>

<%--------------------Using JSTL--------------------%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--------------------------------------------------%>

<html>
<head>
    <title>Student Tracker App</title>

    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>

    <div id="wrapper">
        <div id="header">
            <h2>FooBar University</h2>
        </div>
    </div>

    <div id="container">
        <div id="content">

            <input type="button" value="Add Student"
                   onclick="window.location.href='add-student-form.jsp'; return false;"
                   class="add-student-button" />

            <table>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
<%--------------------Without using JSTL--------------------%>
<%--                <% for (Student student : students) { %>--%>
<%--                    <tr>--%>
<%--                        <td><%=student.getFirstName()%></td>--%>
<%--                        <td><%=student.getLastName()%></td>--%>
<%--                        <td><%=student.getEmail()%></td>--%>
<%--                    </tr>--%>
<%--                <% } %>--%>
<%----------------------------------------------------------%>

<%--------------------Using JSTL--------------------%>
                <c:forEach var="student" items="${student_list}">
                    <!-- set up link for each student -->
                    <c:url var="loadLink" value="StudentControllerServlet">
                        <c:param name="command" value="LOAD" />
                        <c:param name="studentId" value="${student.id}" />
                    </c:url>

                    <!-- set up link to delete a student -->
                    <c:url var="deleteLink" value="StudentControllerServlet">
                        <c:param name="command" value="DELETE" />
                        <c:param name="studentId" value="${student.id}" />
                    </c:url>

                    <tr>
                        <td>${student.firstName}</td>
                        <td>${student.lastName}</td>
                        <td>${student.email}</td>
                        <td>
                            <a href="${loadLink}">Update</a>
                              |
                            <a href="${deleteLink}"
                               onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
                                Delete
                            </a>
                        </td>

                    </tr>
                </c:forEach>
<%--------------------------------------------------%>
            </table>
        </div>
    </div>

</body>
</html>
