package SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;
import tables.Quilometragem;
import tables.Veiculo;

/**
 * Created by Tiziano on 15/12/2014.
 */
public class DatabaseManager extends SQLiteOpenHelper{

    public DatabaseManager(Context context) {
            super(context, database_name, null, database_version);
    }
    String tableName;
    int position;
    List<Quilometragem> listaKm = new ArrayList<Quilometragem>();
    SQLiteDatabase db = this.getWritableDatabase();
    SQLiteDatabase dbr = this.getReadableDatabase();
    List<String> lista = new ArrayList<String>();
    int i;


    private static final String database_name = "mbr";

    private static final int database_version = 1;

    public final int id_abast = 0;

    public final int id_veiculo = 0;

    public final int id_compGeral = 0;

    public final int id_km = 0;

    public final int id_estabelecimento = 0;

    public final int id_marca = 0;


    private final String create_table_abastecimento = "CREATE TABLE IF NOT EXISTS abastecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, km_atual INT, litros NUMERIC, valor NUMERIC);";

    private final String create_table_veiculo = "CREATE TABLE IF NOT EXISTS veiculo (_id INTEGER PRIMARY KEY AUTOINCREMENT, placa TEXT, modelo TEXT, marca TEXT, troca_oleo TEXT);";

    private final String create_table_comprovante_geral = "CREATE TABLE IF NOT EXISTS comprovante_geral (_id INTEGER PRIMARY KEY AUTOINCREMENT, estabelecimento TEXT, valor REAL, date TEXT);";

    private final String create_table_estabelecimento ="CREATE TABLE IF NOT EXISTS estabelecimento (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);";

    private final String  create_table_marca="CREATE TABLE IF NOT EXISTS marca (_id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT);";

    private final String create_table_km = "CREATE TABLE IF NOT EXISTS km (_id INTEGER PRIMARY KEY AUTOINCREMENT, km_atual INT, destino TEXT, placa TEXT, nome TEXT, veiculo TEXT);";

    private final String get_result="SELECT * FROM ";

    private final String delete = "DELETE FROM "+tableName+"WHERE _id = "+position+";";

    private final String deleteTable = "DELETE FROM ";



    public List<String> resultAll(String tableName){
        List<String> lista = new ArrayList<String>();
        final Quilometragem km = new Quilometragem();
        try {
            Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"), null);
            cursor.moveToFirst();
            do {
                System.out.println("Quilometragem "+cursor.getPosition()+1);
                System.out.println("nome "+cursor.getString(4));
                System.out.println("placa "+cursor.getString(3));
                System.out.println("destino "+cursor.getString(2));
                System.out.println("km atual " + cursor.getInt(1));
                System.out.println("veiculo " + cursor.getString(5));

                km.setVeiculo(cursor.getString(5));
                km.setNome(cursor.getString(4));
                km.setPlaca(cursor.getString(3));
                km.setDestino(cursor.getString(2));
                km.setKm_atual(cursor.getInt(1));

                lista.add(km.getVeiculo());
                lista.add(km.getDestino());

            } while (cursor.moveToNext());
            dbr.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public void createTables(){
        db.execSQL(create_table_abastecimento);
        db.execSQL(create_table_veiculo);
        db.execSQL(create_table_comprovante_geral);
        db.execSQL(create_table_km);
        db.execSQL(create_table_estabelecimento);
        db.execSQL(create_table_marca);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }




    public void insertKm(Quilometragem km){//working
        final String insertkm = "INSERT INTO km (km_atual, destino, placa, nome, veiculo) values ("+km.getKm_atual() + ",'" + km.getDestino() + "','" + km.getPlaca() + "','" + km.getNome() + "','"+km.getVeiculo()+ "');";
        db.execSQL(insertkm);
    }

    public void insertVeiculo(Veiculo veiculo){
        final String insertVeiculo="INSERT INTO veiculo (placa, modelo, marca, troca_oleo) values ('"+veiculo.getPlaca()+"','"+veiculo.getModelo()+"','"+veiculo.getMarca()+"','"+veiculo.getTroca_oleo()+"');";
        db.execSQL(insertVeiculo);
    }


    public void deleteTable(String tableName){
        db.execSQL(deleteTable.concat(tableName).concat(";"));
        resetAutoIncrement(tableName);
    }

    public String getPlaca(int position){
            position++;
            Cursor cursor = db.rawQuery("SELECT placa FROM veiculo WHERE ROWID = "+ position,null);
            cursor.moveToFirst();
            final String placa = cursor.getString(cursor.getColumnIndex("placa"));
            return placa;
    }

    public void delete(String tableName,int position){
        db.rawQuery(delete,null);
    }

    public int setIdKm(){
        int id=0;
        Cursor cursor = dbr.rawQuery("SELECT ROWID FROM km", null);
        cursor.moveToFirst();
        do {
            id = cursor.getPosition();
        }while(cursor.moveToNext());

        return id;
    }


    public int getIdVeiculo(){
        int id=0;
        Cursor cursor = dbr.rawQuery("SELECT ROWID FROM veiculo", null);
        cursor.moveToFirst();
        do {
            id = cursor.getPosition();
        }while(cursor.moveToNext());
        return id;
    }


    public List<String> getAllLabels(String tableName){
        List<String> list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(get_result.concat(tableName), null);//selectQuery,selectedArguments

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }

    public void isEmpty(){
        Cursor cursor = db.rawQuery("SELECT * FROM km", null);
        if(cursor.getCount()==0) {
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = km;");
        }
    }

    public void resetAutoIncrement(String tableName){
        dbr.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '" + tableName + "';");
    }


    public List<Veiculo> resultVeiculo(String tableName){
        final Veiculo veiculo = new Veiculo();
        List<Veiculo> lista = new ArrayList<Veiculo>();

        try{
            Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"),null);
            cursor.moveToFirst();
            do{
                i=cursor.getPosition();
                veiculo.setModelo(cursor.getString(2));
                veiculo.setMarca(cursor.getString(3));
                lista.add(i,veiculo);
            }while(cursor.moveToNext());
            dbr.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
    }

    public List<Quilometragem> result(String tableName ) {
        final Quilometragem km = new Quilometragem();
        List<Quilometragem> lista = new ArrayList<Quilometragem>();

        try {
            Cursor cursor = dbr.rawQuery(get_result.concat(tableName).concat(";"), null);
            cursor.moveToFirst();
            do {
                i=cursor.getPosition();
                km.setNome(cursor.getString(4));
                km.setPlaca(cursor.getString(3));
                km.setDestino(cursor.getString(2));
                km.setKm_atual(cursor.getInt(1));
                km.setId(cursor.getInt(0));
                km.setVeiculo(cursor.getString(5));
                System.out.println(km.getId());
                lista.add(i, km);
                System.out.println("elemento a ser inserido na lista "+lista.get(i));
            } while (cursor.moveToNext());
            dbr.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}