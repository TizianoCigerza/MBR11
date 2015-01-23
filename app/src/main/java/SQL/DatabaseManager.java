package SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import tables.Abastecimento;
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

    private final String create_table_abastecimento = "CREATE TABLE IF NOT EXISTS abastecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, km_atual INT, litros NUMERIC, valor NUMERIC, veiculo TEXT, imagem TEXT);";

    private final String create_table_comprovante_geral = "CREATE TABLE IF NOT EXISTS comprovante_geral (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, valor REAL, data TEXT, imagem TEXT);";

    private final String create_table_km = "CREATE TABLE IF NOT EXISTS km (_id INTEGER PRIMARY KEY AUTOINCREMENT, km_atual INT, destino TEXT, placa TEXT, nome TEXT, veiculo TEXT);";

    private final String create_table_veiculos = "CREATE TABLE IF NOT EXISTS veiculos (_id INTEGER PRIMARY KEY AUTOINCREMENT, placa TEXT, modelo TEXT, marca TEXT, troca_oleo TEXT);";

    private final String get_result="SELECT * FROM ";

    private final String deleteComprovante = "DELETE FROM comprovante_geral WHERE ROWID = ";

    private final String deleteKm = "DELETE FROM km WHERE ROWID = ";

    private final String deleteVeiculo = "DELETE FROM veiculos WHERE ROWID = ";

    private final String deleteTable = "DELETE FROM ";

    private final String dropTable = "DROP TABLE ";

    private final String deleteAbastecimento = "DELETE FROM abastecimento WHERE ROWID = ";

    public void createTables(){
        db.execSQL(create_table_abastecimento);
        db.execSQL(create_table_veiculos);
        db.execSQL(create_table_comprovante_geral);
        db.execSQL(create_table_km);
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
//"CREATE TABLE IF NOT EXISTS abastecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, km_atual INT, litros NUMERIC, valor NUMERIC, id_abastecimento INT);";

    public void insertAbastecimento(Abastecimento abastecimento){
        final String insertAbastecimento = "INSERT INTO abastecimento (estabelecimento, km_atual, litros, valor, veiculo, imagem,id_abastecimento) values ('"+abastecimento.getEstabelecimento()+"',"+abastecimento.getKm_atual()+","+abastecimento.getLitros()+","+abastecimento.getValor()+",'"+abastecimento.getVeiculo()+"','"+abastecimento.getImagem()+"',"+abastecimento.getId()+");";
        db.execSQL(insertAbastecimento);
    }

    public void updateAbastecimento(Abastecimento abastecimento){
        final String updateAbastecimento = "UPDATE abastecimento SET estabelecimento = '"+abastecimento.getEstabelecimento()+"',km_atual = "+abastecimento.getKm_atual()+", litros = "+abastecimento.getLitros()+", valor = "+abastecimento.getValor()+"WHERE ROWID = "+abastecimento.getId()+";";
    }
    public void updateComprovante(Comprovante comprovante){
        final String updateComprovante = "UPDATE comprovante_geral SET estabelecimento = '"+comprovante.getEstabelecimento()+"', valor = "+comprovante.getValor()+", data = '"+comprovante.getData()+"', imagem = '"+comprovante.getImagem()+"' WHERE ROWID = "+comprovante.getId()+";";
        System.out.println("imagem updated: "+comprovante.getImagem());
        System.out.println(comprovante.getId());
        System.out.println(comprovante.getEstabelecimento());
        db.execSQL(updateComprovante);
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

    public void insertComprovante(Comprovante comprovante){
        System.out.println("imagem: " + comprovante.getImagem());
        final String insertComprovante = "INSERT INTO comprovante_geral (estabelecimento, valor, data,imagem, id_comprovante) values ('"+comprovante.getEstabelecimento()+"', "+comprovante.getValor()+", '"+comprovante.getData()+"', '"+comprovante.getImagem()+"'," +comprovante.getId()+");";
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

    public void deleteAbastecimento(Abastecimento abast){
        String pos = String.valueOf(abast.getId());
        db.execSQL(deleteAbastecimento.concat(pos).concat(";"));
        int qtdAbastecimento = getCount("abastecimento");
        if(qtdAbastecimento == 0){
            resetAutoIncrement("abastecimento");
        }else{
            db.execSQL(deleteAbastecimento.concat(pos).concat(";"));
        }
    }

    public void deleteVeiculo(Veiculo veiculo){
        String pos = String.valueOf(veiculo.getId());
        db.execSQL(deleteVeiculo.concat(pos).concat(";"));
        int qtdVeiculo = getCount("veiculos");
        if(qtdVeiculo == 0){
            resetAutoIncrement("veiculos");
        }
    }

    public void deleteComprovante(Comprovante comprovante){//works
        int id = comprovante.getId();
        String pos = String.valueOf(id);
        db.execSQL(deleteComprovante.concat(pos).concat(";"));
        int qtdComp = getCount("comprovante_geral");
        if(qtdComp == 0){
            resetAutoIncrement("comprovante_geral");
        }
    }

    public void deleteKm(Quilometragem km){
        String pos = String.valueOf(km.getId());
        db.execSQL(deleteKm.concat(pos).concat(";"));
        int qtdKm = getCount("km");
        if(qtdKm == 0){
            resetAutoIncrement("km");
        }
    }

    public int getCount(String tableName){
        Cursor cursor = dbr.rawQuery("SELECT * FROM " + tableName + ";", null);
        return cursor.getCount();
    }

    public int getIdKm(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM km;", null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return 1;
        }else{
            do{
                id=cursor.getInt(0);
            }while(cursor.moveToNext());
            id++;
        }
        return id;
    }

    public int getIdComprovante(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM comprovante_geral;", null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return 1;
        }else{
            do{
                id=cursor.getInt(0);
            }while(cursor.moveToNext());
            id++;
        }
        return id;
    }

    public int getIdAbastecimento(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM abastecimento",null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return 1;
        }else{
            do{
                id=cursor.getInt(0);
            }while(cursor.moveToNext());
            id++;
        }
        return id;
    }


    public int getIdComprovanteGeral(int position){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM comprovante_geral;", null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return 1;
        }else{
            do{
                id=cursor.getInt(0);
            }while(cursor.moveToNext());
            id++;
        }
        return id;
    }

    public int getIdVeiculo(){
        int id;
        Cursor cursor = dbr.rawQuery("SELECT _id FROM veiculos;", null);
        cursor.moveToFirst();
        if(cursor.getCount()==0){
            return 1;
        }else{
            do{
                id=cursor.getInt(0);
            }while(cursor.moveToNext());
            id++;
        }
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

    public List<Abastecimento> resultAbastecimento(String tableName){
        List<Abastecimento> list = new ArrayList<Abastecimento>();
        Abastecimento abast;
        Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"),null);
        if(cursor != null&& cursor.moveToFirst()){
            do{
                abast = new Abastecimento();
                abast.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                abast.setEstabelecimento(cursor.getString(cursor.getColumnIndex("estabelecimento")));
                abast.setValor(cursor.getDouble(cursor.getColumnIndex("valor")));
                abast.setKm_atual(cursor.getInt(cursor.getColumnIndex("km_atual")));
                abast.setLitros(cursor.getDouble(cursor.getColumnIndex("litros")));
                abast.setVeiculo(cursor.getString(cursor.getColumnIndex("veiculo")));
                abast.setImagem(cursor.getString(cursor.getColumnIndex("imagem")));
                System.out.println(cursor.getString(cursor.getColumnIndex("imagem")));
                list.add(abast);
            }while(cursor.moveToNext());
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
                comprovante.setId(cursor.getInt(cursor.getColumnIndex("_id")));
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