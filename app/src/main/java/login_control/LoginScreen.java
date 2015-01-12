package login_control;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import SQL.DatabaseManager;
import login_control.AESenc;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Quilometragem;

public class LoginScreen extends Activity{
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login_screen);
        super.onCreate(savedInstanceState);
        db = new DatabaseManager(this);
        //db.deleteTable("km");
        //db.deleteTable("veiculo");
        db.createTables();
        final Intent i = new Intent(this, ListaKm.class);
        final AESenc a = new AESenc();
        final EditText editLogin = (EditText) findViewById(R.id.loginEdit);
        final EditText editPass = (EditText) findViewById(R.id.senhaEdit);
        final TextView textView = (TextView) findViewById(R.id.textView);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable login = editLogin.getText();
                String loginTexto = login.toString();

                Editable pass = editPass.getText();
                String senhaTexto = pass.toString();
                try{
                    startActivity(i);
                    //a.encrypt(loginTexto,senhaTexto,textView);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
