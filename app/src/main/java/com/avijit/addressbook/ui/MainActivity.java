package com.avijit.addressbook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.avijit.addressbook.R;
import com.avijit.addressbook.adapter.ContactRecyclerAdapter;
import com.avijit.addressbook.api.RetrofitClient;
import com.avijit.addressbook.common.ApiResponse;
import com.avijit.addressbook.common.Constants;
import com.avijit.addressbook.common.NetworkUtils;
import com.avijit.addressbook.entity.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;

    private TextView messageTextView;

    private ProgressDialog progressDialog;

    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = findViewById(R.id.message_view_id);
        recyclerView = findViewById(R.id.contact_recycler_view_id);

        pullToRefresh = findViewById(R.id.pullToRefresh);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data from server...");

        findViewById(R.id.add_new_contact_btn_id).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ContactActionActivity.class);
            startActivity(intent);
        });

        pullToRefresh.setOnRefreshListener(() -> {
            getContactsFromServer();
            pullToRefresh.setRefreshing(false);
        });

        getContactsFromServer();
    }

    private void getContactsFromServer() {

        if (NetworkUtils.isConnected(getApplicationContext())) {

            progressDialog.show();

            Call<ApiResponse> contactsCall = RetrofitClient.getInstance().getApi().getContacts();

            contactsCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                    if (response.isSuccessful()) {
                        ApiResponse apiResponse = response.body();

                        if (apiResponse.getCode().equalsIgnoreCase(Constants.API_SUCCESS_CODE)) {

                            List<Contact> contacts = getContactsData(apiResponse.getData());

                            if (contacts.isEmpty()) {
                                handleError("No contact found.");

                            } else {
                                ContactRecyclerAdapter contactRecyclerAdapter = new ContactRecyclerAdapter(contacts);
                                recyclerView.setAdapter(contactRecyclerAdapter);
                                contactRecyclerAdapter.notifyDataSetChanged();

                                recyclerView.setVisibility(View.VISIBLE);
                                messageTextView.setVisibility(View.GONE);

                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        } else
                            handleError(apiResponse.getMessage());

                    } else
                        handleError(response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    handleError(t.getMessage());
                }
            });
        } else {
            handleError("No internet connection!\nPlease pull down to refresh.");
        }
    }

    private void handleError(String message) {

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        Log.e(TAG, "errorMessage= " + message);

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
        recyclerView.setVisibility(View.GONE);
    }

    private List<Contact> getContactsData(Object data) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<Contact>>() {
        }.getType();
        List<Contact> contacts = gson.fromJson(gson.toJson(data), type);
        return contacts;
    }
}
