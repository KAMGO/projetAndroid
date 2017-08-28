<?php
	include('inc/erreurs.inc');

	/* CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');



	/* RECUPERATION DE LA LISTE DES JEUX */
	try{	
		$requete="SELECT pseudo FROM utilisateurs";
		$stm= $pdo->prepare($requete);
		$stm->execute();	
	}catch(Exception $e){
		RetournerErreur(2003);
	}

	// On vérifie si on a trouvé au moins un jeu
	if($row = $stm->fetch()){
		$chaine = '{"pseudo": "' . $row["pseudo"] . '"}';
	}else{
		RetournerErreur(300);
	}

	//construire le fichier JSON des jeux
	while ($row = $stm->fetch()) {
		$chaine .= ', {"pseudo": "' . $row["pseudo"] . '"}';
	}



	/*SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	$resultat='{ "code": 0, "utilisateurs": [' . $chaine . '] }';
	echo $resultat;
?>
