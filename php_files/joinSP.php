<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];


if(isset($_POST)){
	if (!empty($_POST['sp_code']) && !empty($_POST['uid'])){
		$sp_code = $_POST['sp_code'];
		$uid = $_POST['uid'];
		$query=$db->prepare('SELECT * FROM splitcount WHERE sp_code = :sp_code');
		$query -> execute(array(
					'sp_code' => $sp_code,
				));
		if ($query->rowCount()==0){
			$results["error"] = true;
			$results["message"] = "No splitcount is associated with this code.";
		}else{
			foreach($query as $row){
				$query2=$db->prepare('SELECT * FROM participants WHERE sid = :sid AND uid = :uid');
				$query2->execute(array(
							'sid' => $row['sid'],
							'uid' => $uid
						));
				if ($query2->rowCount() > 0){
					$results["error"] = true;
					$results["message"] = "Error while Joining splitcount : Already joined.";
				}else{
					$query3 = $db->prepare("INSERT INTO participants VALUES (:sid, :uid)");
					$query3 -> execute(array(
										'sid' => $row["sid"],
										'uid' => $uid
									));
					$results["error"] = false;
					$results["message"] = "Joined splitcount successfully !";
					$query3->closecursor();
				}
			}
		}
	$query->closeCursor();

	}
		echo json_encode($results);
}
?>