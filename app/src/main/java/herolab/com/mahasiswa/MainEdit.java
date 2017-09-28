package herolab.com.mahasiswa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import herolab.com.mahasiswa.API.ApiService;
import herolab.com.mahasiswa.Model.ModelData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEdit extends AppCompatActivity {
    SessionManager session;
    String ID_MAHASISWA;
    EditText txtUsername, txtPassword, txtName, txtKampus, txtTelp, txtHoby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        session = new SessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        final String name = user.get(SessionManager.kunci_email);
        ID_MAHASISWA = name;

        txtUsername = (EditText) findViewById(R.id.edit_username);
        txtPassword = (EditText) findViewById(R.id.edit_password);
        txtName = (EditText) findViewById(R.id.edit_nama);
        txtKampus = (EditText) findViewById(R.id.edit_kampus);
        txtTelp = (EditText) findViewById(R.id.edit_telp);
        txtHoby = (EditText) findViewById(R.id.edit_hoby);

        bindData();

        Button btnUbah = (Button) findViewById(R.id.ubah);
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sUser = String.valueOf(txtUsername.getText());
                String sPass = String.valueOf(txtPassword.getText());
                String sNama = String.valueOf(txtName.getText());
                String sKampus = String.valueOf(txtKampus.getText());
                String sTelp = String.valueOf(txtTelp.getText());
                String sHoby = String.valueOf(txtHoby.getText());

                if(sUser.equals("")){
                    Toast.makeText(MainEdit.this, "Jangan Rubah ID", Toast.LENGTH_SHORT).show();
                } else if (sPass.equals("")){
                    Toast.makeText(MainEdit.this, "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else if (sNama.equals("")){
                    Toast.makeText(MainEdit.this, "Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    editData(sUser, sPass, sNama, sKampus,sTelp,sHoby);
                }

            }
        });

        Button btnHapus = (Button) findViewById(R.id.hapus);
        btnHapus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                hapusData();
                /*
                hapusData(ID_MAHASISWA);
                */
            }
        });

        Button btnBatal = (Button) findViewById(R.id.batal);
        btnBatal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    public void bindData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call = service.getSingleData(ID_MAHASISWA);
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {

                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        txtUsername.setText(response.body().get(i).getUsername());
                        txtPassword.setText(response.body().get(i).getPassword());
                        txtName.setText(response.body().get(i).getNama());
                        txtKampus.setText(response.body().get(i).getTtl());
                        txtTelp.setText(response.body().get(i).getTelp());
                        txtHoby.setText(response.body().get(i).getHoby());
                    }

                }

            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {

            }
        });
    }


    public void editData(String username, String password, String nama, String ttl, String telp, String hoby) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.editData(username, password, nama, ttl, telp, hoby);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                BufferedReader reader = null;
                String respon = "";

                try {
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    respon = reader.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainEdit.this, respon, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void hapusData(){
        txtName.setText("");
        txtKampus.setText("");
        txtHoby.setText("");
        txtTelp.setText("");
    }

    /*
    public void hapusData(String id_mhs) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.hapusData(id_mhs);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                BufferedReader reader = null;
                String respon = "";

                try {
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    respon = reader.readLine();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainEdit.this, respon, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    */
}
