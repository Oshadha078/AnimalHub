package com.domesticanimalhub.dao;

import com.domesticanimalhub.model.Order;
import com.domesticanimalhub.model.OrderStatus;
import com.domesticanimalhub.util.DB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    // CREATE
    public int create(Order o) throws SQLException {
        String sql = "INSERT INTO orders (animal_id,buyer_id,price_paid,status) VALUES (?,?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getAnimalId());
            ps.setInt(2, o.getBuyerId());
            ps.setBigDecimal(3, o.getPricePaid()!=null ? o.getPricePaid() : BigDecimal.ZERO);
            ps.setString(4, o.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Order findById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Order> listByBuyer(int buyerId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM orders WHERE buyer_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Order> out = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, buyerId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public boolean updateStatus(int orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status=? WHERE order_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("order_id"),
            rs.getInt("animal_id"),
            rs.getInt("buyer_id"),
            rs.getBigDecimal("price_paid"),
            OrderStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
