package telefonrehberi.telefonrehberi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterTelefon  extends BaseAdapter{
    private Activity activity;
    private List<Telefon> listTelefon;
    private LayoutInflater mInflater;
    @SuppressLint("WrongConstant")
    public AdapterTelefon(Activity activity, List<Telefon> listTelefon) {
        this.mInflater = (LayoutInflater) activity.getSystemService("layout_inflater");
        this.listTelefon = listTelefon;
        this.activity = activity;
    }

    public int getCount() {
        return this.listTelefon.size();
    }
    public Telefon getItem(int position) {
        return (Telefon) this.listTelefon.get(position);
    }

    public long getItemId(int position) {
        return (long) ((Telefon) this.listTelefon.get(position)).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //gelen listeyi tek tek txt lere ata
        // sonra row_telefon geri gönder
        View rowView = this.mInflater.inflate(R.layout.row_telefon, parent, false);
        TextView txtAdi = (TextView) rowView.findViewById(R.id.txtAdi);
        TextView txtSoyadi = (TextView) rowView.findViewById(R.id.txtSoyadi);
        TextView txtTelefon = (TextView) rowView.findViewById(R.id.txtTelefon);
        final Telefon t = getItem(position);
        txtAdi.setText(t.Adi);
        txtSoyadi.setText(t.Soyadi);
        txtTelefon.setText(t.Telefon);
        return rowView;
    }
}
