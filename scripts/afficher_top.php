<?php
	/* CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');
	include('inc/erreurs.inc');
	$jeu = $_GET['jeu'];

	if (!isset($jeu) || empty($jeu)){
		RetournerErreur(100);
	}



	/* RECUPERATION DU TOP10 DES SCORES*/
	try{	
		$requete="SELECT pseudo, score FROM scores S INNER JOIN utilisateurs U ON S.id_utilisateur = U.id_utilisateur
				  WHERE jeu=? order by score DESC";
		$stm= $pdo->prepare($requete);
		$stm->execute(array($jeu));	
	}catch(Exception $e){
		RetournerErreur(2004);
	}

	// verification si au moins un score
	if($row = $stm->fetch()){
		$chaine = '{"pseudo": "' . $row["pseudo"] . '", "score": ' . $row["score"] . '}';
	}else{
		RetournerErreur(300);
	}

	// construition du fichier JSON des jeux
	while ($row = $stm->fetch()){
		$chaine .= ', {"pseudo": "' . $row["pseudo"] . '", "score": ' . $row["score"] . '}';
	}

	/*  envoie si pas d'erreurs*/
	$resultat='{ "code": 0, "joueurs": [' . $chaine . '] }';
	echo $resultat;
?>