package veiculos;

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
import SQL.VeiculoCustomAdapter;
import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import tables.Quilometragem;
import mbrapp.tiziano.mbr.R;
/**
 * Created by Tiziano on 24/11/2014.
 */
public class ListaVeiculo extends Activity {
    List<String> lista;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    DatabaseManager db;
    Quilometragem km;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_veiculo);
        db = new DatabaseManager(this);
        ListView listView = (ListView) findViewById(R.id.listaVeiculo);
        setTitle("Listar Veiculos");

        try{
            VeiculoCustomAdapter adapter = new VeiculoCustomAdapter(this.getApplicationContext() , db.resultVeiculo("veiculo"));
            listView.setAdapter(adapter);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        ImageButton button = (ImageButton) findViewById(R.id.buttonListaVeiculo);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaVeiculo.this, Novo_veiculo.class);
                startActivity(i);
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_veiculo_drawer);
        dList2 = (ListView) findViewById(R.id.lista_veiculo_leftdrawer);
        try {
            drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
            dList2.setAdapter(drawerAdapter);
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        dList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                args.putString("Menu", menu[position]);
                switch (position) {
                    case 0:
                        Intent i = new Intent(ListaVeiculo.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(ListaVeiculo.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(ListaVeiculo.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(ListaVeiculo.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ListaVeiculo.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }





}






