<?php

$url = "http://ai-server/api/helloworld";
$ch = curl_init($url);

curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

$response = curl_exec($ch);

if ($err = curl_error($ch)) {
    echo $err;
} else {
    echo $response;
}
