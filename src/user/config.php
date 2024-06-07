<?php
$servername = "localhost";
$username = "root";
$password = "123456789"; // Mật khẩu MySQL của bạn
$dbname = "userdb";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>

