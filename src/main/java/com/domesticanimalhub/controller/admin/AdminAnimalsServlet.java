package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.AnimalDao;
import com.domesticanimalhub.dao.AnimalImageDao;
import com.domesticanimalhub.model.Animal;
import com.domesticanimalhub.model.AnimalImage;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class AdminAnimalsServlet extends HttpServlet {

    private final AnimalDao animalDao = new AnimalDao();
    private final AnimalImageDao imageDao = new AnimalImageDao();

    /** Ensure the current session belongs to an admin. */
    private boolean ensureAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession s = req.getSession(false);
        if (s == null || (s.getAttribute("user") == null && s.getAttribute("admin") == null)) {
            resp.sendRedirect(req.getContextPath() + "/views/login.jsp?tab=admin");
            return false;
        }

        Object u = s.getAttribute("user");
        if (u instanceof User) {
            UserRole role = ((User) u).getUserRole();  // <-- enum, not String
            if (role != UserRole.ADMIN) {              // <-- enum comparison
                resp.sendError(403);
                return false;
            }
        } else if (s.getAttribute("admin") == null) {
            // if you also support a separate "admin" session flag from AdminLoginServlet
            resp.sendError(403);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        try {
            List<Animal> pending  = animalDao.listPending(50, 0);
            List<Animal> approved = animalDao.listApproved(50, 0);
            req.setAttribute("pending", pending);
            req.setAttribute("approved", approved);
            req.getRequestDispatcher("/views/admin/animals.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        String action = req.getParameter("action");
        try {
            int id = Integer.parseInt(req.getParameter("animalId"));
            switch (action) {
                case "approve":
                    animalDao.approve(id);
                    break;
                case "sold":
                    animalDao.markSold(id);
                    break;
                case "delete":
                    for (AnimalImage img : imageDao.listByAnimal(id)) {
                        imageDao.deleteById(img.getImageId());
                    }
                    Animal target = animalDao.findById(id);
                    if (target != null) {
                        animalDao.delete(id, target.getUserId());
                    }
                    break;
            }
            resp.sendRedirect(req.getContextPath() + "/admin/animals");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
