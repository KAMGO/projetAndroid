package projetandroid.myandroidapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Martin on 26-08-17.
 */

public class Top10Jeux extends AppCompatActivity {
    Button bt_valider_nomJeu = null;
    TableLayout tl_afficheTop10 = null;
    EditText et_nomJeu = null;
    Button bt_top10_cancel = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_10_jeux);
        // prise de référence
        bt_valider_nomJeu = (Button)findViewById(R.id.bt_valider_nomJeu);
        tl_afficheTop10 = (TableLayout) findViewById(R.id.tl_afficheTop10);
        et_nomJeu = (EditText) findViewById(R.id. et_nomJeu);
        bt_top10_cancel = (Button)findViewById(R.id.bt_top10_cancel);

        //association d'un listener au bton
        bt_valider_nomJeu.setOnClickListener(listener_btn_valider);
        try{
            bt_top10_cancel.setOnClickListener(Listener_btn_retour);
        }
        catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //on masque les noms
        tl_afficheTop10.setVisibility(TableLayout.GONE);
    }

    /****************** Listener de gestion du boutton valider **********************************************
     *  une fois l'utilisateur click sur le bouton 'valider', on vérifier d'abord qu'il à saisie un jeu pour
     *  lequel on doit afficher les top 10 de score. pour l'affichage des socre il faudra utliliser une tache
     *  async qui fera appel à un RPC pour afficher les scores*/
    private View.OnClickListener listener_btn_valider = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // appel à la tache async avec passage en paramètre le nom du jeu
            String nomJeu = et_nomJeu.getText().toString().trim();
            if(!nomJeu.isEmpty()){
                AsyncTop10 top = new AsyncTop10(Top10Jeux.this);
                top.execute(nomJeu);
            }
            else{
                Toast.makeText(getApplicationContext(), "Saisir le nom du jeu", Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener Listener_btn_retour = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    // methode de récuperation des resultat
    public void populate(String[] rest){
        tl_afficheTop10.setVisibility(TableLayout.VISIBLE);
        ((TextView)findViewById(R.id.tv_top10L1C2)).setText(rest[1] != null ? rest[2] : "");
        ((TextView)findViewById(R.id.tv_top10L2C2)).setText(rest[1] != null ? rest[4] : "");
        ((TextView)findViewById(R.id.tv_top10L3C2)).setText(rest[1] != null ? rest[6] : "");
        ((TextView)findViewById(R.id.tv_top10L4C2)).setText(rest[1] != null ? rest[8] : "");
        ((TextView)findViewById(R.id.tv_top10L5C2)).setText(rest[1] != null ? rest[10] : "");
        ((TextView)findViewById(R.id.tv_top10L6C2)).setText(rest[1] != null ? rest[12] : "");
        ((TextView)findViewById(R.id.tv_top10L7C2)).setText(rest[1] != null ? rest[14] : "");
        ((TextView)findViewById(R.id.tv_top10L8C2)).setText(rest[1] != null ? rest[16] : "");
        ((TextView)findViewById(R.id.tv_top10L9C2)).setText(rest[1] != null ? rest[18] : "");
        ((TextView)findViewById(R.id.tv_top10L10C2)).setText(rest[1] != null ? rest[20] : "");
    }
    private class AsyncTop10  extends AsyncTask<String, Void, String[]> {
        private Top10Jeux top10;

        public AsyncTop10(Top10Jeux top) {
            this.top10 = top;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mainScren.tw_reposeServer.setText("Chargement en cours...");
        }

        protected String[] doInBackground(String... params) {
            String[] res;
            res = new String[21];
            try {
                // Création de l'URL du serveur avec ses paramètres
                URL url = new URL("http://192.168.0.8:8000/scripts/afficher_top.php?jeu=" + params[0]);
                // Ouverture de la connexion
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Création du JsonReader
                InputStream input = urlConnection.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(input);
                JsonReader json = new JsonReader(inputReader);
                // Lecture du fichier Json
                json.beginObject();
                json.nextName();
                res[0] = String.valueOf(json.nextInt());
                json.nextName();
                json.beginArray();
                int i = 1;
                while (json.hasNext() && i < 21) {
                    json.beginObject();
                    json.nextName();
                    res[i] = json.nextString();
                    json.nextName();
                    res[i + 1] = json.nextString();
                    json.endObject();
                    i += 2;
                }
                json.endArray();
                json.endObject();
                urlConnection.disconnect();
                return res;
            } catch (MalformedURLException e) {
                return null; //Integer.toString("-1");
            } catch (IOException e) {
                System.out.println(e.toString());
                return null; // Integer.toString(-2);
            }
        }


        /*****************/
    /* ONPOSTEXECUTE */

        /*****************/
        protected void onPostExecute(String[] result) {
            top10.populate(result);
        }
    }
}
