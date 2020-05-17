<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];

/*
$_POST['uid'] = 10;
$_POST['username'] = "Akram";
$_POST['email'] = "Akram@live.fr";
$_POST['passwordConfirm'] = "Akram1234-";
$_POST['password'] = "Akram1233-";
$_POST['password2'] = "Akram1233-";
$_POST['old_email'] = "Akram@live.fr";
$_POST['old_username'] = "Akram";
*/

// Updating username & mail, old password kept

if(isset($_POST)){
	if (!empty($_POST['uid']) && !empty($_POST['username']) && !empty($_POST['email']) && !empty($_POST['passwordConfirm']) && !empty($_POST['old_username']) && !empty($_POST['old_email']) && !isset($_POST["password"]) && !isset($_POST["password2"])){
		$uid = $_POST['uid'];
		$username = $_POST['username'];
		$email = $_POST['email'];
		$passwordConfirm = $_POST['passwordConfirm'];
		$old_email = $_POST['old_email'];
		$old_username = $_POST['old_username'];

		// Username checking

		if (strlen($username) < 3 || !preg_match("/^[a-zA-Z0-9 _-]+$/" , $username) || strlen($username) > 60){
			$results["error"] = true;
			$results["message"]["username"] = "Invalid username.";
		}else if (strcmp($username, $old_username) != 0){
			//Existing username checking
			$query = $db->prepare("SELECT * FROM users where username = :username");
			$query -> execute(array(
					'username' => $username,
				));
			$row = $query->fetch();
			if ($row){ 
				$results["error"] = true;
				$results["message"]["username"] = "Username already used !";
			}
			$query->closecursor();
		}

		//Email checking
		if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
			$results["error"] = true;
			$results["message"]["email"] = "Invalid email address.";
		}else if (strcmp($email, $old_email) != 0){
			//Existing email check
			$query = $db->prepare("SELECT * FROM users where email = :email");
			$query -> execute(array(
					'email' => $email,
				));
			$row = $query->fetch();
			if ($row){
				$results["error"] = true;
				$results["message"]["email"] = "Email already used !";
			}
			$query->closecursor();
		}

		//Password checking
		
			$query = $db->prepare("SELECT password FROM users where uid = :uid");
			$query -> execute(array(
					'uid' => $uid,
				));
			$row = $query->fetch();
			if ($row){
				if (!password_verify($passwordConfirm, $row["password"])){
					$results["error"] = true;
					$results["message"]["password"] = "Wrong password !";
				}
			}
			$query->closecursor();

		

		if ($results["error"] === false){
			$query = $db->prepare("UPDATE users SET username = :username, email = :email where uid = :uid");
			$query -> execute(array(
					'username' => $username,
					'email' => $email,
					'uid' => $uid
				));
			if (!$query){
				$results["error"] = true;
				$results["message"] = "Error while updating user.";
			}else{
				$results["message"] = "Your profile has been updated successfully !";
			}
			$query->closecursor();
		}
				// Updating username & mail, old password changed
		
	}else if (!empty($_POST['uid']) && !empty($_POST['username']) && !empty($_POST['email']) && !empty($_POST['passwordConfirm']) && !empty($_POST['old_username']) && !empty($_POST['old_email']) && isset($_POST["password"]) && isset($_POST["password2"])){

		$uid = $_POST['uid'];
		$username = $_POST['username'];
		$email = $_POST['email'];
		$passwordConfirm = $_POST['passwordConfirm'];
		$old_email = $_POST['old_email'];
		$old_username = $_POST['old_username'];
		$password = $_POST['password'];
		$password2 = $_POST['password2'];

		if (strlen($username) < 3 || !preg_match("/^[a-zA-Z0-9 _-]+$/" , $username) || strlen($username) > 60){
			$results["error"] = true;
			$results["message"]["username"] = "Invalid username.";
		}else if (strcmp($username, $old_username) != 0){
			//Existing username checking
			$query = $db->prepare("SELECT * FROM users where username = :username");
			$query -> execute(array(
					'username' => $username,
				));
			$row = $query->fetch();
			if ($row){ 
				$results["error"] = true;
				$results["message"]["username"] = "Username already used !";
			}
			$query->closecursor();
		}

		//Email checking
		if(!filter_var($email, FILTER_VALIDATE_EMAIL)){
			$results["error"] = true;
			$results["message"]["email"] = "Invalid email address.";
		}else if (strcmp($email, $old_email) != 0){
			//Existing email check
			$query = $db->prepare("SELECT * FROM users where email = :email");
			$query -> execute(array(
					'email' => $email,
				));
			$row = $query->fetch();
			if ($row){
				$results["error"] = true;
				$results["message"]["email"] = "Email already used !";
			}
			$query->closecursor();
		}

		//Password checking

		$query = $db->prepare("SELECT password FROM users where uid = :uid");
		$query -> execute(array(
				'uid' => $uid,
			));
		$row = $query->fetch();
		if ($row){
			if (!password_verify($passwordConfirm, $row["password"])){
				$results["error"] = true;
				$results["message"]["passwordConfirm"] = "Wrong password !";
			}
		}
		$query->closecursor();

		$uppercase = preg_match('@[A-Z]@', $password);
		$lowercase = preg_match('@[a-z]@', $password);
		$number    = preg_match('@[0-9]@', $password);
		$specialChars = preg_match('@[^\w]@', $password);
			//Password strength checking
		if(!$uppercase || !$lowercase || !$number || !$specialChars || strlen($password) < 8) {
			$results["error"] = true;
			$results["message"]["password"] = "Password should be at least 8 characters in length and should include at least one upper case letter, one number, and one special character.";
		}
		if ($password !== $password2){
			$results["error"] = true;
			$results["message"]["password"] = "Your password and confirmation password do not match.";
		}

		//User update into DB.
		$password = password_hash($password, PASSWORD_BCRYPT);
		if ($results["error"] === false){
			$query = $db->prepare("UPDATE users SET username = :username, email = :email , password = :password where uid = :uid");
			$query -> execute(array(
					'username' => $username,
					'email' => $email,
					'password' => $password,
					'uid' => $uid
				));
			if (!$query){
				$results["error"] = true;
				$results["message"] = "Error while updating user.";
			}else{
				$results["message"] = "Your profile has been updated successfully !";
			}
			$query->closecursor();
		}
	}
	echo json_encode($results);
}

?>