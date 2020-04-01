package com.avijit.addressbook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avijit.addressbook.R;
import com.avijit.addressbook.api.RetrofitClient;
import com.avijit.addressbook.common.ApiResponse;
import com.avijit.addressbook.common.Constants;
import com.avijit.addressbook.common.NetworkUtils;
import com.avijit.addressbook.dto.ContactPostDTO;
import com.avijit.addressbook.entity.Contact;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActionActivity extends AppCompatActivity {

    private static final String TAG = ContactActionActivity.class.getSimpleName();

    private Button saveOrUpdateBtn;

    EditText nameEditText;
    EditText addressEditText;
    EditText phoneNumberEditText;

    private Long contactId = null;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_action);

        saveOrUpdateBtn = findViewById(R.id.save_or_update_btn_id);
        nameEditText = findViewById(R.id.name_edit_text_id);
        addressEditText = findViewById(R.id.address_edit_text_id);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text_id);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Submitting data to server...");

        updateUI();

        saveOrUpdateBtn.setOnClickListener(v -> {
            saveOrUpdate();
        });
    }

    private void updateUI() {
        Gson gson = new Gson();

        String contactAsString = getIntent().getStringExtra(Constants.CONTACT_DATA);

        if (contactAsString != null) {

            Contact contact = gson.fromJson(contactAsString, Contact.class);

            contactId = contact.getId();

            nameEditText.setText(contact.getName());
            addressEditText.setText(contact.getAddress());
            phoneNumberEditText.setText(contact.getPhoneNumber());

            saveOrUpdateBtn.setText("Update Contact");
        }
    }

    private void saveOrUpdate() {

        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();

        ContactPostDTO postDTO = new ContactPostDTO(name, address, phoneNumber);

        if (!isVaild(postDTO))
            return;

        if (NetworkUtils.isConnected(getApplicationContext())) {

            progressDialog.show();

            if (contactId != null)
                update(postDTO);
            else
                save(postDTO);
        } else {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isVaild(ContactPostDTO postDTO) {

        if (postDTO.getName().isEmpty() || postDTO.getName().length() < 5) {
            nameEditText.setError("Name must not be less than 5 char.");
            return false;
        }
        if (postDTO.getAddress().isEmpty() || postDTO.getAddress().length() < 5) {
            addressEditText.setError("Address must not be less than 5 char.");
            return false;
        }
        if (postDTO.getPhoneNumber().isEmpty() || postDTO.getPhoneNumber().length() < 5) {
            phoneNumberEditText.setError("Phone number must not be less than 5 char.");
            return false;
        }
        return true;
    }

    private void save(ContactPostDTO postDTO) {

        Call<ApiResponse> saveCall = RetrofitClient.getInstance().getApi().save(postDTO);

        saveCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().equalsIgnoreCase(Constants.API_SUCCESS_CODE)) {
                        Toast.makeText(ContactActionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        goToHomeActivity();
                    } else
                        handleError(response.body().getMessage());

                } else
                    handleError(response.message());

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                handleError(t.getMessage());
            }
        });
    }

    private void update(ContactPostDTO postDTO) {

        Call<ApiResponse> updateCall = RetrofitClient.getInstance().getApi().update(postDTO, contactId);

        updateCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCode().equalsIgnoreCase(Constants.API_SUCCESS_CODE)) {
                        Toast.makeText(ContactActionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        goToHomeActivity();

                    } else
                        handleError(response.body().getMessage());
                } else
                    handleError(response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                handleError(t.getMessage());
            }
        });
    }

    private void handleError(String message) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        Log.e(TAG, "errorMessage= " + message);
        Toast.makeText(ContactActionActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void goToHomeActivity() {

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
