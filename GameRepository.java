package com.gamecollection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    public List<Game> findAll(String searchText) throws SQLException {
        String search = searchText == null ? "" : searchText.trim();
        String sql = """
            SELECT game_id, title, platform, genre, price, status
            FROM games
            WHERE title LIKE ? OR platform LIKE ? OR genre LIKE ?
            ORDER BY game_id DESC
            """;
        List<Game> games = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String pattern = "%" + search + "%";
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    games.add(new Game(
                        rs.getInt("game_id"),
                        rs.getString("title"),
                        rs.getString("platform"),
                        rs.getString("genre"),
                        rs.getBigDecimal("price"),
                        rs.getString("status")
                    ));
                }
            }
        }
        return games;
    }

    public int add(String title, String platform, String genre, BigDecimal price, String status) throws SQLException {
        String sql = "INSERT INTO games (title, platform, genre, price, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setString(2, platform);
            statement.setString(3, genre.isBlank() ? null : genre);
            statement.setBigDecimal(4, price);
            statement.setString(5, status);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public boolean update(int id, String title, String platform, String genre, BigDecimal price, String status)
            throws SQLException {
        String sql = """
            UPDATE games
            SET title = ?, platform = ?, genre = ?, price = ?, status = ?
            WHERE game_id = ?
            """;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, platform);
            statement.setString(3, genre.isBlank() ? null : genre);
            statement.setBigDecimal(4, price);
            statement.setString(5, status);
            statement.setInt(6, id);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM games WHERE game_id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }
}
