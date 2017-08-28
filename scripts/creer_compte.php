<?php
	include('inc/erreurs.inc');

	$pseudo=$_POST['pseudo'];
	$mdp=$_POST['mdp'];
	/* CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');

	/* VERIFICATION DU LOGIN SI DEJA EXISTANT */
	try{	
		$requete="SELECT count(*) AS nbr FROM utilisateurs 
				  WHERE pseudo=?";
		$stm= $pdo->prepare($requete);
		$stm->execute(array($pseudo));
		$row = $stm->fetch();	
	}catch(Exception $e){
		RetournerErreur(2001);
	}
	if($row['nbr'] != 0){
		RetournerErreur(200);
	}
	


	/*SAUVEGARDE DANS LA DB */
	try{	
		$requete="insert into utilisateurs (id_utilisateur,mdp,pseudo) VALUES (null,?,?)";
		$stm= $pdo->prepare($requete);
		$stm->execute(array($mdp,$pseudo));
	}catch(Exception $e){
		RetournerErreur(2001);
	}



	/*  SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	echo "0";
?>
