package com.example.lab1;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    Button btsave, btview;
    EditText name, phonenumber;
    TextView txtview;
    ListView listView;
    List<Contact> contacts;
    List<String> myarraylist;
    DatabaseManagement db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        db = new DatabaseManagement(this);

        btsave = findViewById(R.id.save);
        btview = findViewById(R.id.view);

        name = findViewById(R.id.name);
        phonenumber = findViewById(R.id.phonenumber);
        txtview = findViewById(R.id.textv);
        listView = findViewById(R.id.mobile_list);

        if (name != null) {
            name.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        if (btsave != null) {
            btsave.setOnClickListener(v -> {
                if (name != null && phonenumber != null &&
                        !name.getText().toString().trim().isEmpty() &&
                        !phonenumber.getText().toString().trim().isEmpty()) {

                    db.addContact(new Contact(name.getText().toString().trim(), phonenumber.getText().toString().trim()));
                    name.setText("");
                    phonenumber.setText("");
                    Toast.makeText(getApplicationContext(), "Insert successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill empty fields!", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (btview != null) {
            btview.setOnClickListener(v -> {
                contacts = db.getAllContacts();
                myarraylist = new ArrayList<>();

                for (Contact cn : contacts) {
                    myarraylist.add("Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        ContactActivity.this,
                        R.layout.list_item_text,
                        myarraylist);

                if (listView != null) {
                    listView.setAdapter(arrayAdapter);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
