package mbrapp.tiziano.mbr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import SQL.DatabaseManager;
import comprovantes.ListaAbast;
import comprovantes.ListaGeral;
import login_control.Tela_inicial;
import tables.Quilometragem;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 05/01/2015.
 */
public class VisualizarKm extends Activity{

    Context context;
    List<Quilometragem> quilometragens;
    Quilometragem km;
    DatabaseManager db;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    TextView destino;
    TextView veiculo;
    TextView nome;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_km);
        Button imageB = (Button)findViewById(R.id.editButton);
        Button imageB2 = (Button) findViewById(R.id.deleteButton);
        imageB.setBackgroundResource(R.drawable.ic_action_edit);
        imageB2.setBackgroundResource(R.drawable.ic_action_discard);


        imageB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                int position = mIntent.getIntExtra("position", 0);
                km = quilometragens.get(position);
                db.deleteKm(km);
                Toast.makeText(VisualizarKm.this, "Quilometragem removida.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(VisualizarKm.this, ListaKm.class);
                startActivity(i);
            }
        });

        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                int position = mIntent.getIntExtra("position", 0);
                Intent i = new Intent(VisualizarKm.this, EditKm.class);
                i.putExtra("position",position);
                startActivity(i);

            }
        });

        try {
            Intent mIntent = getIntent();
            int position = mIntent.getIntExtra("position", 0);
            db = new DatabaseManager(this);
            context = this.getApplicationContext();
            this.quilometragens = db.resultKm("km");
            km = quilometragens.get(position);
            formatItem(km);
        }catch(Exception e){
            e.printStackTrace();
        }
        destino.setText(km.getDestino());
        veiculo.setText(km.getVeiculo());
        nome.setText(km.getNome());



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
                        Intent i = new Intent(VisualizarKm.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(VisualizarKm.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(VisualizarKm.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(VisualizarKm.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(VisualizarKm.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }

    public void formatItem(Quilometragem km){
        String nomeStr;
        String veiculoStr;
        String destinoStr;
        destino = (TextView) findViewById(R.id.viewVeiculoMarca);
        veiculo = (TextView) findViewById(R.id.viewComprovanteEstabelecimento);
        nome = (TextView) findViewById(R.id.view);
        veiculoStr = km.getVeiculo();
        destinoStr = km.getDestino();
        nomeStr = km.getNome();
        for(int i=0;i<this.quilometragens.size();i++) {
            if(destinoStr.equals("Balneario Camboriu")){
                destinoStr = "B.Camboriu";
            }else if(destinoStr.equals("Balneario camboriu")){
                destinoStr = "B.Camboriu";
            }else if(destinoStr.equals("Antonio Carlos")){
                destinoStr = "A.Carlos";
            }else if( destinoStr.equals("Antonio carlos")){
                destinoStr = "A.Carlos";
            }
            destino.setText(destinoStr);
            veiculo.setText(veiculoStr);
            nome.setText(nomeStr);
        }
    }





}
