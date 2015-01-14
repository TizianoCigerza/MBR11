package comprovantes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import mbrapp.tiziano.mbr.VisualizarKm;
import tables.Comprovante;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 24/11/2014.
 */
public class ListaGeral extends ListActivity {
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    DatabaseManager db;

    public List<String> formatItem(List<Comprovante> lista){
        List<String> item = new ArrayList<String>();
        double valor;
        String estabelecimento;
        for(int i=0;i<lista.size();i++) {
            valor = lista.get(i).getValor();
            estabelecimento = lista.get(i).getEstabelecimento();
            System.out.println(valor);
            System.out.println(estabelecimento);
            item.add("\nValor:  " + valor+ "  Estabelecimento:  " + estabelecimento + ("\n"));
        }
        return item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_comprovante);

        setTitle("Lista Comprovante Geral");
        List<Comprovante> lista;
        List<String> listaFormat = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listaComprovante);

        db = new DatabaseManager(this);
        db.deleteTableEntries("comprovante_geral");
        db.createTables();
        lista = db.resultComprovante("comprovante_geral");
        listaFormat = formatItem(lista);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaFormat);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListaGeral.this, VisualizarComprovante.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });


        ImageButton button = (ImageButton) findViewById(R.id.novo_comprovante);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaGeral.this, ComprovanteGeral.class);
                startActivity(i);
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos","","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_menu_drawer_comprovanteGeral);
        dList2 = (ListView) findViewById(R.id.lista_menu_left_drawer_comprovanteGeral);
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
                        Intent i = new Intent(ListaGeral.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(ListaGeral.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(ListaGeral.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(ListaGeral.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ListaGeral.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }





}






