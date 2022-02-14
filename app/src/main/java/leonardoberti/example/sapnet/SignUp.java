package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText university;
    EditText username;
    Boolean flag;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        university = findViewById(R.id.University);
        username = findViewById(R.id.Username);
        mAuth = FirebaseAuth.getInstance();
    }

    public void SignUp (View view) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("L'email non è corretta");
            email.requestFocus();
            return;
        }
        if (password.getText().toString().length() < 6 )  {
            password.setError("La password deve avere almeno 6 caratteri");
            password.requestFocus();
            return;
        }
        AlreadyExist(username.getText().toString());
    }

    private void AlreadyExist(String usernome){       //checkiamo se già esiste un utente con quell'username
        DocumentReference docRef = db.collection("User").document(usernome);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username.setError("Questo username è già stato preso");
                        username.requestFocus();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            public void onComplete(@NonNull Task<AuthResult> task1) {
                                if (task1.isSuccessful()) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Email", email.getText().toString());
                                    user.put("Friends", new ArrayList<String>());
                                    user.put("Courses", new ArrayList<String>());
                                    user.put("Instagram", "");
                                    user.put("Name", "diocane");
                                    user.put("Password", password.getText().toString());
                                    user.put("PostCreated", new ArrayList<String>());
                                    user.put("PostVote", new ArrayList<>());
                                    user.put("Subject", new ArrayList<String>());
                                    user.put("Surname", "gesù ladrone");
                                    user.put("University", "La Sapienza");
                                    user.put("TimeCreation", new Timestamp(System.currentTimeMillis()));
                                    db.collection("User").document(username.getText().toString())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp.this, "errore nel creare l'account, riprova", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                } else {
                                    Toast.makeText(SignUp.this, "errore nel creare l'account, riprova", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Log.i("ERRORE", "non sono riuscito a fare la query");
                    return;
                }
            }
        });
    }
}