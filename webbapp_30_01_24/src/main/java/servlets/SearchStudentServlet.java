package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
    @WebServlet(name="searchstudentservlet", urlPatterns = "/searchstudent")
    public class SearchStudentServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();

            String errorMessage = "<html><p>" + req.getAttribute("message") + "</p></html>"; // Retrieve message from the request attribute


            String htmlOutTop = ("<html>"
                    + "<head><title>Association Table</title></head>"
                    + "<link rel='stylesheet' type='text/css' href='styles.css'>"
                    + "<script src=https://cdn.tailwindcss.com></script>"
                    + "<body>"
                    + "<div class='navbar'>"
                    + "<a href ='home'>" + "Home |  </a>"
                    + "<a href ='addstudent'>" + "Add Students |  </a>"
                    + "<a href ='courses'>" + "Add Courses | </a>"
                    + "<a href ='studentcourses'>" + "Add Students to Courses</a>"
                    + "</div>"
                    + "</p>");

            out.println(errorMessage); //printing out error message

            String htmlOutBottom = ("<div class='bg-white'>"); //retrive message from the request attribute\

            out.println(htmlOutTop); //top html code

           //String message = "" +req.getParameter("message"); //retrive message from the request attribute


            searchStudentForm(req, resp); //call method for search form

            out.println(htmlOutBottom); //printing out bottom html

            allStudents(req, resp); //call method for showing students


            //LÄGG TILL MESSAGE nånstans
        }

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String fname = req.getParameter("fname"); //getting the form parameters
            String lname = req.getParameter("lname");
            String message = ""; //saved variable for error message

            //not functionig, program keeps going even though the message is empty, it doesnt send back to doGet method ASK LUKAS FOR HELP HERE
/*
            if (fname.trim().isEmpty() || lname.trim().isEmpty()) {
                message += "Name is not correct, check the student list and try again";
            }
            if (!message.trim().isEmpty()) {
                req.setAttribute("message", message); //holding the message
                req.getRequestDispatcher("/searchstudent").forward(req, resp); //should send  back to doGet method
                return;
            }


 */
            System.out.println(fname + lname);

                try {
                    Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userINSERT", "insert");
                    String associationSql = "SELECT s.fname, s.lname, c.name, c.points, c.description FROM students s "
                            + "JOIN student_course sc ON s.id = sc.student_id "
                            + "JOIN courses c ON c.id = sc.course_id "
                            + "WHERE s.fname='" + fname + "' AND s.lname='" + lname + "'";

                    PreparedStatement ps = connection.prepareStatement(associationSql);
                    ResultSet rs = ps.executeQuery();

                    System.out.println(fname + lname);

                    PrintWriter out = resp.getWriter();

                    String htmlOutTop = ("<html>"
                            + "<head><title>Association Table</title></head>"
                            + "<link rel='stylesheet' type='text/css' href='styles.css'>"
                            + "<script src=https://cdn.tailwindcss.com></script>"
                            + "<body>" + "<p style='text-align:center'>"

                            +"<div class='navbar'>"
                            + "<a href ='http://localhost:9999/home'>" + "Home |  </a>"
                            + "<a href ='http://localhost:9999/searchstudent'>" + "Search Student |  </a>"
                            + "<a href ='http://localhost:9999/addstudent'>" + "Add Students |  </a>"
                            + "<a href ='http://localhost:9999/studentcourses'>" + "Add Students to Courses</a>"
                            +"</div>"

                            + "<div class='bg-white'>"
                            + "<h3>Search Results for: " + fname + " " + lname + "</h3>" + "<table>" + "<tr>"
                            + "<tr><th>Firstname</th>"
                            + "<th>Lastname</th>"
                            + "<th>Course</th>"
                            + "<th>Points</th>"
                            + "<th>Course Description</th>"
                            + "</tr>");

                    out.println(htmlOutTop);

                    while (rs.next()) {

                        // String ID = rs.getString("ID");
                        String firstname = rs.getString("fname");
                        String lastname = rs.getString("lname");
                        String courseName = rs.getString("name");
                        String points = rs.getString("points");
                        String courseDescription = rs.getString("description");

                        out.println("<tr>"
                                //  + "<td>" + ID + "</td>"
                                + "<td>" + firstname + "</td>"
                                + "<td>" + lastname + "</td>"
                                + "<td>" + courseName + "</td>"
                                + "<td>" + points + "</td>"
                                + "<td>" + courseDescription + "</td>"
                                + "</tr>");

                    }

                    displayResetButton(out);

                    out.println("</h3></table>");

                        rs.close();
                        ps.close();
                    connection.close();

                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }


                    // resp.sendRedirect(req.getContextPath() + "/searchstudent"); //redirect back tio doget method so student list will show with new student, but reset button does this already

                }

        //Method for the new student form
        protected void searchStudentForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();

            String htmlOutForm = ("<br>"
                    + "<div class='form-container'>"
                    + "<form style='margin:5px;' action=/searchstudent method=POST>"
                    + "<label for=fname>First Name:</label>"
                    + "<input type=text id=fname name=fname required><br>"
                    + "<label for=lname>Last Name:</label>"
                    + "<input type=text id=lname name=lname required><br>"
                    + "<input type=submit value=Submit>"
                    + "</form>"
                    + "</div>"
                    + "<br>");

            out.println(htmlOutForm);

        }


        private void allStudents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();
            try {
                Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
                String studentSql = "SELECT * FROM students";
                PreparedStatement ps = connection.prepareStatement(studentSql);
                ResultSet rs = ps.executeQuery();

                String htmlOut = ("<html>"
                        + "<h3 class='table-heading' style='text-align:center'>Students</h3>"
                        + "<table><tr>"
                     //   + "<th>ID</th>"
                        + "<th>Firstname</th>"
                        + "<th>Lastname</th>"
                        + "<th>Town</th>"
                        + "<th>Hobby</th>"
                        + "</tr>");

                out.println(htmlOut);

                while (rs.next()) {
                    System.out.println(rs.getInt(1) + " " + rs.getString(2));

                   // String ID = rs.getString("ID");
                    String Firstname = rs.getString("fName");
                    String Lastname = rs.getString("lName");
                    String Town = rs.getString("town");
                    String Hobby = rs.getString("hobby");

                    out.println("<tr>"); //andra tables row med all table data
                  //  out.println("<td>" + ID + "</td>");
                    out.println("<td>" + Firstname + "</td>");
                    out.println("<td>" + Lastname + "</td>");
                    out.println("<td>" + Town + "</td>");
                    out.println("<td>" + Hobby + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("</body>");
                out.println("</html>");

                connection.close();

            } catch (
                    Exception e) {
                System.out.println(e);

            }
        }
            private static void displayResetButton (PrintWriter out){
                out.println("<form action=/searchstudent method=GET>");
                out.println("<input type= submit value=Reset>");
                out.println("</form></body></html>");
            }
        }



