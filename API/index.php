<?php

//header('Access-Control-Allow-Origin: *');
//header('Access-Control-Allow-Methods: GET');

header('Content-Type: application/json');

require('DBConnection.php');

define('DEFAULT_TIME_SYNCHRO', 4242);



function getListCategories() {
    $db = connectionBDD();
    
    $reponse = $db->query('SELECT * FROM categorie ORDER BY id');
    $listCategorie = array();
    while ($data = $reponse->fetch()) {
        $listCategorie[] = $data;
    }
    $reponse->closeCursor();
    return $listCategorie;
}

//echo json_encode(getListCategories());

function getTimeSynchro() {
    $db = connectionBDD();
    
    $req = $db->prepare('INSERT INTO time_synchro (nbuser) VALUES (:nbuser)');
    if (!$req->execute(array(
                'nbuser' => 1
            ))) {
        return DEFAULT_TIME_SYNCHRO;
    }
    return $db->lastInsertId();
}

//echo json_encode(array('time' => getTimeSynchro()));

if (isset($_GET["nameRequest"])) {
    if ($_GET["nameRequest"] == "getTimeSynchro")
        echo json_encode(array('time' => getTimeSynchro()));
    else if ($_GET["nameRequest"] == "getListCategories")
        echo json_encode(getListCategories());
    else
        echo json_encode(array('error' => 'Unknow nameRequest.'));
}
else
   echo json_encode(array('error' => 'nameRequest undefined.'));