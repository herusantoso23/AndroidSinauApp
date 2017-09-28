package herolab.com.mahasiswa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends Activity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(getApplicationContext());
    }

    public void biodata(View view){
        Intent i = new Intent(this, DaftarMahasiswa.class);
        startActivity(i);
    }

    public void profile(View view){
        Intent i = new Intent(this, MainEdit.class);
        startActivity(i);
    }

    public void info(View view){
        Toast.makeText(getApplicationContext(), "On Progress", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view){
        sessionManager.logout();
        finish();
    }

}
