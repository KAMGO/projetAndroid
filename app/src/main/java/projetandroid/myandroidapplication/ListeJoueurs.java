package projetandroid.myandroidapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Martin on 26-08-17.
 */

public class ListeJoueurs  extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_sers);

        final Intent intent = this.getIntent();
        ((TextView)findViewById(R.id.playerListTitle)).setText(intent.getStringExtra("usn"));

        Button btnCancel = (Button) findViewById(R.id.playerListClose);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });

        PlayerListAsync playerListAsync = new PlayerListAsync();
        playerListAsync.execute();
    }

    private class PlayerListAsync extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

        @Override
        protected String[] doInBackground(String... data) {
            String[] rsp;
            try {
                URL url = new URL("http://192.168.0.8:8000/scripts/lister_pseudos.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                int rspCode = conn.getResponseCode();
                if (rspCode == 200) {
                    rsp = new String[10000];
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader inputSteamReader = new InputStreamReader(inputStream);
                    JsonReader jsonReader = new JsonReader(inputSteamReader);
                    jsonReader.beginObject();
                    jsonReader.nextName();
                    rsp[0] = String.valueOf(jsonReader.nextInt());
                    jsonReader.nextName();
                    jsonReader.beginArray();
                    int i = 1;
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        jsonReader.nextName();
                        rsp[i] = jsonReader.nextString();
                        jsonReader.endObject();
                        i++;
                    }
                    jsonReader.endArray();
                    jsonReader.endObject();
                } else {
                    rsp = new String[1];
                    rsp[0] = String.valueOf(rspCode);
                }
                conn.disconnect();
                return rsp;
            } catch (Exception ex) {
                rsp = new String[1];
                rsp[0] = ex.getMessage();
                return rsp;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
                ShowResult(result);
        }
    }

    protected void ShowResult(String [] noms) {
        final LinearLayout layouts = (LinearLayout) findViewById(R.id.playerListLL);
        for (int i = 1; i < noms.length && noms[i] != null; i++) {
            TextView tv = new TextView(this);
            tv.setText("\u2022 " + noms[i]);
            layouts.addView(tv, 1);
        }
    }

    protected void Clear() {
        TextView etUsn = (TextView) findViewById(R.id.playerListTitle);
        Intent intent = this.getIntent();
        intent.putExtra("usn", etUsn.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
