<?php
require_once 'config.php';
setHeaders();

$method = $_SERVER['REQUEST_METHOD'];
$id = isset($_GET['id']) ? (int)$_GET['id'] : null;

switch ($method) {

    case 'GET':
        $conn = getConnection();
        if ($id) {
            $stmt = $conn->prepare('SELECT * FROM bookings WHERE id = ?');
            $stmt->bind_param('i', $id);
            $stmt->execute();
            $result = $stmt->get_result();
            $booking = $result->fetch_assoc();
            if ($booking)
                successResponse($booking);
            else
                errorResponse('Booking tidak ditemukan', 404);
        } else {
            $result = $conn->query('SELECT * FROM bookings ORDER BY id DESC');
            $bookings = $result->fetch_all(MYSQLI_ASSOC);
            successResponse($bookings);
        }
        $conn->close();
        break;

    case 'POST':
        $body = json_decode(file_get_contents('php://input'), true);
        $service_name = trim($body['service_name'] ?? '');
        $pet_name     = trim($body['pet_name'] ?? '');
        $pet_type     = trim($body['pet_type'] ?? '');
        $room_type    = trim($body['room_type'] ?? '');
        $start_date   = trim($body['start_date'] ?? '');
        $end_date     = trim($body['end_date'] ?? '');
        $nights       = (int)($body['nights'] ?? 0);
        $total_price  = (int)($body['total_price'] ?? 0);
        $status       = trim($body['status'] ?? 'Active');
        $user_id      = isset($body['user_id']) ? (int)$body['user_id'] : null;

        if (!$service_name || !$pet_name || !$pet_type) {
            errorResponse('service_name, pet_name, dan pet_type wajib diisi');
            break;
        }

        $conn = getConnection();
        $stmt = $conn->prepare(
            'INSERT INTO bookings (user_id, service_name, pet_name, pet_type, room_type, start_date, end_date, nights, total_price, status)
             VALUES (?,?,?,?,?,?,?,?,?,?)'
        );
        $stmt->bind_param('issssssiis', $user_id, $service_name, $pet_name, $pet_type, $room_type, $start_date, $end_date, $nights, $total_price, $status);

        if ($stmt->execute()) {
            successResponse(['id' => $conn->insert_id], 'Booking berhasil ditambahkan');
        } else {
            errorResponse('Gagal menambahkan booking: ' . $stmt->error, 500);
        }
        $conn->close();
        break;

    case 'PUT':
        if (!$id) { errorResponse('ID booking diperlukan'); break; }

        $body = json_decode(file_get_contents('php://input'), true);
        $status = trim($body['status'] ?? '');

        if (!$status) { errorResponse('Status wajib diisi'); break; }

        $conn = getConnection();
        $stmt = $conn->prepare('UPDATE bookings SET status = ? WHERE id = ?');
        $stmt->bind_param('si', $status, $id);

        if ($stmt->execute()) {
            successResponse(null, 'Booking berhasil diperbarui');
        } else {
            errorResponse('Gagal memperbarui booking', 500);
        }
        $conn->close();
        break;

    case 'DELETE':
        if (!$id) { errorResponse('ID booking diperlukan'); break; }

        $conn = getConnection();
        $stmt = $conn->prepare('DELETE FROM bookings WHERE id = ?');
        $stmt->bind_param('i', $id);

        if ($stmt->execute()) {
            successResponse(null, 'Booking berhasil dihapus');
        } else {
            errorResponse('Gagal menghapus booking', 500);
        }
        $conn->close();
        break;

    default:
        errorResponse('Method tidak diizinkan', 405);
}