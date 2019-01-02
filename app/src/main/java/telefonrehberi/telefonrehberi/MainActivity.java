package telefonrehberi.telefonrehberi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import static android.Manifest.permission.CALL_PHONE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public HorizontalScrollView horizontalScrollViewHeader;
    List<Telefon> listTelefon;
    List<Telefon> listTelefon_Ara;
    public ListView lwTelefon;
    private Dialog dialogTelefonEkle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lwTelefon = (ListView) findViewById(R.id.lwTelefon);
        this.listTelefon_Ara = new ArrayList<>();
        SearchView search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(new C10462());
        this.horizontalScrollViewHeader = (HorizontalScrollView) findViewById(R.id.horizontalScrollViewHeader2);
        this.horizontalScrollViewHeader.setHorizontalScrollBarEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // artı buttonuna tıklanırsa telefon ekle metoduna git ve id yi 0 olarak gönder
        // id yi 0 olarak göndermemizin nedeni ekleme ve güncellme işlemi ayırt etmektir
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telefonEkle(0);
            }
        });
        loadList();
        registerForContextMenu(lwTelefon);
    }
// listede ki isimler üzerinde arama yapma
    class C10462 implements SearchView.OnQueryTextListener {
        C10462() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            araTelefon(newText);
            return false;
        }
    }


    //veritabanındaki verşileri listeye ekle
    private void loadList() {
        this.listTelefon = new SQLSorgu(getApplicationContext()).getTelefon("");
        araTelefon("");
    }
//liste basılı tutulursa menüyü aç
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lwTelefon) {
            menu.setHeaderTitle(((Telefon) this.listTelefon.get(((AdapterView.AdapterContextMenuInfo) menuInfo).position)).Adi);
            menu.add(0, 1, 1, "Düzenle");
            menu.add(0, 2, 2, "Sil");
            menu.add(0, 3, 3, "Ara");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == 1) {
            //gelen id 1 ise düzeneleme yapılacak
            telefonEkle(((Telefon) this.listTelefon.get(info.position)).ID);
        } else if (item.getItemId() == 2) {
            // 2 ise silme yapılacak
            new SQLSorgu(getApplicationContext()).setDeleteTelefon(((Telefon) this.listTelefon.get(info.position)).ID);
            Toast.makeText(MainActivity.this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
            loadList();
        } else if (item.getItemId() == 3) {
//arama işlemi ve izin işlemi, izin varsa ara yoksa izin iste
            //izin verilen yer manifests dosyasındadır
            //listede tıklanan kişinin telefon numarası al ve arama yap
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.listTelefon.get(info.position).Telefon));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(i);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            }
            startActivity(i);
        }
        return true;
    }
//telefon ekleme güncellme
    private void telefonEkle(final int id) {
        dialogTelefonEkle = new Dialog(this);
        dialogTelefonEkle.setContentView(R.layout.dialog_ekle_telefon);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final EditText txtAdi = (EditText) dialogTelefonEkle.findViewById(R.id.txtAdi);
        final EditText txtSoyadi = (EditText) dialogTelefonEkle.findViewById(R.id.txtSoyadi);
        final EditText txtTelefon = (EditText) dialogTelefonEkle.findViewById(R.id.txtTelefon);
//gelen id 0 ise ekleme yapılacak demektir
        if (id != 0) {
            String query = " where ID=" + id;
            List<Telefon> list = new SQLSorgu(this).getTelefon(query);
            txtAdi.setText(list.get(0).Adi);
            txtSoyadi.setText(list.get(0).Soyadi);
            txtTelefon.setText(list.get(0).Telefon);
        }

        //telefon formatına çevir
        txtTelefon.addTextChangedListener(new TextWatcher() {
            int length_before = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length_before = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (length_before < s.length()) {
                    if (s.length() == 3 || s.length() == 7)
                        s.append("-");
                    if (s.length() > 3) {
                        if (Character.isDigit(s.charAt(3)))
                            s.insert(3, "-");
                    }
                    if (s.length() > 7) {
                        if (Character.isDigit(s.charAt(7)))
                            s.insert(7, "-");
                    }
                }
            }
        });
        dialogTelefonEkle.findViewById(R.id.btnKaydet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAdi.getText().equals("")) {
                    txtAdi.setError("Adı giriniz");
                    return;
                }
                if (txtTelefon.getText().equals("")) {
                    txtAdi.setError("Telefon giriniz");
                    return;
                }
                Telefon t = new Telefon();
                t.ID = id;
                t.Adi = txtAdi.getText().toString();
                t.Soyadi = txtSoyadi.getText().toString();
                t.Telefon = txtTelefon.getText().toString();
                //ekleme yap
                if (id == 0) new SQLSorgu(getApplicationContext()).setTelefon(t);
                //güncelleme yap ama gelen id yi güncelle sadece
                else new SQLSorgu(getApplicationContext()).setGuncelleTelefon(t);
                Toast.makeText(MainActivity.this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
                loadList();
                dialogTelefonEkle.dismiss();
            }
        });
        dialogTelefonEkle.show();
    }

    private void araTelefon(String searchText) {
        //yukarıda girilen text kısmındaki karekterlere göre liste için de arama yap
        //onda sonra listede göster
        this.listTelefon_Ara.clear();
        if (searchText.length() == 0) {
            this.listTelefon_Ara.addAll(this.listTelefon);
        } else {
            for (Telefon e : this.listTelefon) {
                if (e.Adi.toLowerCase().contains(searchText.toLowerCase())) {
                    this.listTelefon_Ara.add(e);
                }
            }
        }
        //adaptere listeyi ekle
        this.lwTelefon.setAdapter(new AdapterTelefon(MainActivity.this, this.listTelefon_Ara));
    }
}
