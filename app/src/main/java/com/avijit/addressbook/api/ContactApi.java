package com.avijit.addressbook.api;

import com.avijit.addressbook.common.ApiResponse;
import com.avijit.addressbook.dto.ContactPostDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactApi {

    @GET("contact/")
    Call<ApiResponse> getContacts();

    @POST("contact/save")
    Call<ApiResponse> save(@Body ContactPostDTO postDTO);

    @PUT("contact/update/{contactId}")
    Call<ApiResponse> update(@Body ContactPostDTO postDTO, @Path("contactId") Long contactId);
}
