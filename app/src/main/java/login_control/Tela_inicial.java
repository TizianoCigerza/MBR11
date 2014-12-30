package login_control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class Tela_inicial extends Activity {
    private DrawerLayout dLayout;
    private ListView dList2;
    private ArrayAdapter drawerAdapter;


    public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tela_inicial);
        //Navigation drawer
        LayoutInflater lI = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View nav = lI.inflate(R.layout.tela_inicial, null);
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_drawer_telaInicial);
        dList2 = (ListView) findViewById(R.id.left_drawer_telaInicial);
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
                        Intent i = new Intent(Tela_inicial.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(Tela_inicial.this, ListaAbast.class);//comprovantes
                        startActivity(j);
                        break;
                    case 2:
                        Intent l = new Intent(Tela_inicial.this, ListaGeral.class);
                        startActivity(l);
                        break;
                    case 3:
                        Intent k = new Intent(Tela_inicial.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                }
            }
        });
        }
}
