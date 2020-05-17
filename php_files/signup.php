<?php
require("db_config.php");

$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["error"] = false;
$results["message"] = [];

if(isset($_POST)){
	if (!empty($_POST['username']) && !empty($_POST['email']) && !empty($_POST['password']) && !empty($_POST['password2'])){
		$username = $_POST['username'];
		$email = $_POST['email'];
		$password = $_POST['password'];
		$password2 = $_POST['password2'];

		// Username checking

		if (strlen($username) < 3 || !preg_match("/^[a-zA-Z0-9 _-]+$/" , $username) || strlen($username) > 60){
			$results["error"] = true;
			$results["message"]["username"] = "Invalid username.";
		}else{
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
		}else{
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

		//New user insertion into DB.
		function generateCode($length = 16){
		    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()';
		    $charactersLength = strlen($characters);
		    $invite_code = '';
		    for ($i = 0; $i < $length; $i++) {
		        $invite_code .= $characters[rand(0, $charactersLength - 1)];
		    }
		    return $invite_code;
		}

		if ($results["error"] === false){

			$password = password_hash($password, PASSWORD_BCRYPT);
			$code_exists=true;
			$invite_code = generateCode();
			while ($code_exists == true){
				$query = $db->prepare("SELECT * FROM users where invite_code = :invite_code");
				$query -> execute(array(
						'invite_code' => $invite_code,
					));
				$row = $query->fetch();
				if (!$row){
					$code_exists = false;
				}else{
					$invite_code = generateCode();
				}
				$query->closecursor();
			}

			$query = $db->prepare("INSERT INTO users(username, email, password, invite_code) VALUES (:username, :email, :password, :invite_code)");
			$query -> execute(array(
					'username' => $username,
					'email' => $email,
					'password' => $password,
					'invite_code' => $invite_code
				));
			if (!$query){
				$results["error"] = true;
				$results["message"]["password"] = "Error while adding user.";
			}
			$query->closecursor();
		}
	}
	echo json_encode($results);
}
?>