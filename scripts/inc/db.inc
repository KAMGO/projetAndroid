<?php   
    /************************************************/
    /*                                              */
    /* 1. On se connecte à la DB                    */
    /*                                              */
    /************************************************/
    try
    {
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
	    $pdo = new PDO('mysql:host=localhost;dbname=projetandroid;port=3306','root','');
	    /*$bdd = new PDO("pgsql:host=192.168.1.206;port=5432;dbname=yaya;user=postgres;password=sonic'1984micd");*/
    }
    catch (Exception $e)
    {
        RetournerErreur(1000);
    }
              
    /************************************************/
    /*                                              */
    /* 2. On force l'UTF-8                          */
    /*                                              */
    /************************************************/
    try
    {
        $sql="SET NAMES 'UTF8';";
        $pdo->exec($sql);
    }
    catch (Exception $e)
    {
        RetournerErreur(1001);
    }    
?>