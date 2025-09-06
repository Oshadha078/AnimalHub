package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.UserDao;
import com.domesticanimalhub.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AvatarServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("userId");

            // if no param, default to current session user (handy for profile page)
            if (idParam == null) {
                HttpSession s = req.getSession(false);
                if (s != null && s.getAttribute("user") != null) {
                    idParam = String.valueOf(((User) s.getAttribute("user")).getUserId());
                }
            }
            if (idParam == null) { resp.sendError(404); return; }

            int userId = Integer.parseInt(idParam);
            User u = userDao.findById(userId);
            byte[] img = (u == null) ? null : u.getProfileImage();
            if (img == null || img.length == 0) { resp.sendError(404); return; }

            resp.setContentType("image/jpeg");
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.getOutputStream().write(img);
        } catch (Exception e) {
            resp.sendError(500);
        }
    }
}
