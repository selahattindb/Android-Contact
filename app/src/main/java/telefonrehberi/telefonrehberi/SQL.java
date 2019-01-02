package telefonrehberi.telefonrehberi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL  extends SQLiteOpenHelper{
    //veritabanı adı
    public static final String dbName = "dbTelefon";
    public static final int version = 1;

    public SQL(Context c) {
        super(c, dbName, null, version);
    }
    //tabloyu oluştur
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table If Not Exists  tblTelefon(ID integer, Adi text, Soyadi text,  Telefon text);");

    }

    // tabloları çalıştır
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
