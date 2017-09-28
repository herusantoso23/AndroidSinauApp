package herolab.com.mahasiswa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import herolab.com.mahasiswa.API.ApiService;
import herolab.com.mahasiswa.Model.ModelData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {
    String username, password, user, pass;
    int param = 0;
    private EditText txtUsername, txtPassword;
    SessionManager session;
    Button btnLogin, btnRegister;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());


        txtUsername = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.pass);
        btnLogin = (Button) findViewById(R.id.login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = txtUsername.getText().toString();
                pass = txtPassword.getText().toString();
                new AttemptLogin().execute(user, pass);
            }
        });

    }

    public void register(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    class AttemptLogin extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
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
            if(user.equals(username)){
                if(pass.equals(password)){
                    Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
                    session.createSession(username);
                    param = 0;
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Password tidak sesuai !" + param, Toast.LENGTH_SHORT).show();
                    param = 0;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Akun belum terdaftar !" + param, Toast.LENGTH_SHORT).show();
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

        Call<List<ModelData>> call = service.getSingleData(user);
        call.enqueue(new Callback<List<ModelData>>() {
            @Override
            public void onResponse(Call<List<ModelData>> call, Response<List<ModelData>> response) {

                if (response.isSuccessful()) {
                    try {
                        for (int i = 0; i < response.body().size(); i++) {
                            username = response.body().get(i).getUsername();
                            password = response.body().get(i).getPassword();
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
}
