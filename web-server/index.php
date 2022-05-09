<?php

$url = "http://ai-server/api/helloworld";
$ch = curl_init($url);

$response = curl_exec($ch);

if ($err = curl_error($ch)) {
    echo $err;
} else {
    echo $response;
}
