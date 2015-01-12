package tables;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class Quilometragem {
    private int km_atual;
    private String destino;
    private String placa;
    private String nome;
    private String veiculo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id=0;

    public String getNome() {
        return nome;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getKm_atual() {
        return km_atual;
    }

    public void setKm_atual(int km_atual) {
        this.km_atual = km_atual;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getKm(int km_atual){
        String kmTxt = String.valueOf(km_atual);
        return kmTxt;
    }

    @Override
    public String toString() {
        return ("placa:"+this.getPlaca()+
                " nome: "+ this.getNome() +
                " destino: "+ this.getDestino() +
                " km_atual: " + this.getKm_atual());
    }
}
