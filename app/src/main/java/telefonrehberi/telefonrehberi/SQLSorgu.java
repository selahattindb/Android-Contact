package telefonrehberi.telefonrehberi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLSorgu {
    private SQL sql;

    public SQLSorgu(Context c) {
        this.sql = new SQL(c);
    }

    //veritabanındaki verilerin  en küçük id sini al ve 1 eksilt
    public int getMaxID() {
        Cursor readOn = this.sql.getReadableDatabase().rawQuery("SELECT Min(ID)-1 From  tblTelefon", null);
        if (!readOn.moveToFirst()) {
            return -2;
        }
        int sonuc = readOn.getInt(0);
        if (sonuc < -1) {
            return sonuc;
        }
        return -2;
    }

    //telefon ekleme yap
    public boolean setTelefon(Telefon telefon) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        ContentValues cv = new ContentValues();
        telefon.ID = getMaxID(); //id ver
        cv.put("ID", Integer.valueOf(telefon.ID));
        cv.put("Adi", telefon.Adi);
        cv.put("Soyadi", telefon.Soyadi);
        cv.put("Telefon", telefon.Telefon);
        db.insertOrThrow("tblTelefon", null, cv);
        db.close();
        return true;
    }

    //silme işlemini yap
    public void setDeleteTelefon(int ID) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        db.delete("tblTelefon", "ID = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    //veritabanındaki telefonları getir eğere şart varsa sadece onu getir
    public List<Telefon> getTelefon(String sql) {
        ArrayList<Telefon> list = new ArrayList<>();
        SQLiteDatabase db = this.sql.getReadableDatabase();
        Cursor c = db.rawQuery("Select ID, Adi,Soyadi,Telefon from tblTelefon " + sql + " order by Adi", null);
        if (c.moveToFirst()) {
            do {
                Telefon t = new Telefon();
                t.ID = c.getInt(0);
                t.Adi = c.getString(1);
                t.Soyadi = c.getString(2);
                t.Telefon = c.getString(3);
                list.add(t);
            } while (c.moveToNext());
        }
        return list;
    }

    public boolean setGuncelleTelefon(Telefon t) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", Integer.valueOf(t.ID));
        cv.put("Adi", t.Adi);
        cv.put("Soyadi", t.Soyadi);
        cv.put("Telefon", t.Telefon);
        db.update("tblTelefon", cv, "ID = ?", new String[]{String.valueOf(t.ID)});
        db.close();
        return true;
    }
}
