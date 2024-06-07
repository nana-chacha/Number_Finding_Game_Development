<?php
include 'config.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $sql = "SELECT * FROM users WHERE email='$email'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        if (password_verify($password, $row['password'])) {
            echo "Đăng nhập thành công!";
            // Bạn có thể thiết lập session ở đây
        } else {
            echo "Sai mật khẩu.";
        }
    } else {
        echo "Không tìm thấy người dùng.";
    }

    $conn->close();
}
?>
