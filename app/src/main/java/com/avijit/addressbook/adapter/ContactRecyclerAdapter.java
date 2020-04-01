package com.avijit.addressbook.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avijit.addressbook.R;
import com.avijit.addressbook.common.Constants;
import com.avijit.addressbook.entity.Contact;
import com.avijit.addressbook.ui.ContactActionActivity;
import com.google.gson.Gson;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactRecyclerViewHolder> {

    private List<Contact> contacts;
    private View view;
    private ContactRecyclerViewHolder viewHolder;

    public ContactRecyclerAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.item_contact, viewGroup, false);
        viewHolder = new ContactRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactRecyclerViewHolder viewHolder, int i) {

        final Contact contact = contacts.get(i);
        viewHolder.nameTextView.setText(contact.getName());
        viewHolder.addressTextView.setText(contact.getAddress());
        viewHolder.phoneNumberTextView.setText(contact.getPhoneNumber());

        viewHolder.editButton.setOnClickListener(v -> {

            Gson gson = new Gson();
            String contactAsString = gson.toJson(contact);

            Intent intent = new Intent(v.getContext(), ContactActionActivity.class);
            intent.putExtra(Constants.CONTACT_DATA, contactAsString);
            v.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactRecyclerViewHolder extends RecyclerView.ViewHolder {

        protected TextView nameTextView;
        protected TextView addressTextView;
        protected TextView phoneNumberTextView;
        protected Button editButton;

        public ContactRecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view_id);
            addressTextView = itemView.findViewById(R.id.address_text_view_id);
            phoneNumberTextView = itemView.findViewById(R.id.phone_number_text_view_id);
            editButton = itemView.findViewById(R.id.edit_contact_btn_id);

        }
    }
}