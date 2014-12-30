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

import java.io.File;

import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 28/11/2014.
 */
public class ComprovanteGeral extends Activity {
    final static int CAMERA_REQUEST = 1;

    String caminho;

    public void setCaminho(String caminho) {

        this.caminho = caminho;
    }

    public String getCaminho() {

        return caminho;
    }

    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprovante_geral);
        setTitle("Novo Comprovante Geral");

        final EditText editValor = (EditText) findViewById(R.id.editValor);
        final EditText editCodigo = (EditText) findViewById(R.id.editCodigo);
        final EditText editData = (EditText) findViewById(R.id.editData);
        final EditText editEstabelecimento = (EditText) findViewById(R.id.editEstabelecimento);
        final Button button3 = (Button) findViewById(R.id.buttonSearch);
        final Button button2 = (Button) findViewById(R.id.buttonCamera);
        final Button button1 = (Button) findViewById(R.id.buttonCompGeral);
        button3.setBackgroundResource(R.drawable.ic_action_search);
        button2.setBackgroundResource(R.drawable.ic_action_camera);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editableValor = editValor.getText();
                String valor = editableValor.toString();

                Editable editableCodigo = editCodigo.getText();
                String codigo = editableCodigo.toString();

                Editable editableData = editData.getText();
                String data = editableData.toString();

                Editable editableEstabelecimento = editEstabelecimento.getText();
                String estabelecimento = editableEstabelecimento.toString();
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
                            output = new File(MBRcomprovantes, fileName);
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
        photo_view.invalidate();
    }



}



















