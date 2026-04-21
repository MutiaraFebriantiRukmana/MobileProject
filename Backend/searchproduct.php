<?php
include_once 'dbconnect.php';
$search = $_GET['search'];
$sql = "SELECT * FROM product WHERE name LIKE '%$search%'";
$exec = $conn -> query($sql);
//$exec = mysqli_query($conn, $sql) ;
$itemproduct = array();
if($exec ->num_rows > 0 ){
    while($rows = $exec ->fetch_assoc()){
        $itemproduct[] = $rows;
    }
}
echo json_encode($itemproduct);
$conn -> close();
?>