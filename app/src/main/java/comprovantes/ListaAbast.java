package comprovantes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Abastecimento;
import tables.Comprovante;
import veiculos.ListaVeiculo;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 24/11/2014.
 */
public class ListaAbast extends ListActivity {
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    DatabaseManager db;
    List<Abastecimento> lista;
    List<Comprovante> listaC;
    List<String> listaFormat;

    public List<String> formatItem(List<Abastecimento> lista){
        List<String> item = new ArrayList<String>();
        double valor;
        String veiculo;
        for(int i=0;i<lista.size();i++) {
            valor = lista.get(i).getValor();
            veiculo = lista.get(i).getVeiculo();
            System.out.println(valor);
            System.out.println(veiculo);
            item.add("\nValor:  " + valor+ "  Veiculo:  " + veiculo+ ("\n"));
        }
        return item;
    }

    public void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_abastecimento);
        db = new DatabaseManager(this);
        //db.dropTable("abastecimento");
        //db.deleteTableEntries("abastecimento");
        db.createTables();
        lista = db.resultAbastecimento("abastecimento");
        listaFormat = formatItem(lista);
        listaC = db.resultComprovante("comprovante_geral");
        ListView listaAbastecimento = (ListView) findViewById(R.id.listAbastecimento);
        if(lista.isEmpty() && listaC.isEmpty()){//checar
            File MBRcomprovantes  = new File(Environment.getExternalStorageDirectory() + File.separator + "MbrFotos");
            DeleteRecursive(MBRcomprovantes);
        }

        setTitle("Listar Abastecimento");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaFormat);
        listaAbastecimento.setAdapter(adapter);
        listaAbastecimento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListaAbast.this, VisualizarAbastecimento.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        ImageButton button = (ImageButton) findViewById(R.id.novo_comprovante_abast);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaAbast.this, ComprovanteAbastecimento.class);
                startActivity(i);
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_menu_drawer_comprovanteAbast);
        dList2 = (ListView) findViewById(R.id.lista_menu_left_drawer_comprovanteAbast);
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
                        Intent i = new Intent(ListaAbast.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(ListaAbast.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(ListaAbast.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(ListaAbast.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ListaAbast.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }





}






