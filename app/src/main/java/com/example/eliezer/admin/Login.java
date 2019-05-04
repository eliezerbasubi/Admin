package com.example.eliezer.admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView reset;
    Button btnLogin;
    FirebaseAuth mAuth;
    TextInputLayout edtName,edtPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=(Button)findViewById(R.id.loginBtn);
        reset=(TextView)findViewById(R.id.reset_password);

        edtName = findViewById(R.id.admin_email);
        edtPassword = findViewById(R.id.admin_password);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

       reset.setOnClickListener(onClickListener);
       btnLogin.setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view.equals(reset)){
                startActivity(new Intent(getApplicationContext(),Reset_Password.class));
                return;
            }

            if(view.equals(btnLogin)){
                String email = edtName.getEditText().getText().toString();
                String password = edtPassword.getEditText().getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    /*mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });*/
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(Login.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
}
