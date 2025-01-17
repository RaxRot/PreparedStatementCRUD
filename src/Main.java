import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASSWORD = "19649072Sever";
    public static void main(String[] args) {

        try {
            Connection connection= DriverManager.getConnection(URL,USER,PASSWORD);

            boolean isWorking=true;

            while (isWorking){
                System.out.println("Welcome to User Database");
                System.out.println("Press 1 to create a new user");
                System.out.println("Press 2 to read all users");
                System.out.println("Press 3 to update a user");
                System.out.println("Press 4 to delete a user");
                System.out.println("Press 5 to find user");
                System.out.println("Press 0 to exit");

                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                    System.out.println("Вы ввели: " + choice);

                    switch (choice) {
                        case 1:
                            createUser(connection,scanner);
                            break;
                            case 2:
                                readAllUsers(connection);
                                break;
                                case 3:
                                    updateUser(connection,scanner);
                                    break;
                                    case 4:
                                        deleteUser(connection,scanner);
                                        break;
                                        case 5:
                                            findUser(connection,scanner);
                                            break;
                        case 0:
                            System.out.println("Bye");
                            isWorking=false;
                            break;
                        default:
                            System.out.println("Invalid choice");
                            isWorking=false;
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: '" + input + "' не является целым числом. Попробуйте снова.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void createUser(Connection connection,Scanner scanner) throws SQLException {
        System.out.println("Enter username: ");
        String username = scanner.nextLine();
        System.out.println("Enter age");
        int age = scanner.nextInt();

        if (username.trim().isEmpty() || age<=0){
            System.out.println("Incorrect username or age");
        }else{
            createUserJDBC(connection,username,age);
        }
    }

    private static void createUserJDBC(Connection connection,String name,int age) throws SQLException {
        String sql = "INSERT INTO users (first_name,age) VALUES (?,?)";
        try(PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("User created successfully");
            }else{
                System.out.println("User creation failed");
            }
        }
    }

    private static void readAllUsers(Connection connection) throws SQLException {
        readAllUsersJDBC(connection);
    }

    private static void readAllUsersJDBC(Connection connection) throws SQLException {
        String sql = "SELECT * FROM users";
        try (PreparedStatement preparedStatement=connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while(resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("first_name");
                int age=resultSet.getInt("age");

                System.out.println("---------------");
                System.out.println(id+" "+name+" "+age);
                System.out.println("---------------");
            }
        }
    }

    private static void updateUser(Connection connection,Scanner scanner) throws SQLException {
        System.out.println("Enter id of the user you want to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter new name: ");
        String name = scanner.nextLine();
        System.out.println("Enter new age: ");
        int age = scanner.nextInt();

        if (name.trim().isEmpty() || age<=0 || id<=0){
            System.out.println("Incorrect username or age");
        }else{
            updateUserJDBC(connection,name,age,id);
        }
    }

    private static void updateUserJDBC(Connection connection,String name,int age,int id) throws SQLException {
        String sql = "UPDATE users SET first_name=?,age=? WHERE id=?";
        try (PreparedStatement preparedStatement=connection.prepareStatement(sql)) {
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setInt(3,id);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("User updated successfully");
            }else{
                System.out.println("User update failed");
            }
        }
    }

    private static void deleteUser(Connection connection,Scanner scanner) throws SQLException {
        System.out.println("Enter id of the user you want to delete: ");
        int id = scanner.nextInt();
        if (id<=0){
            System.out.println("Incorrect username or age");
        }else{
            deleteUserJDBC(connection,id);
        }
    }

    private static void deleteUserJDBC(Connection connection,int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try(PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("User deleted successfully");
            }else{
                System.out.println("User delete failed");
            }
        }
    }

    private static void findUser(Connection connection,Scanner scanner) throws SQLException {
        System.out.println("Enter id of the user you want to find: ");
        int id = scanner.nextInt();
        if (id<=0){
            System.out.println("Incorrect username or age");
        }else{
            findUserJDBC(connection,id);
        }
    }

    private static void findUserJDBC(Connection connection,int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=?";
        try(PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int ids=resultSet.getInt("id");
                String name=resultSet.getString("first_name");
                int age=resultSet.getInt("age");
                System.out.println(ids+" "+name+" "+age);
            }
        }
    }
}