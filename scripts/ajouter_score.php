<?php
	/*CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');
	include('inc/erreurs.inc');
	$score = $_GET['score'];
	$jeu = $_GET['jeu'];
	$pseudo = $_GET['pseudo'];


	/* TEST DES PARAMETRES */
	if (!isset($score) || empty($score)){
		RetournerErreur(100);
	}
	if (!isset($jeu) || empty($jeu)){
		RetournerErreur(110);
	}

	/*SAUVEGARDE DANS LA DB */
	try{
		$requete="SELECT id_utilisateur FROM utilisateurs 
		WHERE pseudo=?";
		$stm1= $pdo->prepare($requete);
		$stm1->execute(array($pseudo));
		$row1 = $stm1->fetch();
		$requete="INSERT INTO scores (id_score,jeu,score,id_utilisateur) VALUES (null,?,?,?)";
		$stm= $pdo->prepare($requete);
		$stm->execute(array($jeu, $score,$row1['id_utilisateur']));
	}catch(Exception $e){
		RetournerErreur(2002);
	}



	/*  retourne 0 si tout ok */
	echo "0";

?>
