package com.example.koenigmap;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import Model.Name;

public class Main2Activity extends AppCompatActivity {

    MaterialEditText editNewUser,editNewPassword,editNewEmail;
    MaterialEditText editUser,editPassword;

    Button buttonSignUp,buttonSingIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database =FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        editUser= (MaterialEditText) findViewById(R.id.editName);
        editPassword = (MaterialEditText) findViewById(R.id.editPassword);

        buttonSignUp = (Button) findViewById(R.id.button_sign_up);
        buttonSingIn =(Button) findViewById(R.id.button_sign_in);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingIn(editUser.getText().toString(),editPassword.getText().toString());
            }
        });
    }

    private void SingIn(final String user, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).exists()) {
                    if(!user.isEmpty()){
                        Name login = dataSnapshot.child(user).getValue(Name.class);
                        if(login.getPassword().equals(password))
                            Toast.makeText(Main2Activity.this, "Login OK", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Main2Activity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Main2Activity.this, "Please enter your user name ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(Main2Activity.this, "User is not exists!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Main2Activity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.signup_layout,null);

        editNewUser= (MaterialEditText) sign_up_layout.findViewById(R.id.editNewUserName);
        editNewEmail= (MaterialEditText) sign_up_layout.findViewById(R.id.editNewEmail);
        editNewPassword= (MaterialEditText) sign_up_layout.findViewById(R.id.editNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Name name = new Name(editNewUser.getText().toString(),
                        editNewPassword.getText().toString(),
                        editNewPassword.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(name.getUserName()).exists())
                            Toast.makeText(Main2Activity.this, "Пользователь уже зарегистрирован!", Toast.LENGTH_SHORT).show();
                        else {
                            users.child(name.getUserName())
                                    .setValue(users);
                            Toast.makeText(Main2Activity.this, "Пользоваель успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }

            });
        alertDialog.show();
        }

    }


