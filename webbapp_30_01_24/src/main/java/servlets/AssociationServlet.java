package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
//QUESTIONS FOR LUKAS, - varför visas inte alla anjanis kurser i php

@WebServlet(urlPatterns = "/studentcourses") //
public class AssociationServlet extends HttpServlet { //servlet klassen

    boolean isNewRow; //to check is new row was added
    private int lastInsertedID;
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy", "userINSERT", "insert"); //msq connector port är 13306 (pom är tomcat 9999)

    public AssociationServlet() throws SQLException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();


        String htmlOutTop = ("<html>"
                + "<head><title>Association Table</title></head>"
                +"<link rel='stylesheet' type='text/css' href='styles.css'>"
                +"<script src=https://cdn.tailwindcss.com></script>"
                +"<body>" + "<p style='text-align:center'>"
                +"<div class='navbar'>"
                + "<a href ='home'>" + "Home |  </a>"
                +"<a href ='searchstudent'>" + "Search Student |  </a>"
                +"<a href ='addstudent'>" + "Add Students |  </a>"
                + "<a href ='courses'>" + "Add Courses</a>"
                +"</div></p>");

        String htmlOutBottom = ("<div class='bg-white'>"); //styles for the tables

        out.println(htmlOutTop);

        studentCourseForm(req, resp); //form

        out.println(htmlOutBottom);

        studentTable(out); //calling student table method
        courseTable(out); //calling course tablöe method

        out.println("</body>" + "</html>");

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        int student_id = Integer.parseInt(req.getParameter("student_id")); //getting the form parameters
        int course_id = Integer.parseInt(req.getParameter("course_id"));

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy", "userINSERT", "insert"); //msq connector port är 13306 (pom är tomcat 9999)
            String newStudentSql = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(newStudentSql);
            ps.setInt(1, student_id);
            ps.setInt(2, course_id);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("student was added to the course");
                //out.println("Student was added to the course"); //see why code from association table method is not working correct
                isNewRow = true; //boolean (to add color to new row)
            } else { System.out.println("NOPE - student was not added to the course");
                isNewRow = false;
            }

            associationTable(out); //shows after clicks submit

            displayResetButton(out); //reset button as well
            isNewRow = false; //boolean false- table should get rid of extra row color, go back to normal

            ps.close();
            connection.close();


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //resp.sendRedirect(req.getContextPath() + "/studentcourses"); //redirect back tio soget method so student list will show with new student //this stops table from showing
    }

    protected void studentCourseForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String htmlOutForm = ("<br>"
                + "<div class='form-container'>"
                + "<form style='margin:5px;' action=/studentcourses method=POST>"
                + "<label for=student_id>Student Id:</label>"
                + "<input type=text id=student_id name=student_id required><br>"
                + "<label for=name>Course Id:</label>"
                + "<input type=text id=course_id name=course_id required><br>"
                + "<input type=submit value=Submit>"
                + "</form>"
                + "</div>"
                + "<br>"
        + "</div>");

        out.println(htmlOutForm);


    }

//student table showing on firstpage
    private void studentTable(PrintWriter out) throws IOException {
        String studentSql = "SELECT * FROM students";

        try {
            connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
            PreparedStatement ps3 = connection.prepareStatement(studentSql);
            ResultSet rs3 = ps3.executeQuery();

            String htmlOutStudent = ("<h3 class='table-heading'>Students</h3>" +
                    "<table>" +
                    "<tr>" +
                    "<th>ID</th>" +
                    "<th>Firstname</th>" +
                    "<th>Lastname</th>" +
                    "<th>Hometown</th>" +

                    "</tr>");

            out.println(htmlOutStudent);

            while (rs3.next()) {
                String id = rs3.getString("id");
                String fname = rs3.getString("fName");
                String lname = rs3.getString("lName");
                String town = rs3.getString("town");


                out.println("<tr>"); //andra tables row med all table data
                out.println("<td>" + id + "</td>");
                out.println("<td>" + fname + "</td>");
                out.println("<td>" + lname + "</td>");
                out.println("<td>" + town + "</td>");


                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</body>" + "</html>");




        } catch (
                SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }
//course table showing on firstpage

    private void courseTable(PrintWriter out) throws IOException {
        try {
            connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
            String allCoursesSql = "SELECT * FROM courses";
            PreparedStatement ps2 = connection.prepareStatement(allCoursesSql);
            ResultSet rs2 = ps2.executeQuery();

            String htmlOut2 = ("<h3 class='table-heading'>Courses</h3>" + "<table>" + "<tr>" +
                    "<th>ID</th>"
                    + "<th>Course</th>"
                    + "<th>Points</th>"
                    + "<th>Description</th>"
                    + "</tr>");
            out.println(htmlOut2);

            while (rs2.next()) {

                String id = rs2.getString("id");
                String name = rs2.getString("name");
                String points = rs2.getString("points");
                String description = rs2.getString("description");

                out.println("<tr>" + "<td>" + id + "</td>"
                        + "<td>" + name + "</td>"
                        + "<td>" + points + "</td>"
                        + "<td>" + description + "</td>"
                        + "</tr>");
            }

            out.println("</table>");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

        //assocation table that shows after submit is clicked
        private void associationTable (PrintWriter out) throws
        ServletException, IOException, SQLException, ClassNotFoundException {
            try {
                connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
                String associationSql = "SELECT s.id, s.fname, s.lname, c.name, c.description "
                       + "FROM student_course sc "
                       + "LEFT JOIN students s ON sc.student_id = s.id "
                       + "LEFT JOIN courses c ON sc.course_id = c.id "
                       + "ORDER BY s.id";

                PreparedStatement ps = connection.prepareStatement(associationSql);
                ResultSet rs = ps.executeQuery();

                String htmlOutTop = ("<html>"
                        +"<head><title>Association Table</title></head>"
                        +"<link rel='stylesheet' type='text/css' href='styles.css'>"
                        +"<script src=https://cdn.tailwindcss.com></script>"
                        +"<body>" + "<p style='text-align:center'>"
                        +"<div class='navbar'>"
                        + "<a href ='home'>" + "Home |  </a>"
                        + "<a href ='searchstudent'>" + "Search Students |  </a>"
                        + "<a href ='addstudent'>" + "Add Students |  </a>"
                        + "<a href ='courses'>" + "Add Courses</a>"
                        +"</div></p>"
                       +"<div class='bg-white'>"
                       +"<h3 class='table-heading'>Association Table for Student and Course</h3>" + "<table>" + "<tr>"

                       +"<th>ID</th>" + "<th>Firstname</th>" + "<th>Lastname</th>" + "<th>Course</th>" + "<th>Course Description</th>" + "</tr>");

                out.println(htmlOutTop);

                while (rs.next()) {

                    String ID = rs.getString("id");
                    String firstname = rs.getString("fname");
                    String lastname = rs.getString("lname");
                    String courseName = rs.getString("name");
                    String courseDescription = rs.getString("description");

                    //out.println("<p>" + firstname + " " + lastname + " was added to " + courseName + "</p>"); - make a selcect query to only show recently added to mark the new row in the table


                    //see if a new row is added then color click on there //this is not funcitonig correct, do i need to add to see if new row ID match with table id?
                    /*if (isNewRow) {
                        System.out.println(firstname + lastname + courseName);
                       out.println("<p>" + firstname + " " + lastname + " was added to " + courseName + "</p>");
                       out.println("<tr class='new-row'>");
                       isNewRow = false;
                    } else {
                        out.println("<tr>");
                    }


                     */
                    out.println("<td>" + ID + "</td>"
                            + "<td>" + firstname + "</td>"
                            + "<td>" + lastname + "</td>"
                            + "<td>" + courseName + "</td>"
                            + "<td>" + courseDescription + "</td>"
                            + "</tr>");

                }
                    String htmlBottom = ("</div></h3></div></table>");
                    out.println(htmlBottom);


            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        //reset button
        private static void displayResetButton(PrintWriter out) {
            out.println("<form action=/studentcourses method=GET>");
            out.println("<input type=submit value=Reset>");
            out.println("</form></body></html>");

        }
    }
