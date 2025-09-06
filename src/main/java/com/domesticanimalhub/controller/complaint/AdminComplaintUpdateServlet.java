// src/main/java/com/domesticanimalhub/controller/complaint/AdminComplaintUpdateServlet.java
package com.domesticanimalhub.controller.complaint;

import com.domesticanimalhub.dao.ComplaintDao;
import com.domesticanimalhub.model.ComplaintStatus;
import com.domesticanimalhub.model.User;
import com.domesticanimalhub.model.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AdminComplaintUpdateServlet extends HttpServlet {
    private final ComplaintDao dao = new ComplaintDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User current = (User) req.getSession().getAttribute("user");
        if (current == null || current.getUserRole() != UserRole.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/views/auth.jsp?tab=admin");
            return;
        }

        String idStr = req.getParameter("complaintId");
        String statusStr = req.getParameter("status");

        try {
            int id = Integer.parseInt(idStr);
            ComplaintStatus status = ComplaintStatus.valueOf(statusStr);
            boolean ok = dao.updateStatus(id, status);
            req.getSession().setAttribute("flashSuccess", ok ? "Updated complaint #" + id + " to " + status : "Update failed.");
            resp.sendRedirect(req.getContextPath() + "/admin/complaints");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
