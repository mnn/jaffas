<?php
$notfound = true;
$url = "";

$users = array(
	"monnef" => 0,
	"Tiartyos" => 0
);

$cloaks = array( 0 => "jaffaCloak.png" );

if(!isset($_GET["name"])){
}else{
	$name = $_GET["name"];
	if (in_array($name, $users)){
		$url = $cloaks[$users[$name]];
		$notfound = false;
	}
}

if($notfound || $url == ""){
	header("HTTP/1.0 404 Not Found");
	echo "cloak not found";
}else{
	header("Location: ".$url);
}
?>