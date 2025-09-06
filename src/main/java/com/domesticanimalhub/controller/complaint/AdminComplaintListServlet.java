// src/main/java/com/domesticanimalhub/controller/complaint/AdminComplaintListServlet.java
package com.domesticanimalhub.controller.complaint;

import com.domesticanimalhub.dao.ComplaintDao;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;


public class AdminComplaintListServlet extends HttpServlet {
    private final ComplaintDao dao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User current = (User) req.getSession().getAttribute("user");
        // ✅ redirect to the real admin login tab page
        if (current == null || current.getUserRole() != UserRole.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/views/auth.jsp?tab=admin");
            return;
        }

        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 20);
        int offset = (page - 1) * size;

        try {
            req.setAttribute("items", dao.listAll(size, offset));
            req.setAttribute("counts", dao.getStatusCounts());
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            // ✅ forward to the correct JSP
            req.getRequestDispatcher("/views/admin/AdminComplaints.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
