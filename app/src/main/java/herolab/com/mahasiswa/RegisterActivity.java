package herolab.com.mahasiswa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import herolab.com.mahasiswa.API.ApiService;
import herolab.com.mahasiswa.Model.ModelData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.password;
import static herolab.com.mahasiswa.R.id.pass;

public class RegisterActivity extends Activity {

    String ID_MAHASISWA;
    EditText txtUsername, txtPassword, txtNama;
    SessionManager sessionManager;
    String sPass, sNama, sUsername, bindUsername;
    int param = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sessionManager = new SessionManager(getApplicationContext());

        ID_MAHASISWA = getIntent().getStringExtra(ModelData.username_mahasiswa);

        txtUsername = (EditText) findViewById(R.id.edit_username);
        txtPassword = (EditText) findViewById(R.id.edit_password);
        txtNama = (EditText) findViewById(R.id.edit_nama);

        Button btnAdd = (Button) findViewById(R.id.simpan);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sUsername = String.valueOf(txtUsername.getText());
                sPass = String.valueOf(txtPassword.getText());
                sNama = String.valueOf(txtNama.getText());

                if(sUsername.equals("")){
                    txtUsername.setError("Username / Email tidak boleh kosong");
                } else if (sPass.equals("")){
                    txtPassword.setError("Password tidak boleh kosong");
                } else if (sNama.equals("")){
                    txtNama.setError("Nama tidak boleh kosong");
                } else if(isEmailValid(sUsername) == false){
                    txtUsername.setError("Masukan Email !!");
                } else {
                    new AttemptLogin().execute(sUsername);
                }


            }
        });
    }


    public void tambahData(final String username, String password, String nama, String ttl, String telp, String hoby) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(new StringConverter())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.tambahData(username, password, nama, ttl, telp, hoby);
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

                Toast.makeText(RegisterActivity.this, "Akun anda telah terdaftar", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, DashboardActivity.class);
                sessionManager.createSession(sUsername);
                startActivity(i);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class AttemptLogin extends AsyncTask<String, String, String>

    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Mohon tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            while (param == 0){
                bindData();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(sUsername.equals(bindUsername)){
                txtUsername.setError("sudah digunakan");
                txtUsername.setText("");
                param = 0;
            } else {
                tambahData(sUsername,sPass, sNama,"","","");
                param = 0;
            }
        }
    }

    public void bindData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        Call<List<ModelData>> call = service.getSingleData(sUsername);
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {

                if (response.isSuccessful()) {
                    try {
                        for (int i = 0; i < response.body().size(); i++) {
                            bindUsername = response.body().get(i).getUsername();
                            param = 1;
                        }
                    } catch (RuntimeException e){
                        e.printStackTrace();
                    } finally {
                        param = 2;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelData>> call, Throwable t) {
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
