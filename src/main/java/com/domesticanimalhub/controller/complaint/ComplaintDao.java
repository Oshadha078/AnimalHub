// src/main/java/com/domesticanimalhub/dao/ComplaintDao.java
package com.domesticanimalhub.controller.complaint;

import com.domesticanimalhub.model.Complaint;
import com.domesticanimalhub.model.ComplaintStatus;
import com.domesticanimalhub.util.DB;

import java.sql.*;
import java.util.*;

public class ComplaintDao {

    public int create(Complaint c) throws SQLException {
        String sql = "INSERT INTO complaints (user_id, description, status) VALUES (?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getStatus().name());

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            // If your DB util sets autoCommit=false, make sure we commit the tx
            if (!con.getAutoCommit()) {
                con.commit();
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Complaint findById(int id) throws SQLException {
        String sql = "SELECT * FROM complaints WHERE complaint_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Complaint> listByUser(int userId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM complaints WHERE user_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Complaint> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public List<Complaint> listAll(int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM complaints ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Complaint> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public boolean updateStatus(int complaintId, ComplaintStatus status) throws SQLException {
        String sql = "UPDATE complaints SET status=? WHERE complaint_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, complaintId);
            int affected = ps.executeUpdate();
            if (!con.getAutoCommit()) con.commit();
            return affected > 0;
        }
    }

    public boolean delete(int complaintId) throws SQLException {
        String sql = "DELETE FROM complaints WHERE complaint_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            int affected = ps.executeUpdate();
            if (!con.getAutoCommit()) con.commit();
            return affected > 0;
        }
    }

    public Map<String, Integer> getStatusCounts() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS total FROM complaints GROUP BY status";
        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("total"));
            }
        }
        return map;
    }

    private Complaint map(ResultSet rs) throws SQLException {
        return new Complaint(
            rs.getInt("complaint_id"),
            rs.getInt("user_id"),
            rs.getString("description"),
            ComplaintStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
