package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name="studentservlet", urlPatterns = "/addstudent")
    public class StudentServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


            try {
                Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
                String studentSql = "SELECT * FROM students";
                PreparedStatement ps = connection.prepareStatement(studentSql);
                ResultSet rs = ps.executeQuery();

                //resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();

               // String errorMessage = "<html><p>" + req.getAttribute("message") + "</p></html>"; // Retrieve message from the request attribute

                String htmlOutTop = ("<html>"
                      //  + errorMessage
                        +"<head><title>Add student</title></head>"
                        +"<link rel='stylesheet' type='text/css' href='styles.css'>"
                        + "<script src=https://cdn.tailwindcss.com></script>"
                        +"<body><p style='text-align:center'>"

                        +"<div class='navbar'>"
                        +"<a color: #d3d3d3 href ='home'>" + "Home |  </a>"
                        +"<a href ='searchstudent'>" + "Search Student |  </a>"
                        +"<a href ='courses'>" + "Add Courses |  </a>"
                        +"<a href ='studentcourses'>" + "Add course to student</a>"
                        +"</div>");



                    String htmlOutBottom = (
                        "<h3 class='table-heading' style='text-align:center'>Add new student</h3>"
                        +"<table>"
                        +"<tr>"
                        +"<th>ID</th>"
                        +"<th>Name</th>"
                        +"<th>Town</th>"
                        +"<th>Hobby</th>"
                        +"</tr>");

                out.println(htmlOutTop);

                formNewStudent(req, resp);


                out.println("<div class='bg-white'>");
                out.println(htmlOutBottom);




                while (rs.next()) {
                    System.out.println(rs.getInt(1) + " " + rs.getString(2));

                    int Id = rs.getInt(1);
                    String Name = rs.getString("fName") + " " + rs.getString("lName");
                    String Town = rs.getString("town");
                    String Hobby = rs.getString("hobby");

                    out.println("<tr>"); //andra tables row med all table data
                    out.println("<td>" + Id + "</td>");
                    out.println("<td>" + Name + "</td>");
                    out.println("<td>" + Town + "</td>");
                    out.println("<td>" + Hobby + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("</body>");
                out.println("</html>");

                ps.close();
                connection.close();

            } catch (
                    Exception e) {
                System.out.println(e);

            }
        }

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();

            String fname = req.getParameter("fname"); //getting the form parameters
            String lname = req.getParameter("lname");
            String town = req.getParameter("town");
            String hobby = req.getParameter("hobby");


            try {
                Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userINSERT", "insert");
                String newStudentSql = "INSERT INTO students (fname, lname, town, hobby) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(newStudentSql);
                ps.setString(1,fname);
                ps.setString(2,lname);
                ps.setString(3,town);
                ps.setString(4,hobby);

                int rowsInserted = ps.executeUpdate();

                if(rowsInserted > 0 ) {
                    System.out.println("new student was added to the database");

                   // String message = "new student was added to the database";

                    //GÖR DETTA ATT DET LOOPAR
                //    req.setAttribute("message", message); //holding the message
                 //   req.getRequestDispatcher("/addstudent").forward(req, resp); //should send  back to doGet method

                } else {
                    System.out.println("NOPE - student was not added to database");
                }

                ps.close();
                connection.close();



            } catch(ClassNotFoundException | SQLException e)

            {throw new RuntimeException(e);}

            resp.sendRedirect(req.getContextPath() + "/addstudent"); //redirect back tio soget method so student list will show with new student
    }

        //Method for the new student form
        protected void formNewStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();

            String htmlOutForm = ("<br>"
                    + "<div class='form-container'>"
                    + "<form style='margin:auto;' action=/addstudent method=POST>"
                    + "<label for=fname>First Name:</label>"
                    + "<input type=text id=fname name=fname required><br>"
                    + "<label for=lname>Last Name:</label>"
                    + "<input type=text id=lname name=lname required><br>"
                    + "<label for=town>Town:</label>"
                    + "<input type=text id=town name=town><br>"
                    + "<label for=hobby>Hobby</label>"
                    + "<input type=text id=hobby name=hobby><br>"
                    + "<input type=submit value=Submit>"
                    + "</form>"
                    + "</div>"
                    + "<br>");

            out.println(htmlOutForm);
        }
    }
