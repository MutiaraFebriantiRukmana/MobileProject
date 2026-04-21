<?php
include_once 'dbconnect.php';
$stat = $conn ->prepare("SELECT id, name, price, promo, description, image FROM product ORDER BY price DESC;");
$stat -> execute();
$stat -> bind_result($id, $name, $price, $promo, $description, $image);
$arrayproduct = array();
while($stat -> fetch()){
	$data = array();
	$data['id'] = $id;
	$data['name'] = $name;
	$data['price'] = $price;
	$data['promo'] = $promo;
	$data['description'] = $description;
	$data['image'] = $image;
	array_push($arrayproduct, $data);
}
echo json_encode($arrayproduct);
?>