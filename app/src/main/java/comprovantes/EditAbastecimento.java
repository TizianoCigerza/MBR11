package comprovantes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Abastecimento;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 19/01/2015.
 */
public class EditAbastecimento extends Activity{
    EditText editEstabelecimento;
    EditText editValor;
    EditText editLitros;
    EditText editKm;
    String caminho;
    Spinner spinner;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    DatabaseManager db;
    int comprovantePosition;
    List<Abastecimento> listAbast;
    Abastecimento abast;
    String imagemNome;


    public String getImagemNome(){
        return imagemNome;
    }

    public void setImagemNome(String nomeImagem) {
        this.imagemNome = nomeImagem;
    }

    public void setCaminho(String imagemNome){
        String caminho = Environment.getExternalStorageDirectory()+"/"+"MbrFotos/"+imagemNome+".jpg";
    }

    public String getCaminho() {
        return caminho;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprovante_abastecimento);
        editEstabelecimento = (EditText) findViewById(R.id.editAbastEstabelecimento);
        editValor = (EditText) findViewById(R.id.editValor);
        editLitros = (EditText) findViewById(R.id.editCodigo);
        editKm = (EditText) findViewById(R.id.editEstabelecimento);
        spinner = (Spinner) findViewById(R.id.spinner2);
        db = new DatabaseManager(this);
        Intent i = getIntent();
        comprovantePosition = i.getIntExtra("position", 0);
        db = new DatabaseManager(this);
        listAbast= db.resultAbastecimento("abastecimento");
        abast = listAbast.get(comprovantePosition);
        editEstabelecimento.setText(abast.getEstabelecimento());
        editValor.setText(String.valueOf(abast.getValor()));
        editLitros.setText(String.valueOf(abast.getLitros()));
        editKm.setText(String.valueOf(abast.getKm_atual()));
        loadSpinnerData(spinner);

        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos","","Home"};
        final DrawerLayout dLayout = (DrawerLayout) findViewById(R.id.lista_comp_geral_drawer);
        final ListView dList2 = (ListView) findViewById(R.id.lista_geral_left_drawer);
        try {
            ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
            dList2.setAdapter(drawerAdapter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        dList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();
                Bundle args = new Bundle();
                args.putString("Menu", menu[position]);switch (position) {
                    case 0:
                        Intent i = new Intent(EditAbastecimento.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(EditAbastecimento.this, ListaAbast.class);//comprovantes
                        startActivity(j);
                        break;
                    case 2:
                        Intent l = new Intent(EditAbastecimento.this, ListaGeral.class);
                        startActivity(l);
                        break;
                    case 3:
                        Intent k = new Intent(EditAbastecimento.this, Novo_veiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(EditAbastecimento.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }}
        });
    }


    public void loadSpinnerData(Spinner combobox) {

        DatabaseManager db = new DatabaseManager(getApplicationContext());
        List<String> labels = db.getAllLabels("veiculos");
        if (db.getAllLabels("veiculos").isEmpty()) {
            Toast.makeText(EditAbastecimento.this, "Não há veículos cadastrados, \ncadastre um veiculo para continuar", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(EditAbastecimento.this, Novo_veiculo.class);
                    startActivity(i);
                }
            }, 4000);
        } else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            combobox.setAdapter(dataAdapter);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        ImageView photo_view = (ImageView) findViewById(R.id.photo_view);
        Bitmap image = BitmapFactory.decodeFile(this.getCaminho());
        photo_view.setImageBitmap(image);
        photo_view.invalidate();
    }
}
