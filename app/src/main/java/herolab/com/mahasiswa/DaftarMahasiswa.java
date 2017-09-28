package herolab.com.mahasiswa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import herolab.com.mahasiswa.API.ApiService;
import herolab.com.mahasiswa.Adapter.ListArrayAdapter;
import herolab.com.mahasiswa.Model.ModelData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaftarMahasiswa extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<ModelData> datamahasiswa = new ArrayList<ModelData>();
    ListView listview;
    ListArrayAdapter adapter;

    LinearLayout layout_loading;
    TextView text_load;
    ImageView icon_load;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_mahasiswa);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        final String name = user.get(SessionManager.kunci_email);

        layout_loading = (LinearLayout) findViewById(R.id.layout_loading);
        text_load = (TextView) findViewById(R.id.text_load);
        icon_load = (ImageView) findViewById(R.id.icon_load);

        listview = (ListView) findViewById(R.id.listMhsMhs);
        listview.setOnItemClickListener(DaftarMahasiswa.this);
        listview.setDividerHeight(0);
        setup();



        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goAdd = new Intent(DaftarMahasiswa.this, RegisterActivity.class);
                startActivity(goAdd);

            }
        });


    }

    public void setup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call = service.getSemuaMhs();
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {
                datamahasiswa.clear();

                if (response.isSuccessful()) {
                    int jumlah = response.body().size();

                    for (int i = 0; i < jumlah; i++) {

                        ModelData data = new ModelData(
                                response.body().get(i).getUsername(),
                                response.body().get(i).getPassword(),
                                response.body().get(i).getNama(),
                                response.body().get(i).getTtl(),
                                response.body().get(i).getTelp(),
                                response.body().get(i).getHoby());
                        datamahasiswa.add(data);
                        Log.d("RESPON", "onResponse: " + response.body().get(i).getUsername());

                    }
                    listview.setVisibility(View.VISIBLE);
                    adapter = new ListArrayAdapter(DaftarMahasiswa.this, R.layout.row_mahasiswa, datamahasiswa);
                    listview.setAdapter(adapter);

                    if (adapter.getCount() < 1 ) {
                        layout_loading.setVisibility(View.VISIBLE);
                        String error = "Daftar mahasiswa Kosong";
                        text_load.setText(error);
                        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_data_kosong);
                        icon_load.setImageBitmap(icon);
                    } else {
                        layout_loading.setVisibility(View.GONE);
                    }
                } else {
                    String error = "Error Retrive Data from Server !!!";
                    text_load.setText(error);
                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_network);
                    icon_load.setImageBitmap(icon);

                }

            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {
                String error = "Error Retrive Data from Server wwaau!!!\n" + t.getMessage();
                text_load.setText(error);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_network);
                icon_load.setImageBitmap(icon);
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        TextView ids = (TextView) view.findViewById(R.id.listID);
        Intent intent = new Intent(DaftarMahasiswa.this, MainEdit.class);
        intent.putExtra(ModelData.username_mahasiswa, ids.getText().toString());

        startActivityForResult(intent, 1);
        */

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            adapter.clear();
            setup();
        }
    }
}
