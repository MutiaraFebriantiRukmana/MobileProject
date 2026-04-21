<?php
include 'dbconnect.php';
$name = $_POST ['name'];
$price = $_POST ['price'];
$promo = $_POST ['promo'];
$description = $_POST ['description'];
$image = $_POST['image'];
if(!$name || !$price || !$promo || !$description || !$image ) {
    echo 'Semua data harus diisi, ada data kosong';
} else {
    $query = "INSERT into product (name, price, promo,description, image)
    VALUES ('$name','$price','$promo','$description','$image')";
    $execute = mysqli_query($conn, $query);
    if($execute){
        echo json_encode (array('message'=>'Product baru berhasil ditambahkan'));
    }else{
        echo json_encode (array('message'=>'Product baru gagal ditambahkan'));
    }
}
?>