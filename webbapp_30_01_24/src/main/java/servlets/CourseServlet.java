package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/courses") //
public class CourseServlet extends HttpServlet { //servlet klassen


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //dependency måste läggas in i POM
        //PASTA IN KODEN FRÅN SLIDSEN , men bäst att skapa detta i en egen klass
        //skapa 3 servletss i sina egna klasser
        //bäst att skapa objekt som hanterar databas hantering
        //anropa queries istället för att skapa nya varje gång


        try {
            Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userSELECT", "user");
            String coursesSql = "SELECT * FROM courses";
            PreparedStatement ps = connection.prepareStatement(coursesSql);
            ResultSet rs = ps.executeQuery();

            PrintWriter out = resp.getWriter();
            String htmlOut = ("<html>" + "<head><title>Courses</title></head>"
                    +"<link rel='stylesheet' type='text/css' href='styles.css'>"
                    + "<script src=https://cdn.tailwindcss.com></script>"
                    + "<body><p style='text-align:center'>"

                    +"<div class='navbar'>"
                    + "<a href ='home'>" + "Home |  </a>"
                    + "<a href ='searchstudent'>" + "Search Student |  </a>"
                    + "<a href ='addstudent'>" + "Add Students |  </a>"
                    + "<a href ='studentcourses'>" + "Add Students to Courses</a>"
                    + "</div>");

            out.println(htmlOut);

            newCourseForm(req, resp);

            out.println("<div class='bg-white'>");
            out.println("<h3 class='table-heading'>Courses</h3>" + "<table><tr><th>ID</td><th>Course</td><th>Points</td><th>Description</td></tr>");

            while (rs.next()) {

                int id = rs.getInt(1); //spara resultatet i id
                String courseName = rs.getString("name");
                String points = rs.getString("points");
                String description = rs.getString("description");

                String htmlOutEnd = ("<tr>" + "<td>" + id + "</td>"
                        + "<td>" + courseName + "</td>"
                        + "<td>" + points + "</td>"
                        + "<td>" + description + "</td>" + "</tr>");
                out.println(htmlOutEnd);
            }
            out.println("</table>");


        //    out.println("</div>");
            out.println("</body>" + "</html>");

            ps.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // System.out.println("redirecting");
        //     getServletContext().getRequestDispatcher("dbconnect").forward(req,resp);
        // formNewStudent(req, resp);'

        String name = req.getParameter("name"); //getting the form parameters
        int points = Integer.parseInt(req.getParameter("points"));
        String description = req.getParameter("description");


        try {
            Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "userINSERT", "insert");
            String newStudentSql = "INSERT INTO courses (name, points, description) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(newStudentSql);
            ps.setString(1,name);
            ps.setInt(2,points);
            ps.setString(3,description);

            int rowsInserted = ps.executeUpdate();

            if(rowsInserted > 0 ) {
                System.out.println("new course was added to the database");
            } else {
                System.out.println("NOPE - course was not added to database");
            }

            ps.close();
            connection.close();



        } catch(ClassNotFoundException | SQLException e)
        {throw new RuntimeException(e);}

        resp.sendRedirect(req.getContextPath() + "/courses"); //redirect back tio soget method so student list will show with new student
    }

    //Method for the new student form
    protected void newCourseForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String htmlOutForm = ("<br>"
                + "<div class='form-container'>"
                + "<form style='margin:5px;' action=/courses method=POST>"
                + "<label for=name>Course Name:</label>"
                + "<input type=text id=name name=name required><br>"
                + "<label for=points>Points:</label>"
                + "<input type=text id=points name=points required><br>"
                + "<label for=description>Description:</label>"
                + "<input type=text id=description name=description ><br>"
                + "<input type=submit value=Submit>"
                + "</form>"
                + "</div>"
                + "<br>");

        out.println(htmlOutForm);
    }
}


