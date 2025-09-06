package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;   // <-- import the enum
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class AdminUsersServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    private boolean ensureAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || (s.getAttribute("user") == null && s.getAttribute("admin") == null)) {
            resp.sendRedirect(req.getContextPath()+"/views/login.jsp?tab=admin");
            return false;
        }
        Object u = s.getAttribute("user");
        if (u instanceof User) {
            UserRole role = ((User) u).getUserRole();   // <-- enum, not String
            if (role != UserRole.ADMIN) {               // <-- compare enums
                resp.sendError(403);
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        try {
            String q = req.getParameter("q");
            List<User> users = userDao.listAll(100, 0, q);
            req.setAttribute("users", users);
            req.setAttribute("q", q == null ? "" : q);
            req.getRequestDispatcher("/views/admin/users.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        String action = req.getParameter("action");
        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            switch (action) {
                case "verify": {
                    boolean v = Boolean.parseBoolean(req.getParameter("value"));
                    userDao.setVerified(userId, v);
                    break;
                }
                case "role": {
                    // value comes from <select name="role">ADMIN / CUSTOMER
                    String roleParam = req.getParameter("role");
                    UserRole newRole = UserRole.valueOf(roleParam.toUpperCase());
                    userDao.updateRole(userId, newRole);   // <-- call enum version
                    break;
                }
                case "delete": {
                    userDao.delete(userId);
                    break;
                }
            }
            resp.sendRedirect(req.getContextPath()+"/admin/users");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
