package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    ImageButton show_or_hide_psw;
    private String EmailObtained = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(getApplicationContext(), SignUp_AggiungiCorsoPreferito.class);
        startActivity(intent);
        if (mAuth.getCurrentUser() != null) { // So if it's not equal to null, that means that we have a signed user, which then means we want to log in.
            Log.i(mAuth.getCurrentUser().getEmail(), "we skip the authentication because we are already logged in");
            AlreadylogIn();
        }
    }

    public void AlreadylogIn() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    public void signUp(View view) {

        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void goClicked(View view) {                          //check if we can log in the user and if we cant we sign up the user

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {     //se in input non abbiamo un email proviamo ad accedere con lo username
            db.collection("User")
                    .whereEqualTo("Username", email.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean flag = false;
                        if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            flag = true;
                            EmailObtained = document.get("Email").toString();
                            mAuth.signInWithEmailAndPassword(EmailObtained, password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        AlreadylogIn();
                                    } else {
                                        Toast.makeText(Login.this, "errore, riprova", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        if (!flag) {
                            email.setError("Lo username inserito non è corretto");
                            email.requestFocus();
                        }

                    } else {
                        Toast.makeText(Login.this, "errore, riprova", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {       //se abbiamo un email in input allora proviamo ad accedere con l'eamil
            db.collection("User")
                    .whereEqualTo("Email", email.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean flag = false;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    flag = true;
                                    mAuth.signInWithEmailAndPassword((String) document.get("Email"), password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                AlreadylogIn();
                                            } else {
                                                email.setError("Lo username non è corretto");
                                                email.requestFocus();
                                            }
                                        }
                                    });
                                }
                                if (!flag) {
                                    email.setError("Lo username non è corretto");
                                    email.requestFocus();
                                }
                            } else {
                                Toast.makeText(Login.this, "errore, riprova", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void showPsw(View view){
        password = (EditText) findViewById(R.id.password); //prendo l'edit text della password.
        show_or_hide_psw = (ImageButton) findViewById(R.id.button_show_psw); //prendo l'imagebutton per cambiare l'occhietto.
        if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) { //se è nascosto
            //allora mostro la psw
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //setto l'icona in occhietto_hide_pass
            show_or_hide_psw.setImageResource(R.drawable.occhietto_hide_pass);
            password.setSelection(password.getText().length()); //questo per far rimanere il "puntatore" del testo alla fine
        }
        else { //se invece la psw è già visibile
            //la metto invisibile
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //setto l'icona in occhietto_show_pass
            show_or_hide_psw.setImageResource(R.drawable.occhietto_show_pass);
            password.setSelection(password.getText().length()); //questo per far rimanere il "puntatore" del testo alla fine
        }
    }
    
}