<?php
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'satwalaya');

function getConnection(): mysqli
{
    $conn = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    if ($conn->connect_error) {
        http_response_code(500);
        die(json_encode([
            'success' => false,
            'message' => 'Koneksi database gagal: ' . $conn->connect_error
        ]));
    }
    $conn->set_charset('utf8mb4');
    return $conn;
}

function setHeaders(): void
{
    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Content-Type, Authorization');
    if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
        exit(0);
    }
}

function successResponse($data = null, string $message = 'OK'): void
{
    echo json_encode([
        'success' => true,
        'message' => $message,
        'data'    => $data
    ]);
}

function errorResponse(string $message, int $code = 400): void
{
    http_response_code($code);
    echo json_encode([
        'success' => false,
        'message' => $message,
        'data'    => null
    ]);
}