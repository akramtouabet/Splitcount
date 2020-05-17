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

		$query=$db->prepare("SELECT *, DATE_FORMAT(datetime, '%d/%m/%Y') AS datetime FROM expenditure WHERE expenditure.sid = :sid ORDER BY eid DESC");
		$query -> execute(array(
					'sid' => $sid
				));
		if ($query->rowCount()==0){
			$results["error"] = true;
			$results["message"] = "No operations have been made on this splitcount yet !";
			$a .= json_encode($results);
		}else{
			foreach($query as $row){
				// WE have $row["eid"] - $row["name"] - $row["sid"] - $row["uid"] - $row["amount"] - $row["date"]
				$query2 = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query2 -> execute(array(
					'uid' => $row["uid"]
				));
				if ($query2->rowCount()==0){
					$results["error"] = true;
					$results["message"] = "An error has occured, please try again later.";
				}else{
					foreach($query2 as $row2){
						// WE have $row2["eid"] - $row2["uid"]
						$i++;

						$results["error"] = false;
						$results["title"] = $row["title"];
						$results["amount"] = $row["amount"];
						$results["from"] = $row2["username"];
						$results["datetime"] = $row["datetime"];
						$results["eid"] = $row["eid"];
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
		"operations": [
			'.$a.'
		]
	}';
	}
}
?>