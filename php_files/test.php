<?php
require("db_config.php");
error_reporting(E_ERROR | E_WARNING | E_PARSE);
$db = new PDO($dsn, $username, $password);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
$results["from"];
$results["to"];
$results["amount"];
$results["error"] = "false";


$spent = [];
$nbUsers = 0;
$total=0;
$resp = "";
$maxPos = 0;
$minNeg = 0;
$maxKey;
$minKey;
$_POST["sid"]=4;


if(isset($_POST)){
	if (isset($_POST["sid"])){

		//CALCUL TOTAL

		$sid = $_POST["sid"];
		$query2 = $db->prepare("SELECT * FROM participants WHERE sid =:sid");
		$query2 -> execute(array(
			'sid' => $sid
		));
		foreach($query2 as $row){
			$nbUsers++;
		}
		$query2->closecursor();

		$query = $db->prepare("SELECT * FROM expenditure WHERE sid = :sid");
		$query -> execute(array(
			'sid' => $sid
		));
		// CALCUL TOTAL USERS
		foreach($query as $row){
			// WE HAVE : $row["eid"] - $row["title"] - $row["sid"] - $row["uid"] - $row["amount"] - $row["date"] - $row["exp_code"]
			$total += $row["amount"];
			$spent[$row["uid"]] += $row["amount"];
		}
		$query->closecursor();
		$total = $total / $nbUsers;
		
		foreach ($spent as &$val){
			$val -= $total;
		}


		// Positive / Negative 

		$usrPos = [];
		$usrNeg = [];

		foreach ($spent as $key=>&$val){
			if ($val > 0){
				$usrPos[$key] = $val;
			}else if ($val < 0){
				$usrNeg[$key] = $val;
			}
		}



		function minNeg($array){
			$min =0;
			foreach($array as $key => &$val){
				if ($val < $min){
					$min = $val;
				}
			}
			return $min;
		}

		function maxPos($array){
			$max =0;
			foreach($array as $key => &$val){
				if ($val > $max){
					$max = $val;
				}
			}
			return $max;
		}

		function minNegKey($array){
			$min =0;
			$newKey;
			foreach($array as $key => &$val){
				if ($val < $min){
					$min = $val;
					$newKey = $key;
				}
			}
			return $newKey;
		}

		function maxPosKey($array){
			$max =0;
			foreach($array as $key => &$val){
				if ($val > $min){
					$max = $val;
					$newKey = $key;
				}
			}
			return $newKey;
		}

		function sumPos($array){
			$sum =0;
			foreach($array as $key => &$val){
				$sum += $val;
			}
			return $sum;
		}

		function nbDiff($array){
			$i = 0;
			foreach ($array as $key => &$val){
				if ($val <> 0){
					$i++;
				}
			}
			return $i;
		}
		


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		foreach ($usrPos as $key => &$valPos){
			foreach ($usrNeg as $key =>  &$valNeg){
				if ($valPos == -$valNeg && $valNeg <> 0){
					$usrNeg[$key] = 0;
					$usrPos[$key] = 0;
				}
			}
		}

		while (sumPos($usrPos) <> 0){
			$maxKey = maxPosKey($usrPos);
			$minKey = minNegKey($usrNeg);
			if ($usrPos[$maxKey] >= -$usrNeg[$minKey]){
				$query = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query -> execute(array(
					"uid" => $minKey
				));
				foreach ($query as $row){
					$results["from"] = $row["username"];
				}
				$query->closecursor();

				$query = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query -> execute(array(
					"uid" => $maxKey
				));
				foreach ($query as $row){
					$results["to"] = $row["username"];
				}
				$query->closecursor();

				$results["amount"] = -$usrNeg[$minKey];
				$results["error"] = "false";
				if (nbDiff($usrPos) == 1 && nbDiff($usrNeg) == 1 && $usrNeg[$minKey] == -$usrPos[$maxKey]){
					$a .= json_encode($results)." ";;
				}else{
					$a .= json_encode($results).", ";;
				}

				$usrPos[$maxKey] += $usrNeg[$minKey];
				$usrNeg[$minKey] = 0;
			}else if ($usrPos[$maxKey] < -$usrNeg[$minKey]){
				$query = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query -> execute(array(
					"uid" => $minKey
				));
				foreach ($query as $row){
					$results["from"] = $row["username"];
				}
				$query->closecursor();
				$query = $db->prepare("SELECT * FROM users WHERE uid = :uid");
				$query -> execute(array(
					"uid" => $maxKey
				));
				foreach ($query as $row){
					$results["to"] = $row["username"];
				}
				$query->closecursor();
				$results["amount"] = $usrPos[$maxKey];
				$results["error"] = "false";
				if (nbDiff($usrPos) == 1 && nbDiff($usrNeg) == 1 && $usrNeg[$minKey] == -$usrPos[$maxKey]){
					$a .= json_encode($results)." ";;
				}else{
					$a .= json_encode($results).", ";;
				}
				$usrNeg[$minKey] += $usrPos[$maxKey];
				$usrPos[$maxKey] = 0;
			}
		}




////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	}
	 echo'{
		"balance": [
			'.$a.'
		]
	}'; 
}
?>