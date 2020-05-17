<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];



if(isset($_POST)){
	if (!empty($_POST['title']) && !empty($_POST['uid']) && !empty($_POST['sid']) && !empty($_POST['amount'])){
		$title = $_POST['title'];
		$uid = $_POST['uid'];
		$sid = $_POST['sid'];
		$amount = $_POST['amount'];
		function generateCode($length = 16){
		    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()';
		    $charactersLength = strlen($characters);
		    $uniqueCode = '';
		    for ($i = 0; $i < $length; $i++) {
		        $uniqueCode .= $characters[rand(0, $charactersLength - 1)];
		    }
		    return $uniqueCode;
		}
		$code_exists=true;
		$exp_code = generateCode();
		while ($code_exists == true){
			$query = $db->prepare("SELECT * FROM expenditure where exp_code = :exp_code");
			$query -> execute(array(
					'exp_code' => $exp_code
				));
			$row = $query->fetch();
			if (!$row){
				$code_exists = false;
			}else{
				$exp_code = generateCode();
			}
			$query->closecursor();
		}

		$query = $db->prepare("INSERT INTO expenditure(title, sid, uid, amount, exp_code) VALUES (:title, :sid, :uid, :amount, :exp_code)");
		$query -> execute(array(
				'title' => $title,
				'sid' => $sid,
				'uid' => $uid,
				'amount' => $amount,
				'exp_code' => $exp_code
			));
		if (!$query){
				$results["error"] = true;
				$results["message"] = "Error while adding the expenditure.";
		}else{
			$query2 = $db->prepare("SELECT * FROM expenditure WHERE exp_code = :exp_code");
			$query2->execute(array(
					'exp_code' => $exp_code
				));
			foreach($query2 as $row2){
				// HERE WE'VE GOT $row2["EID"]
				$query3 = $db->prepare("SELECT uid FROM participants WHERE sid = :sid");
				$query3 -> execute(array(
									'sid' => $sid
								));
				foreach ($query3 as $row3) {
					$query4 = $db->prepare("INSERT INTO recipient(eid, uid, amount) VALUES (:eid, :uid, :amount);");
					$query4 -> execute(array(
						'eid' => $row2["eid"],
						'uid' => $row3["uid"],
						'amount' =>number_format((float)(($amount) / ($query3->rowCount())), 2, '.', '')
					)); 
				}
				$results["message"] = "Splitcount added successfully !";
				$query3->closecursor();
			}
			$query2->closecursor();
		}
		$query->closecursor();
	}
	echo json_encode($results);
}
?>