package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import com.domesticanimalhub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name="SignupServlet", urlPatterns={"/SignupServlet"})
public class SignupServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phoneNumber");
        String password = req.getParameter("password");

        try {
            if (email == null || email.isBlank() || password == null || password.isBlank() || fullName == null || fullName.isBlank()) {
                req.setAttribute("signupError", "Please fill all required fields.");
                req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
                return;
            }
            if (userDao.emailExists(email)) {
                req.setAttribute("signupError", "Email already registered.");
                req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
                return;
            }

            User u = new User();
            u.setFullName(fullName);
            u.setEmail(email);
            u.setPhoneNumber(phone);
            u.setPasswordHash(PasswordUtil.hash(password));
            u.setUserRole(UserRole.CUSTOMER);
            u.setVerified(true); // local dev: mark verified

            int id = userDao.create(u);
            if (id <= 0) {
                req.setAttribute("signupError", "Unable to create account. Try again.");
                req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("flashSuccess", "Registration successful! Please login.");
            req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("signupError", "Registration failed. Please try again.");
            req.getRequestDispatcher("/views/auth.jsp").forward(req, resp);
        }
    }
}
