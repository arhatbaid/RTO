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
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.city.GetCityRequest;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleRequest;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleRequest;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleRequest;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.frameworks.signin.SigninRequest;
import rto.example.com.rto.frameworks.signin.SigninResponse;
import rto.example.com.rto.frameworks.signup.SignupRequest;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.frameworks.state.GetStateData;
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
        json().get_state(api_id, api_secret, api_request, data).enqueue(callback);
    }

    public void get_city(GetCityRequest Request, Callback<GetCityResponse> callback) {
        String api_request = "get_city";
        String data = new Gson().toJson(Request);
        json().get_city(api_id, api_secret, api_request, data).enqueue(callback);
    }

//    public void validate_otp(ValidateOtpRequest Request, Callback<ValidateOtpResponse> callback) {
//        String api_request = "validate_otp";
//        String data = new Gson().toJson(Request);
//        json().validate_otp(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void resend_otp(ReSendOtpRequest Request, Callback<ResendOtpResponse> callback) {
//        String api_request = "resend_otp";
//        String data = new Gson().toJson(Request);
//        json().resend_otp(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void register_for_push(RegisterForPushRequest Request, Callback<RegisterForPushResponse> callback) {
//        String api_request = "register_for_push";
//        String data = new Gson().toJson(Request);
//        json().register_for_push(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void my_account_balance(GetAccountBalanceRequest Request, Callback<GetAccountBalanceResponse> callback) {
//        String api_request = "get_account_points";
//        String data = new Gson().toJson(Request);
//        json().my_account_balance(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_offers(GetOfferesRequest Request, Callback<GetOfferesResponse> callback) {
//        String api_request = "get_offers";
//        String data = new Gson().toJson(Request);
//        json().get_offers(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_hot_news(GetHotNewsRequest Request, Callback<GetHotNewsResponse> callback) {
//        String api_request = "get_hot_news";
//        String data = new Gson().toJson(Request);
//        json().get_hot_news(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_configuration(GetConfigRequest Request, Callback<GetConfigResponse> callback) {
//        String api_request = "get_configuration";
//        String data = new Gson().toJson(Request);
//        json().get_configuration(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_price_service(GetPriceServiceRequest Request, Callback<GetPriceServiceResponse> callback) {
//        String api_request = "get_services_and_pricing";
//        String data = new Gson().toJson(Request);
//        json().get_price_servce(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_home_slider(SliderImgRequest Request, Callback<SliderImgResponse> callback) {
//        String api_request = "get_home_slider";
//        String data = new Gson().toJson(Request);
//        json().get_home_slider(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void set_settings(SetSettingRequest Request, Callback<SetSettingResponse> callback) {
//        String api_request = "set_settings";
//        String data = new Gson().toJson(Request);
//        json().set_settings(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_settings(GetSettingRequest Request, Callback<GetSettingResponse> callback) {
//        String api_request = "get_settings";
//        String data = new Gson().toJson(Request);
//        json().get_settings(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void add_user_service(AddUserServiceRequest Request, Callback<AddUserServiceResponse> callback) {
//        String api_request = "add_user_services";
//        String data = new Gson().toJson(Request);
//        json().add_user_service(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_my_notifications(GetNotificationRequest Request, Callback<GetNotificationResponse> callback) {
//        String api_request = "get_my_notifications";
//        String data = new Gson().toJson(Request);
//        json().get_my_notifications(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void set_call_status(CallStatusRequest Request, Callback<CallStatusResponse> callback) {
//        String api_request = "set_open_call_push_status";
//        String data = new Gson().toJson(Request);
//        json().set_call_status(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void call_transaction(TransactionDetailsRequest Request, Callback<TransactionDetailsResponse> callback) {
//        String api_request = "add_payment";
//        String data = new Gson().toJson(Request);
//        json().call_transaction(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//
//    /*call_transaction*/
//
//    public void get_track_sheet_cat_data(GetTrackSheetRequest Request, Callback<GetTrackSheetResponse> callback) {
//        String api_request = "get_track_sheet_cat_data";
//        String data = new Gson().toJson(Request);
//        json().get_track_sheet_cat_data(api_id, api_secret, api_request, data).enqueue(callback);
//    }
//
//    public void get_my_kyc(GetMyKycRequest Request, Callback<GetMyKycResponse> callback) {
//        String api_request = "get_my_kyc";
//        String data = new Gson().toJson(Request);
//        json().get_my_kyc(api_id, api_secret, api_request, data).enqueue(callback);
//    }
    /*add_user_service*/


//    public void signup_user(SignUpRequest Request, Callback<SignUpResponce> callback) {
//        String api_request = "signup_user";
//        String data = new Gson().toJson(Request);
//        json().signup_user(api_id, api_secret, api_request, data).enqueue(callback);
//    }

//    public void signup_user(SignUpRequest request, MultipartBody.Part file, Callback<SignUpResponce> callback) {
//        String api_request = "signup_user";
//        String data = new Gson().toJson(request);
//        //json().add_contact(api_id, api_secret, api_request, data).enqueue(callback);
//        json().signup_user(
//                RequestBody.create(MediaType.parse("text/plain"), api_id),
//                RequestBody.create(MediaType.parse("text/plain"), api_secret),
//                RequestBody.create(MediaType.parse("text/plain"), api_request),
//                RequestBody.create(MediaType.parse("text/plain"), data)
//                , file).enqueue(callback);
//    }

//    public void update_user(UpdateProfileRequest request, MultipartBody.Part file, Callback<UpdateProfileResponce> callback) {
//        String api_request = "update_user";
//        String data = new Gson().toJson(request);
//        //json().add_contact(api_id, api_secret, api_request, data).enqueue(callback);
//        json().update_user(
//                RequestBody.create(MediaType.parse("text/plain"), api_id),
//                RequestBody.create(MediaType.parse("text/plain"), api_secret),
//                RequestBody.create(MediaType.parse("text/plain"), api_request),
//                RequestBody.create(MediaType.parse("text/plain"), data)
//                , file).enqueue(callback);
//    }


}
