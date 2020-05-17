<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];

if(isset($_POST)){
	if (!empty($_POST['uid']) && !empty($_POST['name']) && isset($_POST['description'])){
		$uid = $_POST['uid'];
		$name = $_POST['name'];
		$description = $_POST['description'];
		if (strlen($name) < 3 || !preg_match("/^[a-zA-Z0-9 _-]+$/" , $name) || strlen($name) > 60){
			$results["error"] = true;
			$results["message"]["name"] = "Invalid name.";
		}else{
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
			$sp_code = generateCode();
			while ($code_exists == true){
				$query = $db->prepare("SELECT * FROM splitcount where sp_code = :sp_code");
				$query -> execute(array(
						'sp_code' => $sp_code,
					));
				$row = $query->fetch();
				if (!$row){
					$code_exists = false;
				}else{
					$sp_code = generateCode();
				}
				$query->closecursor();
			}

			$query = $db->prepare("INSERT INTO splitcount(admin_id, name, description, sp_code) VALUES (:uid, :name, :description, :sp_code)");
			$query -> execute(array(
					'uid' => $uid,
					'name' => $name,
					'description' => $description,
					'sp_code' => $sp_code
				));
			if (!$query){
				$results["error"] = true;
				$results["message"]["query"] = "Error while adding splitcount.";
			}else{
				$query2 = $db->prepare("SELECT * FROM splitcount WHERE sp_code = :sp_code");
				$query2->execute(array(
						'sp_code' => $sp_code
					));
				foreach($query2 as $row){
					$query3 = $db->prepare("INSERT INTO participants VALUES (:sid, :uid)");
					$query3 -> execute(array(
										'sid' => $row["sid"],
										'uid' => $uid
									));
					$results["message"] = "Splitcount added successfully !";
					$query3->closecursor();
				}
				$query2->closecursor();
			}
			$query->closecursor();
		}

	}
	echo json_encode($results);
}
?>