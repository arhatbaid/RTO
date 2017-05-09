package rto.example.com.rto.webhelper;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rto.example.com.rto.frameworks.addtawvehicle.AddTawVehicleResponse;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleRequest;
import rto.example.com.rto.frameworks.addvehicle.AddVehicleResponse;
import rto.example.com.rto.frameworks.city.GetCityResponse;
import rto.example.com.rto.frameworks.deletevehicle.DeleteVehicleResponse;
import rto.example.com.rto.frameworks.dispatchtawvehicle.DispatchTawVehicleResponse;
import rto.example.com.rto.frameworks.editvehicle.EditVehicleResponse;
import rto.example.com.rto.frameworks.getnearestpolicestations.GetNearPoliceStationResponse;
import rto.example.com.rto.frameworks.getdispatchedtawvehicles.GetDispatchedTawVehicleResponse;
import rto.example.com.rto.frameworks.getofficertawvehicle.GetOfficerTawVehicleResponse;
import rto.example.com.rto.frameworks.getvehicle.GetVehicleResponse;
import rto.example.com.rto.frameworks.searchtawvehicle.SearchTawVehicleResponse;
import rto.example.com.rto.frameworks.signin.SigninResponse;
import rto.example.com.rto.frameworks.signup.SignupRespose;
import rto.example.com.rto.frameworks.state.GetStateResponse;

/**
 * Created by James P. Zimmerman II on 3/4/16.
 */
public interface WebAPIInterface {


    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<SigninResponse> login_user(@Field("api_id") String api_id,
                                    @Field("api_secret") String api_secret,
                                    @Field("api_request") String api_request,
                                    @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<SignupRespose> register_user(@Field("api_id") String api_id,
                                      @Field("api_secret") String api_secret,
                                      @Field("api_request") String api_request,
                                      @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetStateResponse> get_state(@Field("api_id") String api_id,
                                     @Field("api_secret") String api_secret,
                                     @Field("api_request") String api_request);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetCityResponse> get_city(@Field("api_id") String api_id,
                                   @Field("api_secret") String api_secret,
                                   @Field("api_request") String api_request,
                                   @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetVehicleResponse> get_user_vehicle(@Field("api_id") String api_id,
                                              @Field("api_secret") String api_secret,
                                              @Field("api_request") String api_request,
                                              @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<AddVehicleResponse> add_vehicle(@Field("api_id") String api_id,
                                         @Field("api_secret") String api_secret,
                                         @Field("api_request") String api_request,
                                         @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<AddTawVehicleResponse> add_taw_vehicle(@Field("api_id") String api_id,
                                                @Field("api_secret") String api_secret,
                                                @Field("api_request") String api_request,
                                                @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<EditVehicleResponse> edit_vehicle(@Field("api_id") String api_id,
                                           @Field("api_secret") String api_secret,
                                           @Field("api_request") String api_request,
                                           @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<DeleteVehicleResponse> delete_vehicle(@Field("api_id") String api_id,
                                               @Field("api_secret") String api_secret,
                                               @Field("api_request") String api_request,
                                               @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<SearchTawVehicleResponse> search_taw_vehicle(@Field("api_id") String api_id,
                                                      @Field("api_secret") String api_secret,
                                                      @Field("api_request") String api_request,
                                                      @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetNearPoliceStationResponse> search_nearest_police_station(@Field("api_id") String api_id,
                                                                     @Field("api_secret") String api_secret,
                                                                     @Field("api_request") String api_request,
                                                                     @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetDispatchedTawVehicleResponse> get_dispatched_vehicle(@Field("api_id") String api_id,
                                                                 @Field("api_secret") String api_secret,
                                                                 @Field("api_request") String api_request,
                                                                 @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<GetOfficerTawVehicleResponse> get_officer_taw_vehicle(@Field("api_id") String api_id,
                                                               @Field("api_secret") String api_secret,
                                                               @Field("api_request") String api_request,
                                                               @Field("data") String data);

    @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<DispatchTawVehicleResponse> dispatch_taw_vehicle(@Field("api_id") String api_id,
                                                          @Field("api_secret") String api_secret,
                                                          @Field("api_request") String api_request,
                                                          @Field("data") String data);


//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<UpdateProfileResponse> update_profile(@Field("api_id") String api_id,
//                                               @Field("api_secret") String api_secret,
//                                               @Field("api_request") String api_request,
//                                               @Field("data") String data);


//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<ValidateOtpResponse> validate_otp(@Field("api_id") String api_id,
//                                           @Field("api_secret") String api_secret,
//                                           @Field("api_request") String api_request,
//                                           @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<ResendOtpResponse> resend_otp(@Field("api_id") String api_id,
//                                       @Field("api_secret") String api_secret,
//                                       @Field("api_request") String api_request,
//                                       @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<RegisterForPushResponse> register_for_push(@Field("api_id") String api_id,
//                                                    @Field("api_secret") String api_secret,
//                                                    @Field("api_request") String api_request,
//                                                    @Field("data") String data);
//
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetAccountBalanceResponse> my_account_balance(@Field("api_id") String api_id,
//                                                       @Field("api_secret") String api_secret,
//                                                       @Field("api_request") String api_request,
//                                                       @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetOfferesResponse> get_offers(@Field("api_id") String api_id,
//                                        @Field("api_secret") String api_secret,
//                                        @Field("api_request") String api_request,
//                                        @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetHotNewsResponse> get_hot_news(@Field("api_id") String api_id,
//                                          @Field("api_secret") String api_secret,
//                                          @Field("api_request") String api_request,
//                                          @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetConfigResponse> get_configuration(@Field("api_id") String api_id,
//                                              @Field("api_secret") String api_secret,
//                                              @Field("api_request") String api_request,
//                                              @Field("data") String data);

//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetPriceServiceResponse> get_price_servce(@Field("api_id") String api_id,
//                                                   @Field("api_secret") String api_secret,
//                                                   @Field("api_request") String api_request,
//                                                   @Field("data") String data);
//
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<SliderImgResponse> get_home_slider(@Field("api_id") String api_id,
//                                            @Field("api_secret") String api_secret,
//                                            @Field("api_request") String api_request,
//                                            @Field("data") String data);
//
//    @FormUrlEncoded
//
//    @POST(Endpoint.POST_URL)
//    Call<SetSettingResponse> set_settings(@Field("api_id") String api_id,
//                                          @Field("api_secret") String api_secret,
//                                          @Field("api_request") String api_request,
//                                          @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetSettingResponse> get_settings(@Field("api_id") String api_id,
//                                          @Field("api_secret") String api_secret,
//                                          @Field("api_request") String api_request,
//                                          @Field("data") String data);
//
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<AddUserServiceResponse> add_user_service(@Field("api_id") String api_id,
//                                                  @Field("api_secret") String api_secret,
//                                                  @Field("api_request") String api_request,
//                                                  @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetNotificationResponse> get_my_notifications(@Field("api_id") String api_id,
//                                                       @Field("api_secret") String api_secret,
//                                                       @Field("api_request") String api_request,
//                                                       @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<CallStatusResponse> set_call_status(@Field("api_id") String api_id,
//                                             @Field("api_secret") String api_secret,
//                                             @Field("api_request") String api_request,
//                                             @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetTrackSheetResponse> get_track_sheet_cat_data(@Field("api_id") String api_id,
//                                                         @Field("api_secret") String api_secret,
//                                                         @Field("api_request") String api_request,
//                                                         @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<TransactionDetailsResponse> call_transaction(@Field("api_id") String api_id,
//                                                      @Field("api_secret") String api_secret,
//                                                      @Field("api_request") String api_request,
//                                                      @Field("data") String data);
//
//    @FormUrlEncoded
//    @POST(Endpoint.POST_URL)
//    Call<GetMyKycResponse> get_my_kyc(@Field("api_id") String api_id,
//                                      @Field("api_secret") String api_secret,
//                                      @Field("api_request") String api_request,
//                                      @Field("data") String data);

    /*call_transaction*/
  /*  @FormUrlEncoded
    @POST(Endpoint.POST_URL)
    Call<LoginResponce> login_user(@Field("api_id") String api_id,
                                   @Field("api_secret") String api_secret,
                                   @Field("api_request") String api_request,
*/
   /* @Multipart
    @POST(Endpoint.POST_URL)
    Call<SignUpResponce> signup_user(@Part("api_id") RequestBody api_id,
                                     @Part("api_secret") RequestBody api_secret,
                                     @Part("api_request") RequestBody api_request,
                                     @Part("data") RequestBody data,
                                     @Part MultipartBody.Part profile_image);
*/

    class Endpoint {
        public static final String POST_URL = "RTO/api/api.php";
    }
}
