import java.io.*;
import java.net.*;
import java.sql.*;

public class server {
    private static final int PORT = 0501;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456789";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is listening on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");

            new ClientHandler(clientSocket).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String requestType = in.readLine();

                if (requestType.equals("register")) {
                    String name = in.readLine();
                    String email = in.readLine();
                    String password = in.readLine();

                    if (registerUser(name, email, password)) {
                        out.println("register_success");
                    } else {
                        out.println("register_failed");
                    }
                } else if (requestType.equals("login")) {
                    String email = in.readLine();
                    String password = in.readLine();

                    if (loginUser(email, password)) {
                        out.println("login_success");
                    } else {
                        out.println("login_failed");
                    }
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean registerUser(String name, String email, String password) throws SQLException {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            int rowsAffected = stmt.executeUpdate();

            stmt.close();
            conn.close();

            return rowsAffected > 0;
        }

        private boolean loginUser(String email, String password) throws SQLException {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            boolean isAuthenticated = rs.next();

            rs.close();
            stmt.close();
            conn.close();

            return isAuthenticated;
        }
    }
}
