<html>
	<body>
		<form action="index.php" method="post" enctype="multipart/form-data">
                      <label for="urlFile">URL Picture:</label>
		      <input type="text" name="urlFile" id="urlFile"/><br/>
                      <label for="idCategorie">Categorie:</label>
		      <select name="idCategorie">
		       <?php
		       require_once('../DBConnection.php');
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

				$listCategories = getListCategories();
				var_dump($listCategories);
				foreach ($listCategories as $categorie) {
					echo "<option value='".$categorie['id']."'>".$categorie['name']."</option>";
				}

		       ?>
		      </select>
		      <br />
                      <input type="submit" name="submit" value="Submit"/>
         	 </form>
	 </body>
</html>

<?php

function savePicture($name, $idC) {
   $db = connectionBDD();
   $req = $db->prepare("INSERT INTO picture (idCategorie, path) VALUES (:idCategorie, :path)");
   if ($req->execute([
	'idCategorie' => $idC,
	'path' => "/picture/" . $name
   ]) == false)
	echo "Error: save picture in db failed.";
  else
	echo "Picture saved !!!";
}

if (isset($_POST["urlFile"]) && isset($_POST["idCategorie"])) {
   $url = $_POST["urlFile"];
   $idCategorie = $_POST["idCategorie"];
   $info = pathinfo($url);

   if ($info['extension'] != null) {
      $name = time() . "." . $info['extension'];
      if (file_put_contents("../picture/" . $name, fopen($url, 'r')) == false)
      	 echo "Erreur: copie file failed.";
      else
         savePicture($name, $idCategorie);
   }
   else
	echo "Error: file hasn't extension";
}
else
   echo "Erreur: no file or categorie found.";
?>
