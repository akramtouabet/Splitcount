<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];


if(isset($_POST)){
	if (!empty($_POST['username']) && !empty($_POST['password'])){
		$username = $_POST['username'];
		$password = $_POST['password'];

		$query = $db->prepare("SELECT * FROM users where username = :username");
		$query -> execute(array(
				'username' => $username,
			));
		$row = $query->fetch(PDO::FETCH_OBJ);
		if ($row){
				if (password_verify($password, $row->password)){
					$results["error"] = false;
					$results["uid"] = $row->uid; 
					$results["username"] = $row->username;
					$results["email"] = $row->email;
				}else{
					$results["error"] = true;
					$results["message"] = "Username or password is incorrect.";
			}
		}else{
			$results["error"] = true;
			$results["message"] = "Username or password is incorrect.";
		}
		$query->closecursor();
	}
	echo json_encode($results);
}
?>