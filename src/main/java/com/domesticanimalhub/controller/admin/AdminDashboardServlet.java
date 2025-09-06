package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.AnimalDao;
import com.domesticanimalhub.dao.EducationalContentDao;
import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.Animal;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;   // <-- import the enum
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class AdminDashboardServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final AnimalDao animalDao = new AnimalDao();
    private final com.domesticanimalhub.dao.EducationalContentDao contentDao = new com.domesticanimalhub.dao.EducationalContentDao();

    private boolean ensureAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || (s.getAttribute("user") == null && s.getAttribute("admin") == null)) {
            resp.sendRedirect(req.getContextPath()+"/views/login.jsp?tab=admin");
            return false;
        }
        Object u = s.getAttribute("user");
        if (u instanceof User) {
            UserRole role = ((User) u).getUserRole();   // enum, not String
            if (role != UserRole.ADMIN) {               // compare enums
                resp.sendError(403);
                return false;
            }
        } else if (s.getAttribute("admin") == null) {
            // allow a separate "admin" session flag if you set it during AdminLogin
            resp.sendError(403);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        try {
            int totalUsers     = userDao.countAll();     // requires method below
            int pendingAnimals = animalDao.countPending();
            int approvedAnimals= animalDao.countApproved();
            int totalContent = contentDao.countAll();

            List<Animal> latestPending = animalDao.listPending(6, 0);

            req.setAttribute("totalUsers", totalUsers);
            req.setAttribute("pendingAnimals", pendingAnimals);
            req.setAttribute("approvedAnimals", approvedAnimals);
            req.setAttribute("totalContent", totalContent);
            req.setAttribute("latestPending", latestPending);

            req.getRequestDispatcher("/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
