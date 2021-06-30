package com.example.iitsdns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iitsdns.Database.DatabaseHelperClass;
import com.example.iitsdns.ModelClasses.SPO2HeartModelClass;

public class set_limit extends AppCompatActivity {

    Button button;
    EditText editing_1,editing_2;
    String edit_1,edit_2;
    int data_1,data_2;
    DatabaseHelperClass databaseHelperClass;
    DatabaseHelperClass databaseHelperClass1;
    SPO2HeartModelClass spo2HeartModelClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_limit);

        button=findViewById(R.id.button);
        editing_1=findViewById(R.id.editing_1);
        editing_2=findViewById(R.id.editing_2);
        databaseHelperClass = new DatabaseHelperClass(set_limit.this);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_1=editing_1.getText().toString();
                edit_2=editing_2.getText().toString();

                if(edit_1.isEmpty() || edit_2.isEmpty())
                {
                    Toast.makeText(set_limit.this, "PLease set limit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    data_1=Integer.parseInt(edit_1);
                    data_2=Integer.parseInt(edit_2);
                    databaseHelperClass=new DatabaseHelperClass(set_limit.this);

                    spo2HeartModelClass=new SPO2HeartModelClass(data_2,data_1);

                    if(databaseHelperClass.addUpdateTaskForTwoValue(spo2HeartModelClass))
                    {
                        Toast.makeText(set_limit.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(set_limit.this, "Failed", Toast.LENGTH_SHORT).show();
                    }

                }




            }
        });

        if(databaseHelperClass.readSPO2AndHeartRate().getSPO2()==0){

            editing_1.setText("");
        }
        else
        {
            editing_2.setText(String.valueOf(databaseHelperClass.readSPO2AndHeartRate().getSPO2()));
        }
         if(databaseHelperClass.readSPO2AndHeartRate().getHeartBeat()==0)
        {
            editing_2.setText("");
        }
         else
         {
             editing_1.setText(String.valueOf(databaseHelperClass.readSPO2AndHeartRate().getHeartBeat()));

         }

        //Toast.makeText(this, databaseHelperClass1.readSPO2AndHeartRate().getSPO2(), Toast.LENGTH_SHORT).show();



    }
}