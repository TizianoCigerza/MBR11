package SQL;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import mbrapp.tiziano.mbr.R;
import tables.Quilometragem;
import tables.Veiculo;

/**
 * Created by Tiziano on 29/12/2014.
 */
public class VeiculoCustomAdapter extends BaseAdapter {
    Context context;
    List<Veiculo> lista;
    Veiculo veiculo;

    public VeiculoCustomAdapter(Context context, List<Veiculo> list) {
        this.context = context;
        System.out.println("dentro do custom adapter, list: " + list.get(0)+list.get(1));
        lista = list;
        System.out.println("dentro do custom adapter, quilometragens: "+ lista.get(0)+lista.get(1));
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int color = Color.argb(255, 41, 36, 33);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_row, null);
        TextView textModelo = (TextView) convertView.findViewById(R.id.text2);
        TextView textMarca = (TextView) convertView.findViewById(R.id.text3);
        textModelo.setText("\nModelo:\n");
        textMarca.setText("\nMarca:\n");
        ImageButton imageEdit = (ImageButton) convertView.findViewById(R.id.editButton);
        ImageButton imageDelete = (ImageButton) convertView.findViewById(R.id.deleteButton);
        imageEdit.setBackgroundResource(R.drawable.ic_action_edit);
        imageDelete.setBackgroundResource(R.drawable.ic_action_cancel);

        try{
            for(int i=0;i<=lista.size();i++){
                veiculo = new Veiculo();
                veiculo = lista.get(i);
                TextView modelo = (TextView) convertView.findViewById(R.id.text2);
                TextView marca = (TextView) convertView.findViewById(R.id.text3);
                modelo.setText("\n"+veiculo.getModelo()+"\n");
                modelo.setTextColor(color);
                marca.setText("\n"+veiculo.getMarca()+"\n");
                marca.setTextColor(color);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    /*int color = Color.argb(255,41,36,33);
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
            for (int i = 1; i <= quilometragens.get(i).getId(); i++) {
                km = new Quilometragem();
                km = quilometragens.get(i);
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
        return convertView;*/
}
