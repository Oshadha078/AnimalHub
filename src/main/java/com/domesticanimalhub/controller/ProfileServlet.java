package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;

public class ProfileServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?redirect=" + req.getRequestURI());
            return;
        }
        try {
            User sessionUser = (User) s.getAttribute("user");
            User fresh = userDao.findById(sessionUser.getUserId()); // show latest
            req.setAttribute("profile", fresh);
            req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?redirect=" + req.getRequestURI());
            return;
        }
        User sessUser = (User) s.getAttribute("user");
        String action = req.getParameter("action"); // "update" or "password"

        try {
            if ("password".equalsIgnoreCase(action)) {
                handlePasswordChange(req, sessUser);
            } else { // default: profile update
                handleProfileUpdate(req, sessUser);
            }
            // refresh session + request attribute
            User fresh = userDao.findById(sessUser.getUserId());
            s.setAttribute("user", fresh);
            req.setAttribute("profile", fresh);
        } catch (Exception e) {
            req.setAttribute("err", "Something went wrong. Please try again.");
            e.printStackTrace();
        }
        req.getRequestDispatcher("/user/profile.jsp").forward(req, resp);
    }

    private void handleProfileUpdate(HttpServletRequest req, User sessUser) throws Exception {
        User current = userDao.findById(sessUser.getUserId());

        String fullName = req.getParameter("fullName");
        String phone    = req.getParameter("phoneNumber");
        String address  = req.getParameter("address");

        // keep current values if empty
        if (fullName == null || fullName.isBlank()) fullName = current.getFullName();
        if (phone == null) phone = current.getPhoneNumber();
        if (address == null) address = current.getAddress();

        byte[] avatar = current.getProfileImage(); // keep existing by default

        Part img = null;
        try { img = req.getPart("profileImage"); } catch (IllegalStateException ignore) {}
        if (img != null && img.getSize() > 0 && img.getContentType() != null && img.getContentType().startsWith("image/")) {
            try (InputStream in = img.getInputStream()) {
                avatar = in.readAllBytes();
            }
        }

        User updated = new User();
        updated.setUserId(sessUser.getUserId());
        updated.setFullName(fullName);
        updated.setPhoneNumber(phone);
        updated.setAddress(address);
        updated.setUserRole(current.getUserRole()); // unchanged
        updated.setVerified(current.isVerified());
        updated.setProfileImage(avatar);

        boolean ok = userDao.update(updated);
        if (ok) req.setAttribute("msg", "Profile updated successfully.");
        else    req.setAttribute("err", "Could not update profile.");
    }

    private void handlePasswordChange(HttpServletRequest req, User sessUser) throws Exception {
        String currentPw = req.getParameter("currentPassword");
        String newPw     = req.getParameter("newPassword");
        String confirmPw = req.getParameter("confirmPassword");

        User dbUser = userDao.findById(sessUser.getUserId());
        if (!PasswordUtil.check(currentPw, dbUser.getPasswordHash())) {
            req.setAttribute("errPw", "Current password is incorrect.");
            return;
        }
        if (newPw == null || newPw.length() < 6) {
            req.setAttribute("errPw", "New password must be at least 6 characters.");
            return;
        }
        if (!newPw.equals(confirmPw)) {
            req.setAttribute("errPw", "New password and confirmation do not match.");
            return;
        }
        boolean ok = userDao.updatePassword(dbUser.getUserId(), PasswordUtil.hash(newPw));
        if (ok) req.setAttribute("msgPw", "Password updated.");
        else    req.setAttribute("errPw", "Failed to update password.");
    }
}
