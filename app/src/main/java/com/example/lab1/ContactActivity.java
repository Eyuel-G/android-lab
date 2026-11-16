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

    Button btsave, btview, btupdate, btdelete;
    EditText name, phonenumber, identityNumber;
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
        btupdate = findViewById(R.id.update);
        btdelete = findViewById(R.id.delete);

        name = findViewById(R.id.name);
        phonenumber = findViewById(R.id.phonenumber);
        identityNumber = findViewById(R.id.identity_number);
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
                    if (identityNumber != null) identityNumber.setText("");
                    Toast.makeText(getApplicationContext(), "Insert successful", Toast.LENGTH_LONG).show();
                    if (btview != null) btview.performClick();
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

                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        if (contacts != null && position < contacts.size()) {
                            Contact selectedContact = contacts.get(position);
                            if (identityNumber != null) {
                                identityNumber.setText(String.valueOf(selectedContact.getID()));
                            }
                            if (name != null) {
                                name.setText(selectedContact.getName());
                            }
                            if (phonenumber != null) {
                                phonenumber.setText(selectedContact.getPhoneNumber());
                            }
                        }
                    });
                }
            });
        }

        if (btupdate != null) {
            btupdate.setOnClickListener(v -> {
                if (identityNumber != null && name != null && phonenumber != null &&
                        !identityNumber.getText().toString().trim().isEmpty() &&
                        !name.getText().toString().trim().isEmpty() &&
                        !phonenumber.getText().toString().trim().isEmpty()) {

                    String id = identityNumber.getText().toString().trim();
                    String newName = name.getText().toString().trim();
                    String newPhone = phonenumber.getText().toString().trim();

                    int rows = db.updatedata(id, newName, newPhone);
                    if (rows > 0) {
                        Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                        name.setText("");
                        phonenumber.setText("");
                        identityNumber.setText("");
                        if (btview != null) btview.performClick();
                    } else {
                        Toast.makeText(getApplicationContext(), "No contact found with ID: " + id, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill ID, Name, and Phone fields!", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (btdelete != null) {
            btdelete.setOnClickListener(v -> {
                if (identityNumber != null && name != null && phonenumber != null &&
                        !identityNumber.getText().toString().trim().isEmpty() &&
                        !name.getText().toString().trim().isEmpty() &&
                        !phonenumber.getText().toString().trim().isEmpty()) {

                    String id = identityNumber.getText().toString().trim();
                    String newName = name.getText().toString().trim();
                    String newPhone = phonenumber.getText().toString().trim();

                    int rows = db.deleteContact(id, newName, newPhone);
                    if (rows > 0) {
                        Toast.makeText(getApplicationContext(), "Delete Successful!", Toast.LENGTH_LONG).show();
                        name.setText("");
                        phonenumber.setText("");
                        identityNumber.setText("");
                        if (btview != null) btview.performClick();
                    } else {
                        Toast.makeText(getApplicationContext(), "No contact found with ID: " + id, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill ID, Name, and Phone fields!", Toast.LENGTH_LONG).show();
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
