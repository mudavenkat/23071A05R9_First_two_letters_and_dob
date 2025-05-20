import java.io.*;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URLEncoder;

public class Register extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String username = req.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            out.println("<html><body><h3>Username is required</h3></body></html>");
            return;
        }

        // Loop through all parameters and store them as cookies
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();
            String value = req.getParameter(param);

            // Encode the cookie value to avoid invalid characters
            String encodedValue = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(username + "_" + param, encodedValue);
            res.addCookie(cookie);
        }

        // Generate key: first 2 letters of firstname + dob
        String firstname = req.getParameter("firstname");
        String dob = req.getParameter("dob");
        String key = "invalidkey";
        if (firstname != null && firstname.length() >= 2 && dob != null && dob.length() == 8) {
            key = firstname.substring(0, 2).toLowerCase() + dob;
        }

        // Response
        out.println("<html><body><center>");
        out.println("<h2>Registration Successful</h2>");
        out.printf("<p><b>Username:</b> %s</p>", username);
        out.printf("<p><b>Key:</b> %s</p>", key);
        out.println("</center></body></html>");
        out.close();
    }
}
