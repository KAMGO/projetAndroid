package projetandroid.myandroidapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Martin on 26-08-17.
 */

public class ListeJeux extends AppCompatActivity {
      Button gameListClose = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affiche_jeux_actuels);
        //prise de références
         gameListClose = (Button)findViewById(R.id.gameListClose);
        //Association au listener
         gameListClose.setOnClickListener(listener_gameListClose);

        //récuperation des données tranmises et initialisation des layout correspondant
       // final Intent intent = this.getIntent();
        //((TextView)findViewById(R.id.gameListTitle)).setText(intent.getStringExtra("usn"));
        AsynListeJeux gameListAsync = new AsynListeJeux();
        gameListAsync.execute();
    }

    /*************************Listener du boutton Retout ***************************************/
    private View.OnClickListener listener_gameListClose = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private class AsynListeJeux  extends AsyncTask<String, Void, String[]> {
        private MenuPrincipal menu;

        private static final String URL_connect = "http://192.168.0.8:8000/scripts/lister_jeux.php";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected String[] doInBackground(String... params) {
            String[] rsp;
            rsp = new String[50000];
            try {
                // Création de l'URL du serveur avec ses paramètres
                URL url = new URL("http://192.168.0.8:8000/scripts/lister_jeux.php");
                // Ouverture de la connexion
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Création du JsonReader
                // Récupération et vérification du code de retour
                 int  rspCode = urlConnection.getResponseCode();
                if (rspCode == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    rsp[0]=String.valueOf(jsonReader.nextInt());

                    jsonReader.nextName();
                    jsonReader.beginArray();
                    int i=1;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        rsp[i]=jsonReader.nextString();
                        jsonReader.nextName();
                        rsp[i+1]=jsonReader.nextString();
                        i+=2;
                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                    jsonReader.endObject();
                } else {
                    rsp=new String[1];
                    rsp[0]=String.valueOf(rspCode);
                }

                urlConnection.disconnect();
                return rsp;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                System.out.println(e.toString());
                return null;
            }
        }

        /*****************/
    /* ONPOSTEXECUTE */

        /*****************/
        protected void onPostExecute(String[] jeux) {
            ((TextView) findViewById(R.id.gameListL1AC1)).setText("bonjour");

            ((TextView) findViewById(R.id.gameListL1BC2)).setText(jeux[1] != null ? jeux[1] : "");
            ((TextView) findViewById(R.id.gameListL1BC3)).setText(jeux[2] != null ? jeux[2] : "");

            ((TextView) findViewById(R.id.gameListL2BC2)).setText(jeux[3] != null ? jeux[3] : "");
            ((TextView) findViewById(R.id.gameListL2BC3)).setText(jeux[4] != null ? jeux[4] : "");

            ((TextView) findViewById(R.id.gameListL3BC2)).setText(jeux[5] != null ? jeux[5] : "");
            ((TextView) findViewById(R.id.gameListL3BC3)).setText(jeux[6] != null ? jeux[6] : "");

            ((TextView) findViewById(R.id.gameListL4BC2)).setText(jeux[7] != null ? jeux[7] : "");
            ((TextView) findViewById(R.id.gameListL4BC3)).setText(jeux[8] != null ? jeux[8] : "");

            ((TextView) findViewById(R.id.gameListL5BC2)).setText(jeux[9] != null ? jeux[9] : "");
            ((TextView) findViewById(R.id.gameListL5BC3)).setText(jeux[10] != null ? jeux[10] : "");
        }
    }
}