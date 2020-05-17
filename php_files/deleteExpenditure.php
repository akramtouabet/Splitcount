<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];


if(isset($_POST)){
	if (!empty($_POST['eid'])){

		$eid = $_POST['eid'];

		$query = $db->prepare("DELETE FROM expenditure WHERE eid = :eid");
		$query -> execute(array(
					'eid' => $eid
				));

		if (!$query){
			$results["error"] = true;
			$results["message"] = "Deletion failed.";
		}else{
			$results["message"] = "Expenditure deleted successfully.";
		}
        $query->closecursor();
    }
	echo json_encode($results);
}
?>