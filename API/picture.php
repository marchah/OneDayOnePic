<?php

//header("Content-Type: text/plain");
header('Content-Type: image/jpeg');

require('DBConnection.php');

function getPathPictureByIdCategorie($idCategorie) {
    $db = connectionBDD();
    
    if ($idCategorie <= 0)
        $query = 'SELECT path FROM picture ORDER BY RAND() LIMIT 1 ';
    else
	$query = 'SELECT path FROM picture WHERE idcategorie=' . $db->quote($idCategorie) . ' ORDER BY RAND() LIMIT 1 ';
    $reponse = $db->query($query);
    $ret = false;
    if ($data = $reponse->fetch()) {
        $ret = $data['path'];
    }
    $reponse->closeCursor();
    return $ret;
}

if (isset($_GET["idCategorie"]))
   $pathPicture = getPathPictureByIdCategorie($_GET["idCategorie"]);
else
    $pathPicture = getPathPictureByIdCategorie(0);

readfile('.' . $pathPicture);
exit;