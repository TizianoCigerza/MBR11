package comprovantes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Abastecimento;
import tables.Comprovante;
import tables.Quilometragem;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 19/01/2015.
 */
public class VisualizarAbastecimento extends Activity{

    DatabaseManager db;
    DrawerLayout dLayout;

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    String caminho;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    Bitmap image;
    List<Abastecimento> list;
    Abastecimento abastecimento;
    String imagemNome;
    int position;
    File MBRcomprovantes;
    File output;
    Intent i;
    TextView veiculo;
    TextView litros;
    TextView valor;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_abastecimento);
        db = new DatabaseManager(this);
        Intent i = getIntent();
        list = db.resultAbastecimento("abastecimento");
        setPosition(i.getIntExtra("position", 0));
        abastecimento = list.get(getPosition());
        veiculo = (TextView) findViewById(R.id.viewVeiculo);
        litros = (TextView) findViewById(R.id.viewAbastecimentoLitros);
        valor = (TextView) findViewById(R.id.viewAbastecimentoValor);
        veiculo.setText(abastecimento.getVeiculo());
        litros.setText(String.valueOf(abastecimento.getLitros()));
        valor.setText("R$ "+String.valueOf(abastecimento.getValor()));
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        Button editButton = (Button) findViewById(R.id.editButton);
        deleteButton.setBackgroundResource(R.drawable.ic_action_discard);
        editButton.setBackgroundResource(R.drawable.ic_action_edit);
        printImage(setCaminhoImagem(abastecimento.getImagem()));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (VisualizarAbastecimento.this, EditAbastecimento.class);
                i.putExtra("position",getPosition());
                startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = db.resultAbastecimento("abastecimento");
                abastecimento = list.get(getPosition());
                db.deleteAbastecimento(abastecimento);
                Toast.makeText(VisualizarAbastecimento.this, "Abastecimento removido.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(VisualizarAbastecimento.this, ListaAbast.class);
                startActivity(i);

            }
        });


        //Navigation drawer
        final String[] menu = new String[]{"Quilometragem","Abastecimento", "Comprovante Geral", "Veículos", "","Home"};
        dLayout = (DrawerLayout) findViewById(R.id.abast_drawer);
        dList2 = (ListView) findViewById(R.id.abast_leftDrawer);
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
                        Intent i = new Intent(VisualizarAbastecimento.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(VisualizarAbastecimento.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(VisualizarAbastecimento.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(VisualizarAbastecimento.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(VisualizarAbastecimento.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }


    public String setCaminhoImagem(String imagemString){
        String imageName = "/"+ imagemString.concat(".jpg");
        final File MBRcomprovantes = new File(Environment.getExternalStorageDirectory() + File.separator + "MbrFotos");
        String caminho = MBRcomprovantes.getAbsolutePath().concat(imageName);
        System.out.println(caminho);
        return caminho;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    public void printImage(String caminho){
        ImageView photo_view = (ImageView) findViewById(R.id.imagemAbastecimento);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeFile(caminho, options);
        photo_view.setImageBitmap(image);
        photo_view.invalidate();
        /*ImageView photo_view = (ImageView) findViewById(R.id.imagemAbastecimento);
        image = BitmapFactory.decodeFile(caminho);
        getResizedBitmap(image, 195,180);
        photo_view.setImageBitmap(image);
        photo_view.invalidate();*/
    }

    /*public void showImage(Abastecimento abastecimento){
        ImageView photo_view = (ImageView) findViewById(R.id.imagemComprovante);
        if(abastecimento.getImagem().equals("")){
            Intent i = getIntent();
            startActivity(i);
        }
        if (abastecimento.getImagem().length() == 23) {
            String substring = abastecimento.getImagem().substring(Math.max(abastecimento.getImagem().length() - 1, 0));
            if (image != null) {
                image.recycle();
                image = null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            image = BitmapFactory.decodeFile(getCaminho(Integer.parseInt(substring)), options);
            photo_view.setImageBitmap(image);
            System.out.println(getCaminho(Integer.parseInt(substring)));
            photo_view.invalidate();

        } else if (abastecimento.getImagem().length() > 23) {
            String substring = abastecimento.getImagem().substring(Math.max(abastecimento.getImagem().length() - 2, 0));
            if (image != null) {
                image.recycle();
                image = null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            image = BitmapFactory.decodeFile(getCaminho(Integer.parseInt(substring)), options);
            System.out.println(getCaminho(Integer.parseInt(substring)));
            photo_view.setImageBitmap(image);
            photo_view.invalidate();

        } else if (abastecimento.getImagem().length() > 24) {
            String substring = abastecimento.getImagem().substring(Math.max(abastecimento.getImagem().length() - 3, 0));
            if (image != null) {
                image.recycle();
                image = null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            image = BitmapFactory.decodeFile(getCaminho(Integer.parseInt(substring)), options);
            System.out.println(getCaminho(Integer.parseInt(substring)));
            photo_view.setImageBitmap(image);
            photo_view.invalidate();

        }
    }*/



}
