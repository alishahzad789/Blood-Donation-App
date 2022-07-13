package com.example.bloodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInAcitivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String email="";
    private String password="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);

        mAuth = FirebaseAuth.getInstance();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        Button SignIn=findViewById(R.id.SignUpBtn);
        EditText EmailtextView=findViewById(R.id.EmailTextEdit);
        EditText passwordtextView=findViewById(R.id.PasswordTextEdit);
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"SignIn Clicked!", Toast.LENGTH_LONG).show(); //onStart Called
                email=EmailtextView.getText().toString();
                password=passwordtextView.getText().toString();
                Log.d("**email**", email);
                Log.d("**pass**", password);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInAcitivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("**Success**", "signInWithEmail:success");

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String UID=user.getUid();
                                    SignIntoComet(UID);

                                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);

                                   // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("**Failure**", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInAcitivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                   // updateUI(null);
                                }
                            }
                        });

            }
        });
    }
    void SignIntoComet(String _UID){
        String UID = _UID; // Replace with the UID of the user to login
        String authKey = "012036ce48f46824b9b05f810c85743ecac83b4b"; // Replace with your App Auth Key

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, authKey, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    Log.d("**cometlogin", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("**cometlogin", "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            Log.d("**cometlogin", "Comet User Logged IN: ");
        }
    }

}