package SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import tables.Comprovante;
import tables.Quilometragem;
import tables.Veiculo;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class DatabaseManager extends SQLiteOpenHelper{

    public DatabaseManager(Context context) {
            super(context, database_name, null, database_version);
    }

    SQLiteDatabase db = this.getWritableDatabase();
    SQLiteDatabase dbr = this.getReadableDatabase();

    private static final String database_name = "mbr";

    private static final int database_version = 1;

    private final String create_table_abastecimento = "CREATE TABLE IF NOT EXISTS abastecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, km_atual INT, litros NUMERIC, valor NUMERIC);";

    private final String create_table_comprovante_geral = "CREATE TABLE IF NOT EXISTS comprovante_geral (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, valor REAL, data TEXT, imagem TEXT);";

    private final String create_table_estabelecimento ="CREATE TABLE IF NOT EXISTS estabelecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);";

    private final String  create_table_marca="CREATE TABLE IF NOT EXISTS marca (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);";

    private final String create_table_km = "CREATE TABLE IF NOT EXISTS km (_id INTEGER PRIMARY KEY AUTOINCREMENT, km_atual INT, destino TEXT, placa TEXT, nome TEXT, veiculo TEXT);";

    private final String create_table_veiculos = "CREATE TABLE IF NOT EXISTS veiculos (_id INTEGER PRIMARY KEY AUTOINCREMENT, placa TEXT, modelo TEXT, marca TEXT, troca_oleo TEXT);";

    private final String get_result="SELECT * FROM ";

    private final String deleteComprovante = "DELETE FROM comprovante_geral WHERE ROWID = ";

    private final String deleteKm = "DELETE FROM km WHERE ROWID = ";

    private final String deleteVeiculo = "DELETE FROM veiculos WHERE ROWID = ";

    private final String deleteTable = "DELETE FROM ";

    private final String dropTable = "DROP TABLE ";

    public void createTables(){
        db.execSQL(create_table_abastecimento);
        db.execSQL(create_table_veiculos);
        db.execSQL(create_table_comprovante_geral);
        db.execSQL(create_table_km);
        db.execSQL(create_table_estabelecimento);
        db.execSQL(create_table_marca);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        isEmpty("km");
        isEmpty("veiculos");
        isEmpty("comprovante_geral");
    }


    public void dropTable(String tableName){
        db.execSQL(dropTable.concat(tableName));
    }

    public void updateVeiculo(Veiculo veiculo){
        final String updateVeiculo = "UPDATE veiculos SET placa = '"+veiculo.getPlaca()+"', modelo = '"+veiculo.getModelo()+"', marca = '"+veiculo.getMarca()+"', troca_oleo = '"+ veiculo.getTroca_oleo()+"' WHERE _id = "+veiculo.getId()+";";
        db.execSQL(updateVeiculo);
    }

    public void updateKm(Quilometragem km){
        final String updatekm = "UPDATE km " + "SET km_atual = "+km.getKm_atual()+", destino = '"+km.getDestino()+"', placa = '"+km.getPlaca()+"', veiculo = '"+km.getVeiculo()+"' " +" WHERE _id = "+km.getId()+";";
        db.execSQL(updatekm);
    }

    public void insertKm(Quilometragem km){//working
        final String insertkm = "INSERT INTO km (km_atual, destino, placa, nome, veiculo) values ("+km.getKm_atual() + ",'" + km.getDestino() + "','" + km.getPlaca() + "','" + km.getNome() + "','"+km.getVeiculo()+ "');";
        db.execSQL(insertkm);
    }

    public void insertVeiculo(Veiculo veiculo){
        final String insertVeiculo="INSERT INTO veiculos (placa, modelo, marca, troca_oleo) values ('"+veiculo.getPlaca()+"','"+veiculo.getModelo()+"','"+veiculo.getMarca()+"','"+veiculo.getTroca_oleo()+"');";
        db.execSQL(insertVeiculo);
    }

    public void insertComprovante(Comprovante comprovante){//"CREATE TABLE IF NOT EXISTS comprovante_geral (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, valor REAL, date TEXT);";
        System.out.println("imagem: " + comprovante.getImagem());
        final String insertComprovante = "INSERT INTO comprovante_geral (estabelecimento, valor, data,imagem) values ('"+comprovante.getEstabelecimento()+"', "+comprovante.getValor()+", '"+comprovante.getData()+"', '"+comprovante.getImagem()+"');";
        db.execSQL(insertComprovante);
    }


    public void deleteTableEntries(String tableName){
        db.execSQL(deleteTable.concat(tableName).concat(";"));
        resetAutoIncrement(tableName);
    }

    public String getPlaca(Quilometragem km){
        String veiculo = km.getVeiculo();
        Cursor cursor1 = dbr.rawQuery("SELECT * FROM veiculos WHERE modelo = '"+veiculo+"' ;",null);
        cursor1.moveToFirst();
        Cursor cursor = db.rawQuery("SELECT placa FROM veiculos WHERE ROWID = "+ cursor1.getInt(cursor1.getColumnIndex("_id")),null);
        cursor.moveToFirst();
        final String placa = cursor.getString(cursor.getColumnIndex("placa"));
        return placa;
    }

    public int getKmPosition(Quilometragem km){
        int position = km.getId();
        return position;
    }

    public void deleteVeiculo(Veiculo veiculo){

        int qtdVeiculo = getCount("veiculos");
        String pos = String.valueOf(veiculo.getId());
        if(qtdVeiculo == Integer.parseInt(pos)){
            db.execSQL(deleteVeiculo.concat(pos).concat(";"));
            resetAutoIncrement("veiculos");
        }else{
            db.execSQL(deleteVeiculo.concat(pos).concat(";"));
        }
    }

    public void deleteComprovante(Comprovante comp){
        int id = comp.getId();
        String pos = String.valueOf(comp.getId()+1);
        System.out.println("comprovante id: "+comp.getId());
        int qtdComp = getCount("comprovante_geral");
        if(qtdComp == Integer.parseInt(pos)){
            db.execSQL(deleteComprovante.concat(pos).concat(";"));
            resetAutoIncrement("comprovante_geral");
        }else{
            db.execSQL(deleteComprovante.concat(pos.concat(";")));
        }
    }

    public void deleteKm(Quilometragem km){
        String pos = String.valueOf(km.getId());
        int qtdKm = getCount("km");
        if(qtdKm == Integer.parseInt(pos)){
            db.execSQL(deleteKm.concat(pos).concat(";"));
            resetAutoIncrement("km");
        }else{
            db.execSQL(deleteKm.concat(pos).concat(";"));
        }
    }

    public int getCount(String tableName){
        Cursor cursor = dbr.rawQuery("SELECT * FROM " + tableName + ";", null);
        return cursor.getCount();
    }

    public int getIdKm(){
        Cursor cursor = dbr.rawQuery("SELECT _id FROM km", null);
        cursor.moveToLast();
        int position = cursor.getPosition() + 1;
        return position;
    }

    public int getIdComprovante(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM comprovante_geral",null);
        cursor.moveToFirst();
        do{
            id = cursor.getPosition();
        }while(cursor.moveToNext());
        if(cursor.getCount()==0){
            id=1;
        }else{
            id++;
        }
        return id;
    }

    public int getIdVeiculo(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT ROWID FROM veiculos;", null);
        cursor.moveToFirst();
        do {
            id = cursor.getPosition();
        }while(cursor.moveToNext());
        if (cursor.getCount()==0){
            id=1;
        }else{
            id++;
        }
        System.out.println(id);
        return id;
    }

    public List<String> getAllLabels(String tableName){
        List<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(get_result.concat(tableName), null);//selectQuery,selectedArguments

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return list;
    }

    public void isEmpty(String tableName){
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        if(cursor.getCount()==0) {
            dbr.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '" + tableName + "';");
        }
    }

    public void resetAutoIncrement(String tableName){
        dbr.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '" + tableName + "';");
    }


    public List<Veiculo> resultVeiculo(String tableName) {
        List<Veiculo> lista = new ArrayList<Veiculo>();

        Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"), null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                Veiculo veiculo = new Veiculo();
                veiculo.setId(cursor.getInt(0));
                veiculo.setPlaca(cursor.getString(1));
                veiculo.setModelo(cursor.getString(2));
                veiculo.setMarca(cursor.getString(3));
                veiculo.setTroca_oleo(cursor.getString(4));
                System.out.println("result " + cursor.getPosition() + " id: " + cursor.getInt(0));
                lista.add(veiculo);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    public List<Quilometragem> resultKm(String tableName){
        List<Quilometragem> list = new ArrayList<Quilometragem>();
        Quilometragem km;
        Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"), null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                km = new Quilometragem();
                km.setId(cursor.getInt(0));
                km.setPlaca(cursor.getString(3));
                km.setVeiculo(cursor.getString(5));
                km.setDestino(cursor.getString(2));
                km.setNome(cursor.getString(4));
                km.setKm_atual(cursor.getInt(1));
                list.add(km);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Comprovante> resultComprovante(String tableName){
        List<Comprovante> list = new ArrayList<Comprovante>();
        Comprovante comprovante;
        Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"),null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                comprovante = new Comprovante();
                comprovante.setEstabelecimento(cursor.getString(cursor.getColumnIndex("estabelecimento")));
                comprovante.setValor(cursor.getDouble(cursor.getColumnIndex("valor")));
                comprovante.setData(cursor.getString(cursor.getColumnIndex("data")));
                comprovante.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                list.add(comprovante);
            }while(cursor.moveToNext());
        }
        return list;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}