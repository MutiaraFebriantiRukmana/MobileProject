<?php
include 'dbconnect.php';

if (isset($_POST['id'])) {
    $productId = $_POST['id'];

    $query = "DELETE FROM product WHERE id = '$productId'";

    $execute = mysqli_query($conn, $query);

    if ($execute) {
        if (mysqli_affected_rows($conn) > 0) {
            echo json_encode(array('message' => 'Produk berhasil dihapus'));
        } else {
            echo json_encode(array('message' => 'Produk tidak ditemukan'));
        }
    } else {
        echo json_encode(array('message' => 'Produk gagal dihapus'));
    }
} else {
    echo json_encode(array('message' => 'ID Produk tidak diberikan'));
}