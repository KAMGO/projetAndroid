package projetandroid.myandroidapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.R.attr.button;

/**
 * Created by Martin on 26-08-17.
 */

public class AddScore  extends AppCompatActivity {
    Button bt_addScore_addScore = null;
    EditText et_nomJoueur = null;
    EditText et_nomJeu = null;
    EditText et_newScore = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_score);
        bt_addScore_addScore = (Button)findViewById(R.id.bt_addScore_addScore);
        et_nomJoueur = (EditText)findViewById(R.id.et_nomJoueur);
        et_nomJeu =(EditText)findViewById(R.id.et_nomJeu);
        et_newScore= (EditText) findViewById(R.id.et_newScore);

        //récuperation des données tranmises et initialisation des layout correspondant
        Intent intent = getIntent();
        et_nomJoueur.setText(intent.getStringExtra("nomUser"));

        //association du listener au btn valider
        bt_addScore_addScore.setOnClickListener(listener_addScore_addScore);
    }

    /**************************** Listener associé au button valider *****************************************
     * permet d'encoder le nom du jeu et le socre dans la base de données; il faut faire appel à une
     * tâche asynchrome qui se chargera de l'encodage des données via une réquettes GET qui récoit le nom, score
     * et l'id du utilisateur. lorsque l'encodage c'est bien passé on ferme l'activité si non on in
     * imforme l'utilisater du problème rencontré
     */
    private View.OnClickListener listener_addScore_addScore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //appel à une fonction  async pour encodage
            String nomJeu = et_nomJeu.getText().toString().trim();
            String scoreJeu = et_newScore.getText().toString().trim();
            String pseudo  = et_nomJoueur.getText().toString().trim();
            if(!nomJeu.isEmpty() && !scoreJeu.isEmpty()){
                AsyncAddScore AddScore1 = new AsyncAddScore(AddScore.this);
                AddScore1.execute(pseudo, nomJeu, scoreJeu);
            }
            else{
                Toast.makeText(getApplicationContext(), "veuillez remplir les champs vide", Toast.LENGTH_LONG).show();
            }

            // une fois l'encodage bien passé on ferme l'activité d'ajout de score
            //finish();
        }
    };

    public void populer(String result){
        et_nomJeu.setText(result);
        if(result.equals("0")){
            Toast.makeText(getApplicationContext(), "score a été ajouter", Toast.LENGTH_LONG).show();
        }
        else{
            et_nomJeu.setText(result);
        }
        //finish();
    }



    private class AsyncAddScore extends AsyncTask<String, Void, String> {
        private AddScore ajoutScr;

        public AsyncAddScore(AddScore addscore) {
            ajoutScr = addscore;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mainScren.tw_reposeServer.setText("Connexion en cours...");
        }

        @Override
        protected String doInBackground(String... params) {


            String codeR="";
            try {
                URL url = new URL("http://192.168.0.8:8000/scripts/ajouter_score.php?score=" + params[2] + "&jeu=" + params[1]+"&pseudo=" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //faire la demande de connexion
                connection.connect();
                int reponseCode = connection.getResponseCode();
                if (reponseCode == 200) {
                    InputStream reponseRPC = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(reponseRPC, "UTF-8");
                    Scanner scanner = new Scanner(inputStreamReader);
                    while (scanner.hasNext()){
                    codeR = codeR+scanner.next();}
                    connection.disconnect();
                    codeR = codeR.toString();
                } else {
                    codeR = String.valueOf(404);
                }
                return codeR;
            } catch (MalformedURLException e) {
                return String.valueOf(-1);
            } catch (IOException e) {
                return String.valueOf(-2);
            }
        }

        @Override
        protected void onPostExecute(String chaine) {
            ajoutScr.populer(chaine);
        }
    }
}
