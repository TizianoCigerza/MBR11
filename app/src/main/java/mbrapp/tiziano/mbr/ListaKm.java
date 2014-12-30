package mbrapp.tiziano.mbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import SQL.QuilometragemCustomAdapter;
import SQL.DatabaseManager;
import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import login_control.Tela_inicial;
import tables.Quilometragem;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 24/11/2014.
 */
public class ListaKm extends Activity {
    List<String> lista;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    DatabaseManager db;
    Quilometragem km;
    List<Quilometragem> listaKm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_km);
        db = new DatabaseManager(this);
        ListView listView = (ListView) findViewById(R.id.listaKm);
        setTitle("Listar Quilometragem");
        listaKm = db.result("km");

        try{
            QuilometragemCustomAdapter adapter = new QuilometragemCustomAdapter(this.getApplicationContext() , listaKm);
            listView.setAdapter(adapter);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        ImageButton button = (ImageButton) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaKm.this, NovaKm.class);
                startActivity(i);
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_drawer);
        dList2 = (ListView) findViewById(R.id.lista_left_drawer);
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
                        Intent i = new Intent(ListaKm.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(ListaKm.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(ListaKm.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(ListaKm.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ListaKm.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }





}






