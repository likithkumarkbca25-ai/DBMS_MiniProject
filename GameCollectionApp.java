package com.gamecollection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class GameCollectionApp extends JFrame {
    private final GameRepository repository = new GameRepository();
    private final JTextField titleField = new JTextField(18);
    private final JTextField platformField = new JTextField(18);
    private final JTextField genreField = new JTextField(18);
    private final JTextField priceField = new JTextField(18);
    private final JComboBox<String> statusBox = new JComboBox<>(
            new String[]{"Not Played", "Playing", "Completed", "On Hold"});
    private final JTextField searchField = new JTextField(20);
    private final JLabel messageLabel = new JLabel("Ready.");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Title", "Platform", "Genre", "Price (₹)", "Status"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable gamesTable = new JTable(tableModel);
    private int selectedGameId = -1;

    public GameCollectionApp() {
        super("Game Collection Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 590));
        setLocationRelativeTo(null);
        buildUi();
        loadGames("");
        testDatabase();
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        JLabel heading = new JLabel("Game Collection Management");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 24f));
        JLabel subtitle = new JLabel("Add, search, update and remove games — stored in MySQL");
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.add(heading);
        titlePanel.add(subtitle);
        root.add(titlePanel, BorderLayout.NORTH);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBorder(BorderFactory.createTitledBorder("Game details"));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 7, 6, 7);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        addFormRow(form, c, 0, "Title *", titleField);
        addFormRow(form, c, 1, "Platform *", platformField);
        addFormRow(form, c, 2, "Genre", genreField);
        addFormRow(form, c, 3, "Price (₹) *", priceField);
        addFormRow(form, c, 4, "Status", statusBox);
        left.add(form, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 2, 8, 8));
        JButton addButton = new JButton("Add Game");
        JButton updateButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear Form");
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);
        left.add(buttons, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout(8, 8));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Saved games"));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search title/platform/genre:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesTable.setRowHeight(24);
        gamesTable.setAutoCreateRowSorter(true);
        tablePanel.add(new JScrollPane(gamesTable), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, tablePanel);
        split.setResizeWeight(0.38);
        split.setDividerLocation(340);
        root.add(split, BorderLayout.CENTER);

        messageLabel.setBorder(new EmptyBorder(4, 2, 0, 2));
        root.add(messageLabel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGame());
        updateButton.addActionListener(e -> updateGame());
        deleteButton.addActionListener(e -> deleteGame());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> loadGames(searchField.getText()));
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadGames("");
        });
        searchField.addActionListener(e -> loadGames(searchField.getText()));

        gamesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && gamesTable.getSelectedRow() >= 0) {
                int viewRow = gamesTable.getSelectedRow();
                int row = gamesTable.convertRowIndexToModel(viewRow);
                selectedGameId = (int) tableModel.getValueAt(row, 0);
                titleField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
                platformField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
                genreField.setText(String.valueOf(tableModel.getValueAt(row, 3)).equals("null")
                        ? "" : String.valueOf(tableModel.getValueAt(row, 3)));
                priceField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                statusBox.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 5)));
                messageLabel.setText("Selected game ID " + selectedGameId + ".");
            }
        });
    }

    private void addFormRow(JPanel panel, GridBagConstraints c, int row, String label, Component field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(field, c);
    }

    private void testDatabase() {
        try {
            Database.testConnection();
            messageLabel.setText("Connected to MySQL successfully.");
        } catch (SQLException ex) {
            messageLabel.setText("MySQL connection failed. Check db.properties and ensure MySQL is running.");
        }
    }

    private void loadGames(String search) {
        try {
            List<Game> games = repository.findAll(search);
            tableModel.setRowCount(0);
            for (Game game : games) {
                tableModel.addRow(new Object[]{
                    game.getId(), game.getTitle(), game.getPlatform(), game.getGenre(),
                    game.getPrice() == null ? "" : game.getPrice().toPlainString(), game.getStatus()
                });
            }
            messageLabel.setText("Loaded " + games.size() + " game(s) from MySQL.");
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void addGame() {
        GameInput input = readAndValidateForm();
        if (input == null) return;
        try {
            int id = repository.add(input.title, input.platform, input.genre, input.price, input.status);
            clearForm();
            loadGames(searchField.getText());
            messageLabel.setText("Game saved successfully. New game ID: " + id);
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void updateGame() {
        if (selectedGameId < 0) {
            showMessage("Select a game from the table before updating.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        GameInput input = readAndValidateForm();
        if (input == null) return;
        try {
            boolean updated = repository.update(selectedGameId, input.title, input.platform,
                    input.genre, input.price, input.status);
            loadGames(searchField.getText());
            messageLabel.setText(updated ? "Game updated successfully." : "No game was updated.");
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void deleteGame() {
        if (selectedGameId < 0) {
            showMessage("Select a game from the table before deleting.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete the selected game (ID " + selectedGameId + ")?",
                "Confirm deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            boolean deleted = repository.delete(selectedGameId);
            clearForm();
            loadGames(searchField.getText());
            messageLabel.setText(deleted ? "Game deleted from MySQL." : "No game was deleted.");
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private GameInput readAndValidateForm() {
        String title = titleField.getText().trim();
        String platform = platformField.getText().trim();
        String genre = genreField.getText().trim();
        String priceText = priceField.getText().trim();
        String status = String.valueOf(statusBox.getSelectedItem());

        if (title.isEmpty()) {
            showMessage("Title is required.", JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return null;
        }
        if (title.length() > 100) {
            showMessage("Title must be 100 characters or fewer.", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (platform.isEmpty()) {
            showMessage("Platform is required.", JOptionPane.WARNING_MESSAGE);
            platformField.requestFocus();
            return null;
        }
        if (platform.length() > 50 || genre.length() > 50) {
            showMessage("Platform and genre must be 50 characters or fewer.", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        BigDecimal price;
        try {
            price = new BigDecimal(priceText);
            if (price.signum() < 0 || price.scale() > 2 || price.precision() > 10) {
                showMessage("Price must be non-negative and have at most 2 decimal places.", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException ex) {
            showMessage("Enter a valid price, for example 1499 or 1499.50.", JOptionPane.WARNING_MESSAGE);
            priceField.requestFocus();
            return null;
        }
        return new GameInput(title, platform, genre, price, status);
    }

    private void clearForm() {
        selectedGameId = -1;
        gamesTable.clearSelection();
        titleField.setText("");
        platformField.setText("");
        genreField.setText("");
        priceField.setText("");
        statusBox.setSelectedItem("Not Played");
        messageLabel.setText("Form cleared.");
        titleField.requestFocus();
    }

    private void showDatabaseError(SQLException ex) {
        String detail = ex.getMessage() == null ? "Unknown database error." : ex.getMessage();
        messageLabel.setText("Database operation failed: " + detail);
        JOptionPane.showMessageDialog(this,
                "Could not complete the database operation.\n\n" + detail
                + "\n\nCheck that MySQL is running and db.properties is correct.",
                "Database error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, int type) {
        JOptionPane.showMessageDialog(this, message, "Input check", type);
    }

    private record GameInput(String title, String platform, String genre, BigDecimal price, String status) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new GameCollectionApp().setVisible(true);
        });
    }
}
