package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import com.domesticanimalhub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AdminLoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    // Hardcoded fallback credentials
    private static final String DEFAULT_ADMIN_EMAIL = "admin@gmail.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "Mobitel#123";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        try {
            boolean authenticated = false;
            User u = null;

            // Check hardcoded credentials first
            if (DEFAULT_ADMIN_EMAIL.equals(email) && DEFAULT_ADMIN_PASSWORD.equals(password)) {
                u = new User();
                u.setEmail(email);
                u.setFullName("System Admin");
                u.setUserRole(UserRole.ADMIN);
                authenticated = true;
            } else {
                // Fallback: check against DB
                u = userDao.findByEmail(email);
                if (u != null && u.getUserRole() == UserRole.ADMIN
                        && PasswordUtil.check(password, u.getPasswordHash())) {
                    authenticated = true;
                }
            }

            if (authenticated && u != null) {
                HttpSession s = req.getSession(true);
                s.setAttribute("user", u);
                s.setAttribute("admin", Boolean.TRUE);

                String target = (redirect != null && redirect.startsWith("/admin"))
                        ? redirect
                        : "/admin/dashboard";

                resp.sendRedirect(req.getContextPath() + target);
                return;
            }

            // fail -> back to auth.jsp with admin tab active and error message
            req.setAttribute("adminError", "Invalid admin credentials or not an admin user.");
            req.setAttribute("activeTab", "admin");
            req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("activeTab", "admin");
        req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
    }
}
