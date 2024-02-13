package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name="dbconnector", urlPatterns = "/dbconnector")
public class DbConnector extends HttpServlet {

    private static String ip;
    private static int port;
    private static String db;
    private static String user;
    private static String password;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("index.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getParameter("fname"));
        System.out.println(req.getParameter("lname"));
        resp.sendRedirect("index.html");

    }

    //database connection for reusing
        protected static Connection getConnection(String ip, int port, String db, String user, String password) throws SQLException, ClassNotFoundException {

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:13306/gritacademy", "root", "");
        }


    }
