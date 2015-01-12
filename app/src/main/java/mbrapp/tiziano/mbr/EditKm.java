package mbrapp.tiziano.mbr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SQL.DatabaseManager;
import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import login_control.Tela_inicial;
import tables.Quilometragem;
import veiculos.ListaVeiculo;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 05/01/2015.
 */
public class EditKm extends Activity {
    DatabaseManager db;
    public int kmPosition;
    List<Quilometragem> listKm;
    Quilometragem km;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Editar Quilometragem");
        setContentView(R.layout.quilometragem);
        final Spinner combobox = (Spinner) findViewById(R.id.spinner);
        loadSpinnerData(combobox);
        final EditText editKm = (EditText) findViewById(R.id.kmEdit);
        final EditText editDestino = (EditText) findViewById(R.id.destinoEdit);
        final EditText editNome = (EditText) findViewById(R.id.nomeEdit);
        Button button = (Button) findViewById(R.id.button2);


        Intent i = getIntent();
        kmPosition = i.getIntExtra("position", 0);
        db = new DatabaseManager(this);
        listKm = db.resultKm("km");
        km = listKm.get(kmPosition);
        editKm.setText(km.getKm(km.getKm_atual()));
        editDestino.setText(km.getDestino());
        editNome.setText(km.getNome());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable novoKmEditable = editKm.getText();
                String novoKm = novoKmEditable.toString();
                km.setKm_atual(Integer.parseInt(novoKm));

                Editable novoDestinoEditable = editDestino.getText();
                String novoDestino = novoDestinoEditable.toString();
                km.setDestino(novoDestino);

                Editable novoNomeEditable = editNome.getText();
                String novoNome = novoNomeEditable.toString();
                km.setNome(novoNome);

                km.setVeiculo(combobox.getSelectedItem().toString());
                km.setId(km.getId());
                km.setPlaca(db.getPlaca(km));

                db.updateKm(km);
                editDestino.setText("");
                editNome.setText("");
                editKm.setText("");
                Toast.makeText(EditKm.this, "Quilometragem atualizada.", Toast.LENGTH_LONG).show();
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList2 = (ListView) findViewById(R.id.left_drawer);
        try {
            drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
            dList2.setAdapter(drawerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                args.putString("Menu", menu[position]);
                switch (position) {
                    case 0:
                        Intent i = new Intent(EditKm.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(EditKm.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(EditKm.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(EditKm.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(EditKm.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }


    public void loadSpinnerData(Spinner combobox) {

        DatabaseManager db = new DatabaseManager(getApplicationContext());
        List<String> labels = db.getAllLabels("veiculos");
        if (db.getAllLabels("veiculos").isEmpty()) {
            Toast.makeText(EditKm.this, "Não há veículos cadastrados, \ncadastre um veiculo para continuar", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(EditKm.this, Novo_veiculo.class);
                    startActivity(i);
                }
            }, 4000);
        } else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            combobox.setAdapter(dataAdapter);

        }
    }
}
