
package herolab.com.mahasiswa.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelData {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("ttl")
    @Expose
    private String ttl;


    @SerializedName("telp")
    @Expose
    private String telp;


    @SerializedName("hoby")
    @Expose
    private String hoby;

    public static final String username_mahasiswa = "USERNAME_MAHASISWA";
    public static final String nama_mahasiswa = "ID_MAHASISWA";
    public static final String jenis_mahasiswa = "ID_MAHASISWA";

    public ModelData(String username, String password, String nama, String ttl, String telp, String hoby) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.ttl = ttl;
        this.telp = telp;
        this.hoby = hoby;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getHoby() {
        return hoby;
    }

    public void setHoby(String hoby) {
        this.hoby = hoby;
    }
}
