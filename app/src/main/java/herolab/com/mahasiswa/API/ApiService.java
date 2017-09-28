package herolab.com.mahasiswa.API;

import herolab.com.mahasiswa.Model.ModelData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {

    @FormUrlEncoded
    @POST("tambah_data.php")
    Call<ResponseBody> tambahData(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("ttl") String ttl,
            @Field("telp") String telp,
            @Field("hoby") String hoby);

    @FormUrlEncoded
    @POST("edit_data.php")
    Call<ResponseBody> editData(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("ttl") String ttl,
            @Field("telp") String telp,
            @Field("hoby") String hoby);

    @FormUrlEncoded
    @POST("hapus_data.php")
    Call<ResponseBody> hapusData(@Field("username") String username);

    @GET("lihat_data_kelas.php")
    Call<List<ModelData>> getSemuaMhs();

    @GET("single_data.php")
    Call<List<ModelData>> getSingleData(@Query("username") String username);

    @GET("login.php")
    Call<List<ModelData>> getLoginData(
            @Query("username") String username,
            @Query("password") String password );

}
