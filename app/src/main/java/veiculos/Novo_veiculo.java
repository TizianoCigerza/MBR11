package veiculos;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import SQL.DatabaseManager;
import comprovantes.ComprovanteAbastecimento;
import comprovantes.ComprovanteGeral;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import tables.Veiculo;
import mbrapp.tiziano.mbr.R;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class Novo_veiculo extends Activity {

    private DrawerLayout dLayout;
    private ListView dList2;
    private ArrayAdapter drawerAdapter;
    private SQLiteDatabase banco = null;
    DatabaseManager db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novo_veiculo);
        db = new DatabaseManager(this);
        final EditText modelo = (EditText) findViewById(R.id.modeloEdit);
        final EditText marca = (EditText) findViewById(R.id.marcaEdit);
        final EditText data = (EditText) findViewById(R.id.troca_oleoEdit);
        final EditText placa = (EditText) findViewById(R.id.editPlaca);
        data.addTextChangedListener(VeiculoFunctions.insert("##/##/####", data));
        placa.addTextChangedListener(VeiculoFunctions.insert("###-####", placa));
        Button botao = (Button) findViewById(R.id.botaoVeiculo);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Veiculo veiculo = new Veiculo();

                Editable placaEdit = placa.getText();
                veiculo.setPlaca(placaEdit.toString());

                Editable marcaEdit = marca.getText();
                veiculo.setMarca(marcaEdit.toString());

                Editable modeloEdit = modelo.getText();
                veiculo.setModelo(modeloEdit.toString());

                Editable dataEdit = data.getText();
                veiculo.setTroca_oleo(dataEdit.toString());

                veiculo.setId(db.getIdVeiculo());

                try{
                    db.insertVeiculo(veiculo);
                    placa.setText("");
                    modelo.setText("");
                    marca.setText("");
                    data.setText("");
                    Toast.makeText(Novo_veiculo.this, "Veiculo Adicionado.", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }

        });

        //Navigation drawer
        LayoutInflater lI = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View nav = lI.inflate(R.layout.tela_inicial, null);
        final String[] menu = new String[]{"Quilometragem","Comprovante Geral", "Abastecimento", "Veículos","","Home"};
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
                        Intent i = new Intent(Novo_veiculo.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(Novo_veiculo.this, ComprovanteGeral.class);//comprovantes
                        startActivity(j);
                        break;
                    case 2:
                        Intent l = new Intent(Novo_veiculo.this, ComprovanteAbastecimento.class);
                        startActivity(l);
                        break;
                    case 3:
                        Intent k = new Intent(Novo_veiculo.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(Novo_veiculo.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });


    }
}