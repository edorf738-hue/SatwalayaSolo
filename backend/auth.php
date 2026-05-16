<?php
require_once 'config.php';
setHeaders();

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    errorResponse('Method tidak diizinkan', 405);
    exit;
}

$action = $_GET['action'] ?? '';
$body = json_decode(file_get_contents('php://input'), true);

switch ($action) {

    case 'register':
        $username = trim($body['username'] ?? '');
        $password = trim($body['password'] ?? '');
        $email    = trim($body['email'] ?? '');
        $phone    = trim($body['phone'] ?? '');

        if (!$username || !$password || !$email) {
            errorResponse('username, password, dan email wajib diisi');
            break;
        }

        $conn = getConnection();
        $hashed = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $conn->prepare('INSERT INTO users (username, password, email, phone) VALUES (?,?,?,?)');
        $stmt->bind_param('ssss', $username, $hashed, $email, $phone);

        if ($stmt->execute()) {
            successResponse(['id' => $conn->insert_id], 'Registrasi berhasil');
        } else {
            if ($conn->errno === 1062) {
                errorResponse('Username sudah digunakan');
            } else {
                errorResponse('Gagal registrasi: ' . $stmt->error, 500);
            }
        }
        $conn->close();
        break;

    case 'login':
        $username = trim($body['username'] ?? '');
        $password = trim($body['password'] ?? '');

        if (!$username || !$password) {
            errorResponse('username dan password wajib diisi');
            break;
        }

        $conn = getConnection();
        $stmt = $conn->prepare('SELECT * FROM users WHERE username = ? OR email = ?');
        $stmt->bind_param('ss', $username, $username);
        $stmt->execute();
        $result = $stmt->get_result();
        $user = $result->fetch_assoc();

        if ($user && password_verify($password, $user['password'])) {
            unset($user['password']);
            successResponse($user, 'Login berhasil');
        } else {
            errorResponse('Username atau password salah', 401);
        }
        $conn->close();
        break;

    default:
        errorResponse('Action tidak valid. Gunakan ?action=login atau ?action=register');
}
