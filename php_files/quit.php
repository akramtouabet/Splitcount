<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];


if(isset($_POST)){
	if (!empty($_POST['sid']) && !empty($_POST['uid'])){
		$sid = $_POST['sid'];
		$uid = $_POST['uid'];
		$query=$db->prepare('DELETE FROM participants WHERE uid = :uid AND sid = :sid');
		$query -> execute(array(
					'sid' => $sid,
					'uid' => $uid,
				));
		if ($query->rowCount()==0){
			$results["error"] = true;
			$results["message"] = "ERROR FOUND.";
		}else{
			$results["error"] = false;
			$results["message"] = "Left splitcount successfully.";	
		}
	$query->closeCursor();

	}
		echo json_encode($results);
}
?>