package comprovantes;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import veiculos.Novo_veiculo;

/**
 * Created by Tiziano on 28/11/2014.
 */
public class ComprovanteAbastecimento extends Activity {



    String caminho;
    final static int CAMERA_REQUEST = 1;
    public void setCaminho(String caminho) {

        this.caminho = caminho;
    }

    public String getCaminho() {

        return caminho;
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprovante_abastecimento);
        setTitle("Novo Abastecimento");
        final Spinner combobox = (Spinner) findViewById(R.id.spinner2);
        loadSpinnerData(combobox);
        final Button button2 = (Button) findViewById(R.id.buttonCamera);
        button2.setBackgroundResource(R.drawable.ic_action_camera);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int imageNum = 0;
                    final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "imageComprovanteAbast" + String.valueOf(imageNum) + ".jpg";
                    final File MBRcomprovantes = new File(Environment.getExternalStorageDirectory() + File.separator + "MbrFotos");
                    File output = new File(MBRcomprovantes, fileName);
                    MBRcomprovantes.mkdir();
                    if (MBRcomprovantes.exists()) {
                        while (output.exists()) {
                            imageNum++;
                            fileName = "imageComprovanteAbast" + String.valueOf(imageNum) + ".jpg";
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
        final String[] menu = new String[]{"Nova Quilometragem","Abastecimento", "Comprovante Geral", "Veículos","","Home"};
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
                        Intent i = new Intent(ComprovanteAbastecimento.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(ComprovanteAbastecimento.this, ListaAbast.class);//comprovantes
                        startActivity(j);
                        break;
                    case 2:
                        Intent l = new Intent(ComprovanteAbastecimento.this, ListaGeral.class);
                        startActivity(l);
                        break;
                    case 3:
                        Intent k = new Intent(ComprovanteAbastecimento.this, Novo_veiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(ComprovanteAbastecimento.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }}
        });
    }

    public void loadSpinnerData(Spinner combobox) {

        DatabaseManager db = new DatabaseManager(getApplicationContext());
        List<String> labels = db.getAllLabels("veiculos");
        if (db.getAllLabels("veiculos").isEmpty()) {
            Toast.makeText(ComprovanteAbastecimento.this, "Não há veículos cadastrados, \ncadastre um veiculo para continuar", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(ComprovanteAbastecimento.this, Novo_veiculo.class);
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
