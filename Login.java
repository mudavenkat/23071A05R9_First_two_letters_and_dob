import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.URLDecoder;

public class Login extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String username = req.getParameter("username");
        String inputPassword = req.getParameter("password");

        Cookie[] cookies = req.getCookies();
        Map<String, String> userData = new HashMap<>();

        if (cookies != null) {
            for (Cookie c : cookies) {
                String name = c.getName();
                if (name.startsWith(username + "_")) {
                    String key = name.substring((username + "_").length());
                    String decodedValue = URLDecoder.decode(c.getValue(), "UTF-8");
                    userData.put(key, decodedValue);
                }
            }
        }

        out.println("<html><body><center>");

        if (!userData.containsKey("password")) {
            out.println("<h3>User not registered.</h3>");
            out.println("<a href='register.html'>Register Here</a>");
        } else {
            String storedPassword = userData.get("password");

            if (storedPassword.equals(inputPassword)) {
                // Generate key
                String firstname = userData.getOrDefault("firstname", "Unknown");
                String dob = userData.getOrDefault("dob", "Unknown");
                String key = "invalidkey";
                if (firstname.length() >= 2 && dob.length() == 8) {
                    key = firstname.substring(0, 2).toLowerCase() + dob;
                }

                out.println("<h2>Welcome Back!</h2>");
                out.printf("<p><b>Username:</b> %s</p>", username);

                List<String> keyList = new ArrayList<>(userData.keySet());
                for (String k : keyList) {
                    if (!k.equals("password")) {
                        out.printf("<p><b>%s:</b> %s</p>", k, userData.get(k));
                    }
                }

                out.printf("<p><b>Key:</b> %s</p>", key);
            } else {
                out.println("<h3>Incorrect password.</h3>");
            }
        }

        out.println("</center></body></html>");
        out.close();
    }
}
