package com.android.group.network;

import com.android.group.model.foursquare.FourSquareResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BusinessService {
    //categoryId=4bf58dd8d48988d181941735&
    @GET("v2/venues/search?near=NYC&client_id=SP2WL1QS355XNGMTHC02WIMLYWX05HITY2IIKCTGBBV034PR&client_secret=J13NZIPPJMBQMGHBZK4LC55WOPQRF1DMIXWB1T2AM2SF2WIH&v=20190518")
    Call<FourSquareResponse> getBusinesses(@Query("categoryId") String category);
}
