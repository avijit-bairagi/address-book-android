package com.avijit.addressbook.api;

import com.avijit.addressbook.entity.Address;
import com.avijit.addressbook.dto.AddressPostDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @GET("address/")
    Call<List<Address>> getAddress();

    @POST("address/save")
    Call<Address> save(@Body AddressPostDTO postDTO);

    @PUT("address/update/{addressId}")
    Call<Address> update(@Body AddressPostDTO postDTO, @Path("addressId") Long addressId);
}
