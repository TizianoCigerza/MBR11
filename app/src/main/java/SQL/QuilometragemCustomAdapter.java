package SQL;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mbrapp.tiziano.mbr.R;
import tables.Quilometragem;

/**
 * Created by Tiziano on 19/12/2014.
 */

public class QuilometragemCustomAdapter extends BaseAdapter {
    Context context;
    List<Quilometragem> quilometragens;
    Quilometragem km;

    public QuilometragemCustomAdapter(Context context, List<Quilometragem> list) {
        this.context = context;
        quilometragens = list;
    }

    @Override
    public int getCount() {

        return quilometragens.size();
    }

    @Override
    public Quilometragem getItem(int position) {

        return quilometragens.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        int color = Color.argb(255,41,36,33);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_row, null);
        TextView txtV = (TextView) convertView.findViewById(R.id.text2);
        TextView txtD = (TextView) convertView.findViewById(R.id.text3);
        txtV.setText("\nVeiculo:\n");
        txtD.setText("\nDestino:\n");
        ImageButton imageB = (ImageButton) convertView.findViewById(R.id.editButton);
        ImageButton imageB2 = (ImageButton) convertView.findViewById(R.id.deleteButton);
        imageB.setBackgroundResource(R.drawable.ic_action_edit);
        imageB2.setBackgroundResource(R.drawable.ic_action_cancel);

        System.out.println("posicao get view " + quilometragens.get(0));
        try {
            for (int i = 0; i < quilometragens.size(); i++) {
                km = new Quilometragem();
                km = quilometragens.get(position);
                //System.out.println("daods dentro do for de print " + quilometragens.get(i));
                //System.out.println("indice for "+i);
                TextView nKm = (TextView) convertView.findViewById(R.id.text1);
                nKm.setText("\n" + km.getId() + "\n");

                TextView veiculo = (TextView) convertView.findViewById(R.id.textView6);
                veiculo.setText("\n\n" + km.getVeiculo() + "           \n");
                veiculo.setTextColor(color);
                //System.out.println(km.getVeiculo());

                TextView destino = (TextView) convertView.findViewById(R.id.text4);
                destino.setText("\n" + km.getDestino() + "\n");
                destino.setTextColor(color);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }





}
