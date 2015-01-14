package veiculos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
public class EditVeiculo extends Activity {
    List<Veiculo> list;
    Veiculo veiculo;
    DatabaseManager db;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    VeiculoFunctions vf;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Editar Veiculo");
        setContentView(R.layout.novo_veiculo);
        db = new DatabaseManager(this);
        Intent i = getIntent();
        int pos = i.getIntExtra("position", 0);
        list = db.resultVeiculo("veiculos");
        veiculo = list.get(pos);
        vf = new VeiculoFunctions();

        final EditText modeloEdit = (EditText) findViewById(R.id.modeloEdit);
        final EditText marcaEdit = (EditText) findViewById(R.id.marcaEdit);
        final EditText placaEdit = (EditText) findViewById(R.id.editPlaca);
        final EditText trocaOleo = (EditText) findViewById(R.id.troca_oleoEdit);
        Button button = (Button) findViewById(R.id.botaoVeiculo);
        trocaOleo.addTextChangedListener(VeiculoFunctions.insert("##/##/####", trocaOleo));
        placaEdit.addTextChangedListener(VeiculoFunctions.insert("###-####", placaEdit));
        modeloEdit.setText(veiculo.getModelo());
        marcaEdit.setText(veiculo.getMarca());
        placaEdit.setText(veiculo.getPlaca());
        trocaOleo.setText(veiculo.getTroca_oleo());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable modeloTexto = modeloEdit.getText();
                String modelo = modeloTexto.toString();
                veiculo.setModelo(modelo);

                Editable marcaTexto = marcaEdit.getText();
                String marca = marcaTexto.toString();
                veiculo.setMarca(marca);

                Editable placaTexto = placaEdit.getText();
                String placa = placaTexto.toString();
                veiculo.setPlaca(placa);

                Editable trocaOleoTexto = trocaOleo.getText();
                String troca_oleo = trocaOleoTexto.toString();
                veiculo.setTroca_oleo(troca_oleo);

                veiculo.setId(veiculo.getId());

                db.updateVeiculo(veiculo);
                modeloEdit.setText("");
                marcaEdit.setText("");
                placaEdit.setText("");
                trocaOleo.setText("");

                Toast.makeText(EditVeiculo.this, "Veiculo atualizado. ", Toast.LENGTH_LONG).show();
            }
        });

        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};

        dLayout = (DrawerLayout) findViewById(R.id.lista_drawer_telaInicial);
        dList2 = (ListView) findViewById(R.id.left_drawer_telaInicial);
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
                        Intent i = new Intent(EditVeiculo.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(EditVeiculo.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(EditVeiculo.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(EditVeiculo.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(EditVeiculo.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }
}
