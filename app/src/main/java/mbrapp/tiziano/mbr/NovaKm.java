package mbrapp.tiziano.mbr;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import SQL.DatabaseManager;
import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import login_control.Tela_inicial;
import tables.Quilometragem;
import tables.Veiculo;
import veiculos.ListaVeiculo;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 21/11/2014.
 */
public class NovaKm extends Activity {
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter;
    DatabaseManager db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quilometragem);

        db = new DatabaseManager(this);

        setTitle("Nova Quilometragem");
        final Button button2 = (Button) findViewById(R.id.button2);
        final Spinner combobox = (Spinner) findViewById(R.id.spinner);
        loadSpinnerData(combobox);



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quilometragem km = new Quilometragem();
                Veiculo veiculo = new Veiculo();
                final EditText editNome = (EditText) findViewById(R.id.nomeEdit);
                final EditText editDestino = (EditText) findViewById(R.id.destinoEdit);
                final EditText editKm = (EditText) findViewById(R.id.kmEdit);

                //pega o nome inserido no campo do nome
                Editable nomeEditable = editNome.getText();
                String nomeTexto = nomeEditable.toString();
                km.setNome(nomeTexto);


                //pega o destino inserido no campo do destino
                Editable destinoEditable = editDestino.getText();
                String destinoTexto = destinoEditable.toString();
                km.setDestino(destinoTexto);//destino para o banco

                //pega o valor inserido no campo do km
                Editable kmEditable = editKm.getText();
                String kmTexto = kmEditable.toString();
                km.setKm_atual(Integer.parseInt(kmTexto));//km atual para o banco

                km.setVeiculo(combobox.getSelectedItem().toString());
                km.setId(db.getId());
                km.setPlaca(db.getPlaca(km));

                try{
                    db.insertKm(km);
                    editDestino.setText("");
                    editNome.setText("");
                    editKm.setText("");
                    Toast.makeText(NovaKm.this, "Quilometragem adicionada.", Toast.LENGTH_LONG).show();
                    //KmFunctions f = new KmFunctions();
                    // /f.formatPost(nomeTexto, placaTexto, destinoTexto, kmTexto, textView);
                } catch (Exception e) {
                    System.out.println("Button press exception: " + e.getMessage());
                }

            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos","","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        dList.setAdapter(adapter);
        dList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                args.putString("Menu", menu[position]);
                switch (position) {
                    case 0:
                        Intent i = new Intent(NovaKm.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(NovaKm.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(NovaKm.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(NovaKm.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(NovaKm.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });

    }

    public void loadSpinnerData(Spinner combobox) {

        DatabaseManager db = new DatabaseManager(getApplicationContext());
        List<String> labels = db.getAllLabels("veiculo");
        if (db.getAllLabels("veiculo").isEmpty()) {
            Toast.makeText(NovaKm.this, "Não há veículos cadastrados, \ncadastre um veiculo para continuar", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(NovaKm.this, Novo_veiculo.class);
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
