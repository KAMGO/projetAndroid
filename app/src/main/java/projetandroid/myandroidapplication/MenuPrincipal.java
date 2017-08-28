package projetandroid.myandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * Created by Martin on 17-08-17.
 */

public class MenuPrincipal extends AppCompatActivity {
    TextView tv_titre = null;
    Button bt_addScore = null;
    Button bt_top10=null;
    Button bt_listeJeux = null;
    Button bt_listeJoueurs= null;
    Button bt_cancel = null;
    public static final int passage_vers_addScore = 2;
    public static final int passage_vers_top10 = 3;
    public static final int passage_vers_ListeJeux = 4;
    public static final int passage_vers_listeJoueurs = 5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // recuperation des references
        tv_titre = (TextView) findViewById(R.id.tv_titre);
        bt_addScore = (Button)findViewById(R.id.bt_addScore);
        bt_top10 = (Button)findViewById(R.id.bt_top10);
        bt_listeJeux = (Button)findViewById(R.id.bt_listeJeux);
        bt_listeJoueurs  = (Button) findViewById(R.id.bt_listeJoueurs);
        bt_cancel = (Button)findViewById(R.id.bt_cancel);

        //récuperation des données tranmises et initialisation des layout correspondant
        Intent intent = getIntent();
        tv_titre.setText(intent.getStringExtra("nomUser"));

        // association des listener au click d'un button
        bt_addScore.setOnClickListener(listener_addScore);
        bt_top10.setOnClickListener(listener_top10);
        bt_listeJeux.setOnClickListener(listener_listeJeux);
        bt_listeJoueurs.setOnClickListener(listener_listeJoueurs);
        bt_cancel.setOnClickListener(listener_cancel);
    }
    /*****************************listener d'ajout d'un score*******************************
     * va ouvrir l'activité ajout score, ou l'utilisateur pourra ajouter son socre
     * j'ai pas besoin de faire un travail en tache asynch pour contacter le serveur
     * c'est au moment de la validation du score que je ferrai appel à la tache async*/
    private View.OnClickListener listener_addScore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*appel à la troisième activité avec retour possible: Ajout Score*/
            //il faut voir comment transmettre l'id du users car à l'ajout socre on doit
            //savoir pour qui on ajoute du score

            Intent passageVersAddScore = new Intent(MenuPrincipal.this, AddScore.class);
            passageVersAddScore.putExtra("nomUser",tv_titre.getText().toString().trim());
            startActivityForResult(passageVersAddScore , passage_vers_addScore);
        }
    };

    /******************** listener d'affichage des top 10 d'un jeu **************************
     * va ouvrir l'activité d'affichage des top 10 d'un jeu. on affiche simplement l'activité
     * sans besoin de tâche async*/
    private View.OnClickListener listener_top10 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent passageVersTop10 = new Intent(MenuPrincipal.this, Top10Jeux.class);
            startActivityForResult(passageVersTop10 , passage_vers_top10);
        }
    };

    /****************** listener d'affichage des jeux gérés ************************************
     *  ce listeners va ouvrir l'activite de d'affichage des jeux gérés actuellement par
     *  l'application "AfficheListeJeuxGeres".   */
    private View.OnClickListener listener_listeJeux = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent listejeux = new Intent(MenuPrincipal.this, ListeJeux.class);
            //TextView tvTitle = (TextView) findViewById(R.id.gameListTitle);
           // listejeux.putExtra("usn", tvTitle.getText());
            startActivityForResult(listejeux, passage_vers_ListeJeux);
        }
    };

    /********************* listener d'affichage des utilisateur **********************************
     *  nous utliliserons une tache async pour aller chercher les pseudos des users et les afficher
     *  dans une activite "listerUsers"   */
    private View.OnClickListener listener_listeJoueurs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MenuPrincipal.this, ListeJoueurs.class);
            TextView tvTitle = (TextView) findViewById(R.id.tv_titre);
            intent.putExtra("usn", tvTitle.getText());
            startActivityForResult(intent, passage_vers_listeJoueurs);
        }
    };
    /*************************Listener du boutton Retout ***************************************/
    private View.OnClickListener listener_cancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
