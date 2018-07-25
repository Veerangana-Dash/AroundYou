package com.veeradash.justvish;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity  {
    private EditText nemail, npassword;
    private Button nsignin, nsignup;

    private FirebaseAuth nAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)

                {
                    Intent intent = new Intent(HomeActivity.this, MapsActivity1.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        nemail = (EditText) findViewById(R.id.email);
        npassword = (EditText) findViewById(R.id.password);
        nsignin = (Button) findViewById(R.id.signin);
        nsignup = (Button) findViewById(R.id.signup);

        nsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = nemail.getText().toString();
                final String password = npassword.getText().toString();
                nAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, " SIGN UP ERROR ", Toast.LENGTH_SHORT).show();
                        } else {
                            String user_id = nAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            current_user_id.setValue(true);
                        }
                    }
                });
            }
        });

        nsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = nemail.getText().toString();
                final String password = npassword.getText().toString();
                nAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, " SIGN IN ERROR ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
        @Override
        protected void onStart() {
           super.onStart();
            nAuth.addAuthStateListener(firebaseAuthListener);
        }

        @Override
        protected void onStop() {
            super.onStop();
            nAuth.removeAuthStateListener(firebaseAuthListener);
        }


}

