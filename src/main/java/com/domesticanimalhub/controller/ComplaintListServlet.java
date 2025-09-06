// src/main/java/com/domesticanimalhub/controller/ComplaintListServlet.java
package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.ComplaintDao;
import com.domesticanimalhub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/complaints/my") // <-- match the link in your navbar
public class ComplaintListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ComplaintDao dao = new ComplaintDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User current = (User) request.getSession().getAttribute("user");
        if (current == null) {
            response.sendRedirect(request.getContextPath() + "/views/auth.jsp");
            return;
        }

        int pageNum = parseInt(request.getParameter("page"), 1);
        int size = parseInt(request.getParameter("size"), 10);
        int offset = (pageNum - 1) * size;

        try {
            request.setAttribute("items", dao.listByUser(current.getUserId(), size, offset));
            request.setAttribute("page", pageNum);
            request.setAttribute("size", size);
            request.getRequestDispatcher("/views/MyComplaints.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
