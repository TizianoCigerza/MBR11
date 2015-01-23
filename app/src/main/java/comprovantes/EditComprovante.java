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
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Comprovante;
import tables.Quilometragem;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 16/01/2015.
 */
public class EditComprovante extends Activity {
    DatabaseManager db;
    EditText editEstabelecimento;
    EditText editValor;
    EditText editData;
    Button salva;
    Button camera;
    ImageView editImagem;
    Comprovante comprovante;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    public int comprovantePosition;
    List<Comprovante> listComprovante;
    String nomeImagem;
    String caminho;
    String imagemNome;
    final static int CAMERA_REQUEST = 1;

    public void setCaminho(String caminho) {

        this.caminho = caminho;
    }

    public String getCaminho() {

        return caminho;
    }

    public String getNomeImagem(){
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comprovante_geral);
        editEstabelecimento = (EditText) findViewById(R.id.editEstabelecimento);
        editValor = (EditText) findViewById(R.id.editValor);
        editData = (EditText) findViewById(R.id.editData);
        editImagem= (ImageView) findViewById(R.id.photo_view);
        salva = (Button) findViewById(R.id.buttonCompGeral);
        camera = (Button) findViewById(R.id.buttonCamera);
        db = new DatabaseManager(this);
        Intent i = getIntent();
        comprovantePosition = i.getIntExtra("position", 0);
        db = new DatabaseManager(this);

        listComprovante= db.resultComprovante("comprovante_geral");
        comprovante = listComprovante.get(comprovantePosition);
        editEstabelecimento.setText(comprovante.getEstabelecimento());
        editData.setText(comprovante.getData());
        editValor.setText(String.valueOf(comprovante.getValor()));
        imagemNome = comprovante.getImagem();
        String caminho = Environment.getExternalStorageDirectory()+"/"+"MbrFotos/"+imagemNome+".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(caminho);
        editImagem.setImageBitmap(bitmap);
        camera.setBackgroundResource(R.drawable.ic_action_camera);
        camera.setOnClickListener(new View.OnClickListener() {
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


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable estabelecimentoEditable = editEstabelecimento.getText();
                String estabelecimento = estabelecimentoEditable.toString();
                comprovante.setEstabelecimento(estabelecimento);
                Editable valorEditable = editValor.getText();
                String valor = valorEditable.toString();
                comprovante.setValor(Double.parseDouble(valor));

                Editable dataEditable = editData.getText();
                String data = dataEditable.toString();
                comprovante.setData(data);

                comprovante.setId(db.getIdComprovanteGeral(comprovantePosition));
                comprovante.setImagem(getCaminho());
                try {
                    db.updateComprovante(comprovante);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });



        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.comp_geral_drawer);
        dList2 = (ListView) findViewById(R.id.geral_left_drawer);
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
                        Intent i = new Intent(EditComprovante.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(EditComprovante.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(EditComprovante.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(EditComprovante.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(EditComprovante.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        ImageView photo_view = (ImageView) findViewById(R.id.photo_view);
        Bitmap image = BitmapFactory.decodeFile(this.getCaminho());
        photo_view.setImageBitmap(image);
        String imagem = getResources().getResourceName(R.id.photo_view);
        if(imagem.isEmpty()){
            Bitmap image2 = BitmapFactory.decodeFile(this.getCaminho());
            photo_view.setImageBitmap(image2);
            photo_view.invalidate();
        }

    }
}
