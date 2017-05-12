package rto.example.com.rto.webhelper;

import android.content.Context;

import com.google.gson.Gson;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleRequest;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleResponse;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleRequest;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleResponse;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleRequest;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleRequest;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleResponse;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationRequest;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationResponse;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.GetDispatchedTawVehicleRequest;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.GetDispatchedTawVehicleResponse;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleRequest;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleRequest;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleResponse;
import rto.example.com.rto.frameworks.signin.SigninRequest;
import rto.example.com.rto.frameworks.signin.SigninResponse;
import rto.example.com.rto.frameworks.signup.SignupRequest;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.frameworks.state.GetStateRequest;
import rto.example.com.rto.frameworks.state.GetStateResponse;

/**
 * Created by James P. Zimmerman II on 3/4/16.
 */
public class WebAPIClient {
    private static final String TAG = WebAPIClient.class.getSimpleName();
    public static String baseUrl = "http://vdapplications.com/";
    private final String api_id = "b86735a86a785c910a5bb97dd961253b";
    private final String api_secret = "84c028c487d6e94dcf57873b215cb4ad";
    private Retrofit jsonClient;

    public WebAPIClient(Retrofit jsonClient) {
        this.jsonClient = jsonClient;
    }

    public static WebAPIClient getInstance(Context c) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(60, TimeUnit.MINUTES);
        builder.connectTimeout(60, TimeUnit.MINUTES);

       /* HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);*/

        fixSSL(builder);
        OkHttpClient httpClient = builder.build();

 /*       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
                setLenient().create();*/

        Retrofit json = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
//        WebAPIClient webAPIClient = new WebAPIClient(json);
        return new WebAPIClient(json);
    }

    public static void fixSSL(OkHttpClient.Builder builder) {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private WebAPIInterface json() {
        return jsonClient.create(WebAPIInterface.class);
    }


    public void login_user(SigninRequest Request, Callback<SigninResponse> callback) {
        String api_request = "login_user";
        String data = new Gson().toJson(Request);
        json().login_user(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void get_user_vehicle(GetVehicleRequest Request, Callback<GetVehicleResponse> callback) {
        String api_request = "get_user_vehicle";
        String data = new Gson().toJson(Request);
        json().get_user_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void add_vehicle(AddVehicleRequest Request, Callback<AddVehicleResponse> callback) {
        String api_request = "add_vehicle";
        String data = new Gson().toJson(Request);
        json().add_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void add_taw_vehicle(AddTawVehicleRequest Request, Callback<AddTawVehicleResponse> callback) {
        String api_request = "add_taw_vehicle";
        String data = new Gson().toJson(Request);
        json().add_taw_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void edit_vehicle(EditVehicleRequest Request, Callback<EditVehicleResponse> callback) {
        String api_request = "edit_vehicle";
        String data = new Gson().toJson(Request);
        json().edit_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void delete_vehicle(DeleteVehicleRequest Request, Callback<DeleteVehicleResponse> callback) {
        String api_request = "delete_vehicle";
        String data = new Gson().toJson(Request);
        json().delete_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void register_user(SignupRequest Request, Callback<SignupRespose> callback) {
        String api_request = "register_user";
        String data = new Gson().toJson(Request);
        json().register_user(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void search_taw_vehicle(SearchTawVehicleRequest Request, Callback<SearchTawVehicleResponse> callback) {
        String api_request = "search_taw_vehicle";
        String data = new Gson().toJson(Request);
        json().search_taw_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void search_nearest_police_station(GetNearPoliceStationRequest Request, Callback<GetNearPoliceStationResponse> callback) {
        String api_request = "search_nearest_police_station";
        String data = new Gson().toJson(Request);
        json().search_nearest_police_station(api_id, api_secret, api_request, data).enqueue(callback);
    }
//
//
//    public void update_profile(UpdateProfileRequest Request, Callback<UpdateProfileResponse> callback) {
//        String api_request = "update_profile";
//        String data = new Gson().toJson(Request);
//        json().update_profile(api_id, api_secret, api_request, data).enqueue(callback);
//    }

    public void get_state(GetStateRequest Request, Callback<GetStateResponse> callback) {
        String api_request = "get_state";
        String data = new Gson().toJson(Request);
        json().get_state(api_id, api_secret, api_request).enqueue(callback);
    }

    public void get_city(GetCityRequest Request, Callback<GetCityResponse> callback) {
        String api_request = "get_city";
        String data = new Gson().toJson(Request);
        json().get_city(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void get_dispatched_vehicle(GetDispatchedTawVehicleRequest Request, Callback<GetDispatchedTawVehicleResponse> callback) {
        String api_request = "get_officer_dispatched_taw_vehicle";
        String data = new Gson().toJson(Request);
        json().get_dispatched_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void get_officer_taw_vehicle(GetOfficerTawVehicleRequest Request, Callback<GetOfficerTawVehicleResponse> callback) {
        String api_request = "get_officer_taw_vehicle";
        String data = new Gson().toJson(Request);
        json().get_officer_taw_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void dispatch_taw_vehicle(DispatchTawVehicleRequest Request, Callback<DispatchTawVehicleResponse> callback) {
        String api_request = "dispatch_taw_vehicle";
        String data = new Gson().toJson(Request);
        json().dispatch_taw_vehicle(api_id, api_secret, api_request, data).enqueue(callback);
    }

}
