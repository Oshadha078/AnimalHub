// src/main/java/com/domesticanimalhub/controller/complaint/ComplaintCreateServlet.java
package com.domesticanimalhub.controller.complaint;

import com.domesticanimalhub.dao.ComplaintDao;
import com.domesticanimalhub.model.Complaint;
import com.domesticanimalhub.model.ComplaintStatus;
import com.domesticanimalhub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/complaints/new") // safe even with web.xml (mapping will still work)
public class ComplaintCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ComplaintDao dao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User current = (User) req.getSession().getAttribute("user");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/views/auth.jsp");
            return;
        }
        req.getRequestDispatcher("/views/complaints/new.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User current = (User) req.getSession().getAttribute("user");
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/views/auth.jsp");
            return;
        }

        String description = req.getParameter("description");
        if (description == null || description.trim().isEmpty()) {
            req.setAttribute("error", "Please enter your complaint description.");
            req.getRequestDispatcher("/views/complaints/new.jsp").forward(req, resp);
            return;
        }

        Complaint c = new Complaint();
        c.setUserId(current.getUserId());
        c.setDescription(description.trim());
        c.setStatus(ComplaintStatus.OPEN);

        try {
            int id = dao.create(c);
            if (id <= 0) {
                req.setAttribute("error", "Could not save your complaint. Please try again.");
                req.getRequestDispatcher("/views/complaints/new.jsp").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("flashSuccess", "Complaint submitted successfully (ID: " + id + ").");
            resp.sendRedirect(req.getContextPath() + "/complaints/my");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
