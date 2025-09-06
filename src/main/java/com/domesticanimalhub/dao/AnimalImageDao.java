package com.domesticanimalhub.dao;

import com.domesticanimalhub.model.AnimalImage;
import com.domesticanimalhub.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalImageDao {

    public int addImage(int animalId, byte[] data) throws SQLException {
        String sql = "INSERT INTO animal_images (animal_id, image_data) VALUES (?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, animalId);
            ps.setBytes(2, data);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    /** Preferred name (used by servlets I provided earlier). */
    public boolean deleteById(int imageId) throws SQLException {
        return deleteImage(imageId);
    }

    public boolean deleteImage(int imageId) throws SQLException {
        String sql = "DELETE FROM animal_images WHERE image_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Remove all images for a listing (handy if you ever add a “replace all” mode). */
    public void deleteAllForAnimal(int animalId) throws SQLException {
        String sql = "DELETE FROM animal_images WHERE animal_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            ps.executeUpdate();
        }
    }

    public List<AnimalImage> listByAnimal(int animalId) throws SQLException {
        String sql = "SELECT * FROM animal_images WHERE animal_id=? ORDER BY uploaded_at ASC";
        List<AnimalImage> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AnimalImage ai = new AnimalImage(
                            rs.getInt("image_id"),
                            rs.getInt("animal_id"),
                            rs.getBytes("image_data"),
                            rs.getTimestamp("uploaded_at")
                    );
                    out.add(ai);
                }
            }
        }
        return out;
    }

    public AnimalImage findById(int imageId) throws SQLException {
        String sql = "SELECT * FROM animal_images WHERE image_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AnimalImage(
                            rs.getInt("image_id"),
                            rs.getInt("animal_id"),
                            rs.getBytes("image_data"),
                            rs.getTimestamp("uploaded_at")
                    );
                }
            }
        }
        return null;
    }
}
