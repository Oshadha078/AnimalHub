// src/main/java/com/domesticanimalhub/controller/admin/ContentImageServlet.java
package com.domesticanimalhub.controller.admin;

import com.domesticanimalhub.dao.EducationalContentDao;
import com.domesticanimalhub.model.EducationalContent;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ContentImageServlet extends HttpServlet {
    private final EducationalContentDao dao = new EducationalContentDao();

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            EducationalContent c = dao.findById(id);
            if (c == null || c.getImage() == null) { resp.sendError(404); return; }
            resp.setContentType("image/jpeg");
            resp.setHeader("Cache-Control", "public, max-age=86400");
            resp.setContentLength(c.getImage().length);
            resp.getOutputStream().write(c.getImage());
        } catch (Exception e) {
            resp.sendError(500);
        }
    }
}
