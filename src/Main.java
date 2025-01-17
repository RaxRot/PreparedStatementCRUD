import java.sql.*;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "19649072Sever";
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Create
            createUser(conn, "John", 25);
            createUser(conn, "Alice", 30);
            createUser(conn, "Bob", 22);

            // Read
            readUsers(conn);

            // Update
            updateUser(conn, 1, "John Doe", 26);

            // Delete
            deleteUser(conn, 2);

            // Read after changes
            readUsers(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createUser(Connection conn, String firstName, int age)throws SQLException {
        String sql = "INSERT INTO users (first_name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("User " + firstName + " added.");
        }
    }

    // Read
    public static void readUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Users:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                int age = rs.getInt("age");
                System.out.printf("ID: %d, Name: %s, Age: %d%n", id, firstName, age);
            }
        }
    }

    // Update
    public static void updateUser(Connection conn, int id, String newFirstName, int newAge) throws SQLException {
        String sql = "UPDATE users SET first_name = ?, age = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newFirstName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("User updated. Rows affected: " + rowsAffected);
        }
    }

    // Delete
    public static void deleteUser(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("User deleted. Rows affected: " + rowsAffected);
        }
    }
}