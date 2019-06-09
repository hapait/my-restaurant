<html>

    <head>
        <title>Send push notification</title>
        <link rel="stylesheet" href="css/style.css">
    </head>

    <body align="center" >
    	<form id="pushform" method="POST" action="push.php">
            <br>
            <br>
            <div class="textHeader">Push notification console</div>
            <br>
			<input placeholder="Title" type="text" size="38" name="title">
			<br>
			<br>
            <textarea placeholder="Message" name="message" cols="40" rows="5"></textarea>
            <br>
			<br>
			<input placeholder="Put your item/post ID here" type="text" size="38" name="id">
			<br>
            <br>
    		<input class="button button2" type="submit" name="submit" value="Send">

    	</form>
    </body>

</html>