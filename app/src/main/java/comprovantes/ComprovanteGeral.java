package comprovantes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.KmFunctions;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Comprovante;
import veiculos.Novo_veiculo;
import veiculos.VeiculoFunctions;

/**
 * Created by Tiziano on 28/11/2014.
 */
public class ComprovanteGeral extends Activity {
    final static int CAMERA_REQUEST = 1;

    String nomeImagem;

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    String caminho;

    public void setCaminho(String caminho) {

        this.caminho = caminho;
    }

    public String getCaminho() {

        return caminho;
    }

    DatabaseManager db;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprovante_geral);
        setTitle("Novo Comprovante Geral");
        db = new DatabaseManager(this);
        final EditText editValor = (EditText) findViewById(R.id.editValor);
        final EditText editData = (EditText) findViewById(R.id.editData);
        final EditText editEstabelecimento = (EditText) findViewById(R.id.editEstabelecimento);
        final Button button2 = (Button) findViewById(R.id.buttonCamera);
        final Button button1 = (Button) findViewById(R.id.buttonCompGeral);
        editData.addTextChangedListener(VeiculoFunctions.insert("##/##/####",editData));
        button2.setBackgroundResource(R.drawable.ic_action_camera);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comprovante comp = new Comprovante();

                Editable editableValor = editValor.getText();
                String valor = editableValor.toString();
                valor = valor.replace(",",".");
                comp.setValor(comp.toDouble(valor));


                Editable editableData = editData.getText();
                String data = editableData.toString();
                comp.setData(data);

                Editable editableEstabelecimento = editEstabelecimento.getText();
                String estabelecimento = editableEstabelecimento.toString();
                comp.setEstabelecimento(estabelecimento);

                comp.setImagem(nomeImagem);

                comp.setId(db.getIdComprovante());
                System.out.println("valor id criacao do comp: "+ comp.getId());

                db.insertComprovante(comp);
                editValor.setText("");
                editData.setText("");
                editEstabelecimento.setText("");
                Toast.makeText(ComprovanteGeral.this, "Comprovante adicionado.", Toast.LENGTH_LONG).show();

            }
        });

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
                args.putString("Menu", menu[position]);
                switch (position) {
                    case 0:
                        Intent i = new Intent(ComprovanteGeral.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(ComprovanteGeral.this, ListaAbast.class);//comprovantes
                        startActivity(j);
                        break;
                    case 2:
                        Intent l = new Intent(ComprovanteGeral.this, ListaGeral.class);
                        startActivity(l);
                        break;
                    case 3:
                        Intent k = new Intent(ComprovanteGeral.this, Novo_veiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ComprovanteGeral.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int imageNum = 0;
                    final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "imageComprovanteGeral_" + String.valueOf(imageNum) + ".jpg";
                    final File MBRcomprovantes = new File(Environment.getExternalStorageDirectory() + File.separator + "MbrFotos");
                    File output = new File(MBRcomprovantes, fileName);
                    MBRcomprovantes.mkdir();
                    if (MBRcomprovantes.exists()) {
                        while (output.exists()) {
                            imageNum++;
                            fileName = "imageComprovanteGeral_" + String.valueOf(imageNum) + ".jpg";
                            String fileNameNoJpg = "imageComprovanteGeral_" + String.valueOf(imageNum);
                            output = new File(MBRcomprovantes, fileName);
                            setNomeImagem(fileNameNoJpg);
                        }
                        File out = output;
                        setCaminho(out.getAbsolutePath());
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
                    }
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        ImageView photo_view = (ImageView) findViewById(R.id.photo_view);
        Bitmap image = BitmapFactory.decodeFile(this.getCaminho());
        photo_view.setImageBitmap(image);
        if(!photo_view.getDrawable().isVisible()){
            Bitmap image2 = BitmapFactory.decodeFile(this.getCaminho());
            photo_view.setImageBitmap(image2);
            photo_view.invalidate();
        }

    }



}



















