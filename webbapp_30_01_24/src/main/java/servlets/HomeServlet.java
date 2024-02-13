package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name="homeservlet", urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
/*
    LEFT TO DO
    - lägg till credit för home page bilden
 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Connection connection = DbConnector.getConnection("localhost", 13306, "gritacademy", "root", "");

            PrintWriter out = resp.getWriter();

            String htmlOut = ("<html>"
                   + "<head><title>Home</title></head>"
                + "<link rel='stylesheet' type='text/css' href='styles.css'>"
                + "<script src=https://cdn.tailwindcss.com></script>"

                  + "<body style='background-image: url(\"/forest.jpg\"); background-size: cover; background-position: center; background-repeat: no-repeat;'>"
                    +"<body style='background-color:aquamarine;'>"

                   +"<p style='text-align:center; margin-top: 50px;'>"
                    +"<a href ='http://localhost:9999/searchstudent'>" + "Search Student |  </a>"
                    +"<a href ='http://localhost:9999/addstudent'>" + "Add Student |  </a>"
                    +"<a href ='http://localhost:9999/courses'>" + "Show Courses |  </a>"
                    +"<a href ='http://localhost:9999/studentcourses'>" + "Show Students Courses</a>"
                   // +"</p>"
                    +"</body>"
                    +"</html>");

                    out.println(htmlOut);

        connection.close();

    } catch(
    Exception e)

    {
        System.out.println(e);

    }
}

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       // System.out.println("redirecting");
   //     getServletContext().getRequestDispatcher("dbconnect").forward(req,resp);

    }
}
