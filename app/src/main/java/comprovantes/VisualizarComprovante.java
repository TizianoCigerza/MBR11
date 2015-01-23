package comprovantes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import SQL.DatabaseManager;
import login_control.Tela_inicial;
import mbrapp.tiziano.mbr.ListaKm;
import mbrapp.tiziano.mbr.R;
import tables.Comprovante;
import veiculos.ListaVeiculo;

/**
 * Created by Tiziano on 13/01/2015.
 */
public class VisualizarComprovante extends Activity {
    DatabaseManager db;
    DrawerLayout dLayout;
    ListView dList2;
    ArrayAdapter<String> drawerAdapter;
    Bitmap image;
    List<Comprovante> list;
    Comprovante comprovante;
    int position;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_comprovante);
        db = new DatabaseManager(this);
        Intent i = getIntent();
        TextView estabelecimentoView = (TextView) findViewById(R.id.viewComprovanteEstabelecimento);
        TextView dataView = (TextView) findViewById(R.id.viewComprovanteData);
        TextView valorView = (TextView) findViewById(R.id.viewComprovanteValor);
        Button editButton = (Button) findViewById(R.id.editButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        editButton.setBackgroundResource(R.drawable.ic_action_edit);
        deleteButton.setBackgroundResource(R.drawable.ic_action_discard);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (VisualizarComprovante.this, EditComprovante.class);
                i.putExtra("position",getPosition());
                startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = db.resultComprovante("comprovante_geral");
                comprovante = list.get(getPosition());
                db.deleteComprovante(comprovante);
                Toast.makeText(VisualizarComprovante.this, "Comprovante removido.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(VisualizarComprovante.this, ListaGeral.class);
                startActivity(i);
            }
        });

        try {
            setPosition(i.getIntExtra("position", 0));
            list = db.resultComprovante("comprovante_geral");
            comprovante = list.get(getPosition());
            System.out.println(comprovante.getImagem());
            printImage(setCaminhoImagem(comprovante.getImagem()));
            estabelecimentoView.setText(comprovante.getId()+comprovante.getEstabelecimento());
            dataView.setText(comprovante.getData());
            valorView.setText("R$ "+Double.toString(comprovante.getValor()));
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
                        Intent i = new Intent(VisualizarComprovante.this, ListaKm.class);//quilometragem
                        startActivity(i);
                        break;
                    case 1:
                        Intent l = new Intent(VisualizarComprovante.this, ListaAbast.class);
                        startActivity(l);
                        break;
                    case 2:
                        Intent o = new Intent(VisualizarComprovante.this, ListaGeral.class);
                        startActivity(o);
                        break;
                    case 3:
                        Intent k = new Intent(VisualizarComprovante.this, ListaVeiculo.class);
                        startActivity(k);
                        break;
                    case 5:
                        Intent m = new Intent(VisualizarComprovante.this, Tela_inicial.class);
                        startActivity(m);
                        break;
                }
            }
        });
    }


    public void printImage(String caminho) {
        ImageView photo_view = (ImageView) findViewById(R.id.imagemComprovante);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        image = BitmapFactory.decodeFile(caminho, options);
        photo_view.setImageBitmap(image);
        photo_view.invalidate();
    }


    public String setCaminhoImagem(String imagemString){
        String fileName = "/"+imagemString+".jpg";
        final File MBRcomprovantes = new File(Environment.getExternalStorageDirectory() + File.separator + "MbrFotos");
        String caminho = MBRcomprovantes.getAbsolutePath().concat(fileName);
        return caminho;
    }
/*
    public void showImage(Comprovante comprovante){
        ImageView photo_view = (ImageView) findViewById(R.id.imagemComprovante);
            if (comprovante.getImagem().length() == 23) {
                String substring = comprovante.getImagem().substring(Math.max(comprovante.getImagem().length() - 1, 0));
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
            } else if (comprovante.getImagem().length() > 23) {
                String substring = comprovante.getImagem().substring(Math.max(comprovante.getImagem().length() - 2, 0));
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
            } else if (comprovante.getImagem().length() > 24){
                String substring = comprovante.getImagem().substring(Math.max(comprovante.getImagem().length() - 3, 0));
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
