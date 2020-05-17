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
	if (!empty($_POST['uid'])){
		$uid = $_POST['uid'];

		$query=$db->prepare('SELECT splitcount.name, splitcount.sid, splitcount.description, splitcount.sp_code FROM splitcount, participants WHERE participants.uid = :uid AND participants.sid = splitcount.sid ORDER BY participants.sid DESC');
		$query -> execute(array(
					'uid' => $uid
				));
		if ($query->rowCount()==0){
			$results["error"] = true;
			$results["message"] = "No splitcount found.";
			$a .= json_encode($results);
		}else{
			foreach($query as $row){
				$i++;
				$results["name"] = $row["name"];
				$results["description"] = $row["description"];
				$results["sid"] = $row["sid"];
				$results["sp_code"] = $row["sp_code"];
				if ($i < $query->rowCount()) {
					$a .= json_encode($results).", ";
				}
				else {
					$a .= json_encode($results)." ";
				}
			}
		}
	$query->closeCursor();
	echo'{
		"splitcounts": [
			'.$a.'
		]
	}';
	}
}
?>