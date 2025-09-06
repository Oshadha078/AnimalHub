package com.domesticanimalhub.controller;

import com.domesticanimalhub.dao.AnimalImageDao;
import com.domesticanimalhub.model.AnimalImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ImageServlet extends HttpServlet {

    private final AnimalImageDao imageDao = new AnimalImageDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String imageIdStr = req.getParameter("imageId");
            String animalIdStr = req.getParameter("animalId");

            byte[] data = null;

            if (imageIdStr != null) {
                AnimalImage img = imageDao.findById(Integer.parseInt(imageIdStr));
                if (img != null) data = img.getImageData();
            } else if (animalIdStr != null) {
                List<AnimalImage> list = imageDao.listByAnimal(Integer.parseInt(animalIdStr));
                if (!list.isEmpty()) data = list.get(0).getImageData();
            }

            if (data == null) { resp.sendError(404); return; }

            resp.setContentType("image/jpeg"); // default; browser will cope even if PNG/WEBP
            resp.setHeader("Cache-Control", "max-age=86400");
            resp.getOutputStream().write(data);
        } catch (Exception e) {
            resp.sendError(500);
        }
    }
}
