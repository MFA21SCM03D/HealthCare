package com.example.rohanspc.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Emergency extends AppCompatActivity {
    private EditText enterlocation;
    private Button ambulance,contact_d, bloodbank,medicines;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        ambulance = (Button)findViewById(R.id.ambulance);
        contact_d = (Button)findViewById(R.id.contact_doctor);
        bloodbank = (Button)findViewById(R.id.blood_bank);
        medicines = (Button)findViewById(R.id.buy_medicines);
        enterlocation = (EditText)findViewById(R.id.enter_location);

        medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = (new Intent(Emergency.this,Buy_medicines.class));
                startActivity(intent);
            }
        });

    }
}
