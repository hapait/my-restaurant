<?php

    // put server key into common.php
	include('common.php');

	if ($_SERVER["REQUEST_METHOD"] == "POST") {

		$message = $_POST["message"];
		$title = $_POST["title"];
		$id = $_POST["id"];

		sendPushNotification($title, $message, $id, $apiKey);
		
	}


	function sendPushNotification($title, $message, $id, $apiKey) {
		
		$post = array(
						'to' => '/topics/myrestaurant',
						'data' => array (
								'title' => $title,
								'message' => $message,
								'id' => $id
						)
					 );

		$headers = array( 
							'Authorization: key=' . $apiKey,
							'Content-Type: application/json'
						);
    
		$ch = curl_init();  
		curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');   
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);    
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($post));
		curl_setopt($ch, CURLOPT_FOLLOWLOCATION, TRUE);
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);  
		$result = curl_exec($ch);
		if (curl_errno($ch)) {
			echo 'FCM error: ' . curl_error($ch);
		} else {
			echo "<br><div class='textOutput'>Push sent to all devices.</div>";
			echo "<br><a href='index.php'>Send New Push Notification</a>";
		}
		curl_close($ch);
	}

?>