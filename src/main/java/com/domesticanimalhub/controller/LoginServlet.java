package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name="LoginServlet", urlPatterns={"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");
        String remember = req.getParameter("remember");

        try {
            User user = userDao.findByEmail(email);
            if (user == null || !PasswordUtil.check(password, user.getPasswordHash())) {
                req.setAttribute("error", "Invalid email or password.");
                req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
                return;
            }

            // Session
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(60 * 60); // 1 hour

            // Remember email cookie
            if ("on".equalsIgnoreCase(remember)) {
                Cookie cookie = new Cookie("rememberEmail", email);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("rememberEmail", "");
                cookie.setMaxAge(0);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);
            }

            // Go to redirect or default home
            String target = (redirect != null && !redirect.isEmpty()) ? redirect : (req.getContextPath() + "/user/home.jsp");
            resp.sendRedirect(target);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Login failed. Please try again.");
            req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
        }
    }
}
