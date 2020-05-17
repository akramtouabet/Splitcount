<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];

$i = 0;
$a = "";

if(isset($_POST)){
	if (!empty($_POST['sid'])){
		$sid = $_POST['sid'];

		$query=$db->prepare('SELECT * FROM participants WHERE sid = :sid');
		$query -> execute(array(
					'sid' => $sid
				));
		if ($query->rowCount()==0){
			$results["error"] = true;
			$results["message"] = "No user is part of this splitcount.";
			$a .= json_encode($results);
		}else{
			foreach($query as $row){
				// WE have $row["uid"] - $row["sid"] 
				$query2 = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query2 -> execute(array(
					'uid' => $row["uid"]
				));
				if ($query2->rowCount()==0){
					$results["error"] = true;
					$results["message"] = "An error has occured, please try again later.";
				}else{
					foreach($query2 as $row2){
						// WE have $row2["uid"] - $row2["username"] - $row2["email"]
						$i++;
						$results["error"] = false;
						$results["uid"] = $row["uid"];
						$results["username"] = $row2["username"];
						if ($i < $query->rowCount()) {
							$a .= json_encode($results).", ";
						}
						else {
							$a .= json_encode($results)." ";
						}
					}
				}
			}
		}
	$query->closeCursor();
	echo'{
		"spUsersList": [
			'.$a.'
		]
	}';
	}
}
?>