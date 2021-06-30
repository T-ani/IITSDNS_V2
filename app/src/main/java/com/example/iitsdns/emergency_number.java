package com.example.iitsdns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iitsdns.Database.DatabaseHelperClass;
import com.example.iitsdns.ModelClasses.PhoneNumberModelClass;

public class emergency_number extends AppCompatActivity {
    DatabaseHelperClass databaseHelperClass = null;
    PhoneNumberModelClass phoneNumberModelClass;
    EditText editing_number_1,editing_number_2;
    String editing_n_1,editing_n_2;
    String read_editing_n_1,read_editing_n_2;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_number);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //databaseHelperClass = new DatabaseHelperClass(emergency_number.this);

        editing_number_1=findViewById(R.id.editing_number_1);
        editing_number_2=findViewById(R.id.editing_number_2);
        button=findViewById(R.id.button_2);

        databaseHelperClass = new DatabaseHelperClass(this);
        if( databaseHelperClass.readPhoneNumberModelClass()!=null){



            read_editing_n_1=databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1();
            System.out.println("00000 "+databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber1());

            if(!read_editing_n_1.isEmpty()){

               // read_editing_n_1=read_editing_n_1.replaceAll("'","");

            }
            else
                read_editing_n_1="";

            read_editing_n_2=databaseHelperClass.readPhoneNumberModelClass().getPhoneNumber2();

            if(!read_editing_n_2.isEmpty())
            {
               // read_editing_n_2=read_editing_n_2.replaceAll("'","");

            }
            else
                read_editing_n_2="";

            System.out.println(read_editing_n_1+"-----------------");
            editing_number_1.setText(read_editing_n_1);
            editing_number_2.setText(read_editing_n_2);

            System.out.println("read_read"+read_editing_n_1+" "+read_editing_n_2+"read_read");
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (editing_number_1.getText().toString()!=null)
                {
                    editing_n_1=editing_number_1.getText().toString();

                }


                if(editing_number_2.getText().toString()!=null)
                {
                    editing_n_2=editing_number_2.getText().toString();
                }
                System.out.println("---89"+editing_n_1+editing_n_2);




                if(editing_n_1.isEmpty() &&  editing_n_2.isEmpty())
                {
                    Toast.makeText(emergency_number.this, "Please enter both number", Toast.LENGTH_SHORT).show();
                }else if(editing_n_1.isEmpty()){
                    Toast.makeText(emergency_number.this, "Your primary phone number is empty please enter it... ", Toast.LENGTH_SHORT).show();
                }else if(editing_n_2.isEmpty()){
                    Toast.makeText(emergency_number.this, "Your second phone number is empty please enter....", Toast.LENGTH_SHORT).show();
                }else {


                    editing_n_1="'"+editing_number_1.getText().toString()+"'";
                    editing_n_2="'"+editing_number_2.getText().toString()+"'";

                    phoneNumberModelClass = new PhoneNumberModelClass(editing_n_1, editing_n_2);

                    if (databaseHelperClass.addUpdatePhoneNumber(phoneNumberModelClass)) {
                        Toast.makeText(emergency_number.this, "Data saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(emergency_number.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });





    }
}