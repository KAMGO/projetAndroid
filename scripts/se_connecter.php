<?php
	include('inc/erreurs.inc');
	/* ETAPE 2 : CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');
	$pseudo =$_POST['pseudo'];
	$mdp =$_POST['mdp'];

	/*: TEST DES PARAMETRES */
	if (!isset($pseudo) || empty($pseudo)){
		RetournerErreur(100);
	}
	if (!isset($mdp) || empty($mdp)){
		RetournerErreur(110);
	}	

	/* VERIF LOGIN/PASSWORD */
	try{	
		$requete="SELECT id_utilisateur FROM utilisateurs 
				  WHERE pseudo=? AND mdp=?";
		$stm= $pdo->prepare($requete);
		$stm->execute(array($pseudo, $mdp));
		$row = $stm->fetch();	
	}catch(Exception $e){
		erreur(2001);
	}
	


	/* VERIFICATION DE LA COMBINAISON LOGIN/PASSWORD */
	if($row['id_utilisateur'] == 0){
		RetournerErreur(200);
	}



	/* SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	$_SESSION['id_utilisateur']=$row['id_utilisateur'];
	echo "0";
?>
