package tables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class Veiculo extends Object {

    private String placa;
    private String marca;
    private String modelo;
    private String troca_oleo;
    private int id=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTroca_oleo() {
        return troca_oleo;
    }

    public void setTroca_oleo(String troca_oleo) {
        this.troca_oleo = troca_oleo;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        try{
            Date date = formatter.parse(troca_oleo);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

}
