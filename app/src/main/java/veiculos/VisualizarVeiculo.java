package veiculos;

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
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Veiculo;

/**
 * Created by Tiziano on 09/01/2015.
 */
public class VisualizarVeiculo extends Activity {

    Context context;
    List<Veiculo> veiculos;
    DatabaseManager db;
    Veiculo veiculo;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    public void formatItem(Veiculo veiculo){
        String modelo;
        String marca;
        String placa;
        String data;

        TextView modeloText = (TextView) findViewById(R.id.viewComprovanteEstabelecimento);
        TextView marcaText = (TextView) findViewById(R.id.viewVeiculoMarca);
        TextView placaText = (TextView) findViewById(R.id.viewComprovanteData);
        TextView dataText = (TextView) findViewById(R.id.viewVeiculoData);

        modelo = veiculo.getModelo();
        marca = veiculo.getMarca();
        placa = veiculo.getPlaca();
        data = veiculo.getTroca_oleo();

            modeloText.setText(modelo);
            marcaText.setText(marca);
            placaText.setText(placa);
            dataText.setText(data);

        modeloText.setText(veiculo.getModelo());
        marcaText.setText(veiculo.getMarca());
        dataText.setText(veiculo.getTroca_oleo());
        placaText.setText(veiculo.getPlaca());
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_veiculo);
        db = new DatabaseManager(this);


        Button imageB = (Button)findViewById(R.id.editButton);
        Button imageB2 = (Button) findViewById(R.id.deleteButton);
        imageB.setBackgroundResource(R.drawable.ic_action_edit);
        imageB2.setBackgroundResource(R.drawable.ic_action_discard);

        imageB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                int position = mIntent.getIntExtra("position", 0);
                veiculo = veiculos.get(position);
                db.deleteVeiculo(veiculo);
                Toast.makeText(VisualizarVeiculo.this, "Veiculo removido.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(VisualizarVeiculo.this, ListaVeiculo.class);
                startActivity(i);
            }
        });

        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                int position = mIntent.getIntExtra("position", 0);
                Intent i = new Intent(VisualizarVeiculo.this, EditVeiculo.class);
                i.putExtra("position",position);
                startActivity(i);

            }
        });

        try {
            Intent mIntent = getIntent();
            int position = mIntent.getIntExtra("position", 0);
            db = new DatabaseManager(this);
            context = this.getApplicationContext();
            this.veiculos = db.resultVeiculo("veiculos");
            veiculo = veiculos.get(position);
            formatItem(veiculo);
        }catch(Exception e){
            e.printStackTrace();
        }

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.lista_drawer);
        dList2 = (ListView) findViewById(R.id.lista_left_drawer);
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
                        Intent i = new Intent(VisualizarVeiculo.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(VisualizarVeiculo.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(VisualizarVeiculo.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(VisualizarVeiculo.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(VisualizarVeiculo.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }


}
