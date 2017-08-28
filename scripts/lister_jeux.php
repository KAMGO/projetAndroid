<?php
	include('inc/erreurs.inc');
	/* CONNEXION A LA BASE DE DONNEES */
	include('inc/db.inc');
	/* RECUPERATION DE LA LISTE DES JEUX */
	try{	
		$requete="SELECT jeu,pseudo,score FROM scores S INNER JOIN utilisateurs U ON S.id_utilisateur = U.id_utilisateur
		group by jeu
		HAVING MAX(score)
		order by score
		";
		$stm= $pdo->prepare($requete);
		$stm->execute();	
	}catch(Exception $e){
		RetournerErreur(2003);
	}

	// vérifie si on a trouvé au moins un jeu
	if($row = $stm->fetch()){
		$chaine_jeux = '{"nom_jeu":' . $row["jeu"].', "pseudo ":'. $row["pseudo"].'}';
	}else{
		RetournerErreur(300);
	}

	// construction du fichier JSON des jeux
	while ($row = $stm->fetch()) {
		$chaine_jeux .= ', {"nom_jeu":' . $row["jeu"] .', "pseudo ":'. $row["pseudo"].'}';
	}


	/*SI ON EST ARRIVE JUSQU'ICI, C'EST QUE TOUT EST CORRECT */
	$resultat='{ "code": 0, "jeux": [' . $chaine_jeux . '] }';
	echo $resultat;
?>
