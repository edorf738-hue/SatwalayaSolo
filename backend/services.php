<?php
require_once 'config.php';
setHeaders();

$method = $_SERVER['REQUEST_METHOD'];

if ($method !== 'GET') {
    errorResponse('Method tidak diizinkan', 405);
    exit;
}

$conn = getConnection();
$category = isset($_GET['category']) ? trim($_GET['category']) : null;

if ($category) {
    $stmt = $conn->prepare('SELECT * FROM services WHERE category = ? ORDER BY price ASC');
    $stmt->bind_param('s', $category);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query('SELECT * FROM services ORDER BY category, price ASC');
}

$services = $result->fetch_all(MYSQLI_ASSOC);
successResponse($services);
$conn->close();