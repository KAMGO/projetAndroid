package projetandroid.myandroidapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static  final int passage_vers_Menu = 1;
    EditText et_Login =null;
    TextView tv_login = null;
    TextView tv_messages = null;
    EditText et_password = null;
    Button   bt_login = null;
    Button   bt_register = null;
    LinearLayout register = null ;
    EditText et_registerLogin = null;
    EditText et_registerPassword1 = null;
    EditText et_registerPassword2 = null;
    Button bt_registerRegister = null;
    Button bt_registerCancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_login = (TextView) findViewById(R.id.tv_Login);
        tv_messages = (TextView) findViewById(R.id.tv_messages);
        et_password = (EditText) findViewById(R.id.et_password);
        et_Login = (EditText) findViewById(R.id.et_login);
        et_registerLogin = (EditText) findViewById(R.id.et_registerLogin);
        et_registerPassword1= (EditText) findViewById(R.id.et_registerPassword1);
        et_registerPassword2 = (EditText) findViewById(R.id.et_registerPassword2);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_registerRegister = (Button)findViewById(R.id.bt_registerRegister);
        bt_registerCancel = (Button)findViewById(R.id.bt_registerCancel);
        register = (LinearLayout)findViewById(R.id.register);

        // au demarage de l'application la partie register est cachée

        register.setVisibility(LinearLayout.GONE);

        bt_login.setOnClickListener(listener_bt_login);
        bt_register.setOnClickListener(listener_bt_register);
        bt_registerRegister.setOnClickListener(listener_bt_registerRegister);
        bt_registerCancel.setOnClickListener(listener_bt_registerCancel);
    }
    /************************************************************************************************************************/
    /*                                creation des listeners relatifs a chaque boutton                                      */
    /************************************************************************************************************************/

    // cette listener permet d'ouvrier la partie d'enregistrement

    private View.OnClickListener listener_bt_register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tv_messages = (TextView)findViewById(R.id.tv_messages);
            tv_messages.setText("");
            register = (LinearLayout) findViewById(R.id.register);
            register.setVisibility(LinearLayout.VISIBLE);

            // une fois le boutton register cliqué on vide les wizards
            et_registerLogin.setText("");
            et_registerPassword1.setText("");
            et_registerPassword2.setText("");
        }
    };


    // cette listener permet de valider le formulaire d'enregistrement

    private View.OnClickListener listener_bt_registerRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tv_messages = (TextView)findViewById(R.id.tv_messages);
            String login = et_registerLogin.getText().toString().trim();
            String mdp1 = et_registerPassword1.getText().toString().trim();
            String mdp2 = et_registerPassword2.getText().toString().trim();

            // cette condition permet de verifier si les deux mots de passe sont identique et que le login n'est pas vide
            if( mdp1.equals(mdp2) && !login.equals("") ){
                AsynCreerCompte creationCompte = new AsynCreerCompte(MainActivity.this);
                creationCompte.execute(login, mdp1);
            }
            else{
                tv_messages.setText("Les mots de passe ne correspondent pas ou alors le login est vide");
                Toast.makeText(getApplicationContext(), "veuillez remplir les champs vide", Toast.LENGTH_LONG).show();
                et_registerLogin.setText("");
                et_registerPassword1.setText("");
                et_registerPassword2.setText("");
            }
        }
    };
   // connexion avec son login et son mot de passe
   private View.OnClickListener listener_bt_login = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           String pseudo = et_Login.getText().toString().trim();
           String mdp = et_password.getText().toString().trim();
            //appel à la tâche asynchrone pour vérification du login et mot passe
           if (!pseudo.isEmpty() && !mdp.isEmpty()) {
              AsyncLogin seConnecter = new AsyncLogin(MainActivity.this);
               seConnecter.execute(pseudo, mdp);
           } else {
               Toast.makeText(getApplicationContext(), "veuillez remplir les champs vides ", Toast.LENGTH_LONG).show();
           }
       }
   };
    //quitter la partie enregistrement
    private View.OnClickListener listener_bt_registerCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            register.setVisibility(LinearLayout.GONE);
            // une fois le boutton cancel cliqué on vide les wizards
            et_registerLogin.setText("");
            et_registerPassword1.setText("");
            et_registerPassword2.setText("");
        }
    };
    //cette methode prend en parametre le code retourné l'hors de la creation d'un compte et affiche un message descriptif
    public void decodeCreatCompte(String codeR){
        tv_messages = (TextView)findViewById(R.id.tv_messages);
        tv_messages.setText("");
        if(codeR.equals("0") ){
            tv_messages.setText("votre enregistrement est efectif ");
        }
        else{
            if(codeR.equals("100")){
                tv_messages.setText(" votre pseudo n'est pas transmis ou est vide)");
            }
            else
            if (codeR.equals("110"))
                tv_messages.setText("votre mot de passe n'est pas transmis ou est vide");
            else
            if(codeR.equals("200"))
                tv_messages.setText("desolé Pseudo déjà existant ");
            else
                tv_messages.setText(codeR);
        }
        register.setVisibility(LinearLayout.GONE);
    }

    //cette methode prend en parametre le code retourné l'hors d'une connection et affiche son descrptif

    public void decodeConexion(String codeR){
        tv_messages = (TextView)findViewById(R.id.tv_messages);
        String res="";
        // puisque le rpc renvoi un nombre auve des quote donc j'utlise d'abord un tableau char et ensuite j'extrait le nombre
         char[] tab = codeR.toCharArray();
        for(int i = 0; i< tab.length ; i++){
           res += Character.toString(tab[i]);
        }

        if(true){
            Intent passageVersMenu = new Intent(MainActivity.this, MenuPrincipal.class);
            passageVersMenu.putExtra("nomUser",et_Login.getText().toString().trim());
            startActivityForResult(passageVersMenu, passage_vers_Menu);
        }
        else{
            et_password.setText("");
            if(res.equals("100")){
                tv_messages.setText(" Problème de pseudo(non transmis ou vide)");
            }
            else
            if (res.equals("110"))
                tv_messages.setText(res + "Problème de mot de passe (non transmis ou vide) ");
            else
            if(res.equals("200"))
                tv_messages.setText(res +"Combinaison pseudo/mot de passe incorrecte ");
            else
               tv_messages.setText(res +  " Problème de connexion à la DB : "+codeR );

        }
    }
    // creation de la classe asynchrone pour la creation d'un compte
    private class AsynCreerCompte extends AsyncTask<String, Void, String> {

        private MainActivity ecranMain;
        public AsynCreerCompte(MainActivity ecranMain){
            this.ecranMain = ecranMain;
        }
        private static final String URL_connect = "http://192.168.0.8:8000/scripts/creer_compte.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ecranMain.tv_messages.setText(URL_connect);
        }

        @Override
        protected String doInBackground(String ...params) {
            String codeR ;
            try{
                URL url = new URL(URL_connect);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // declaration de la methode pour les envois
                connection.setRequestMethod("POST");
               // connection.setConnectTimeout(6000);// précision du temps d'attente
                //progress.setMessage("Ecriture des données...");
                //progress.show();
                // etant donnee que la methode est un post,on utilise  OutputStrean
                OutputStream os = connection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String param = "pseudo=" + params[0] + "&mdp=" + params[1];
                writer.write(param);
                writer.flush();
                writer.close();
                os.close();

                //connexion au RPC
                connection.connect();
                //recuperation de la reponse
                int reponseCode = connection.getResponseCode();
                if(reponseCode == 200){
                    InputStream reponseRPC = connection.getInputStream();
                    InputStreamReader  inputStreamReader = new InputStreamReader(reponseRPC );/*, "UTF-8");*/
                   // JsonReader json_reader = new JsonReader(inputStreamReader);
                   // json_reader.beginObject();
                   // json_reader.nextName();
                   // codeR= json_reader.nextString();
                  //  json_reader.endObject();
                    Scanner scanner = new Scanner(inputStreamReader);
                    codeR="o";
                    while (scanner.hasNext()) {
                        codeR = codeR + scanner.next();
                    }
                    connection.disconnect();
                    //return codeR;
                }
               else{
                   // return  Integer.toString(404);
                    codeR = String.valueOf(4);
               }
                return codeR;
            }
            catch (MalformedURLException e){
                return Integer.toString(-1);
            } catch (IOException e){
               return Integer.toString(-2);
            }
        }

        @Override
        protected void onPostExecute(String chaine) {
            ecranMain.decodeCreatCompte(chaine);
        }
}

// creation de la classe asynchrone pour se connecter
    private class AsyncLogin  extends AsyncTask<String, Void, String> {
    private MainActivity ecranMain;

    public AsyncLogin(MainActivity ecranMain){
        this.ecranMain = ecranMain;
    }

    private static final String URL_connect ="http://192.168.0.8:8000/scripts/se_connecter.php";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ecranMain.tv_messages.setText("Connexion en cours d'execution...");
    }

    @Override
    protected String doInBackground(String ...params) {
        String codeR ;
        try{
            URL url = new URL(URL_connect);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // envoye des parmètres vu que c'est un post on utlise la classe OutputStrean
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            String parametres_post = "pseudo=" + params[0] + "&mdp=" + params[1] ;
            writer.write(parametres_post);
            writer.flush();
            writer.close();
            os.close();

            //faire la demande de connexion
            connection.connect();
            int reponseCode = connection.getResponseCode();
            if(reponseCode == 200){
                InputStream reponseRPC = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(reponseRPC,  "UTF-8");
                Scanner scanner = new Scanner(inputStreamReader);
                codeR = scanner.next();
                connection.disconnect();
                codeR= codeR.toString();
            }
            else{
                codeR= Integer.toString(404);
            }
            return codeR;
        }
        catch (MalformedURLException e){
            return Integer.toString(-1);
        } catch (IOException e){
            return Integer.toString(-2);
        }
    }

    @Override
    protected void onPostExecute(String chaine) {
        ecranMain.decodeConexion(chaine);
    }
}


}
