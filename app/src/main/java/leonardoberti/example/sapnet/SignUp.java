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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText password2;
    EditText university;
    EditText username;
    EditText nome;
    EditText cognome;
    ImageButton show_or_hide_psw;
    ImageButton show_or_hide_psw2;
    Boolean flag;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

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

    public void showPsw2(View view){
        password2 = (EditText) findViewById(R.id.password2); //prendo l'edit text della password.
        show_or_hide_psw2 = (ImageButton) findViewById(R.id.button_show_psw2); //prendo l'imagebutton per cambiare l'occhietto.
        if (password2.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) { //se è nascosto
            //allora mostro la psw
            password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            //setto l'icona in occhietto_hide_pass
            show_or_hide_psw2.setImageResource(R.drawable.occhietto_hide_pass);
            password2.setSelection(password2.getText().length()); //questo per far rimanere il "puntatore" del testo alla fine
        }
        else { //se invece la psw è già visibile
            //la metto invisibile
            password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            //setto l'icona in occhietto_show_pass
            show_or_hide_psw2.setImageResource(R.drawable.occhietto_show_pass);
            password2.setSelection(password2.getText().length()); //questo per far rimanere il "puntatore" del testo alla fine
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        nome = findViewById(R.id.nome);
        cognome = findViewById(R.id.cognome);
        username = findViewById(R.id.username);
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
        if (!password.getText().toString().equals(password2.getText().toString())) {
            password2.setError("Le due password sono diverse");
            password2.requestFocus();
            return;
        }
        if (username.getText().toString().length() < 6) {
            username.setError("Lo username deve avere almeno 6 caratteri");
            username.requestFocus();
            return;
        }
        if (username.getText().toString().length() > 16) {
            username.setError("Lo username può avere al massimo 16 caratteri");
            username.requestFocus();
            return;
        }
        if (!username.getText().toString().matches("^[a-zA-Z0-9._-]*$")) {
            username.setError("Lo username può contenere solo numeri e/o lettere");
            username.requestFocus();
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
                        username.setError("Questo username è già utilizzato");
                        username.requestFocus();
                    } else {
                        //controllo se l'email è già stata presa
                        String emailUnder = email.getText().toString().toLowerCase(Locale.ROOT);
                        db.collection("User")
                                .whereEqualTo("Email", emailUnder)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            flag = true;
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                flag = false;
                                                Log.i("ESISTE GIà", "sisi");
                                            }
                                            if (flag) {
                                                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                    public void onComplete(@NonNull Task<AuthResult> task1) {
                                                        if (task1.isSuccessful()) {
                                                            Map<String, Object> user = new HashMap<>();
                                                            user.put("Email", email.getText().toString());
                                                            user.put("Friends", new ArrayList<String>());
                                                            user.put("Courses", new ArrayList<String>());
                                                            user.put("Instagram", "");
                                                            user.put("Name", nome.getText().toString());
                                                            user.put("PostCreated", new ArrayList<String>());
                                                            user.put("PostVote", new ArrayList<>());
                                                            user.put("Subject", new ArrayList<String>());
                                                            user.put("Surname", cognome.getText().toString());
                                                            user.put("University", "La Sapienza");
                                                            user.put("TimeCreation", new Timestamp(System.currentTimeMillis()));
                                                            user.put("Username", username.getText().toString());
                                                            db.collection("User").document(mAuth.getUid())
                                                                    .set(user)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Map<String, Object> Password = new HashMap<>();
                                                                            Password.put("Value", password.getText().toString());
                                                                            db.collection("User").document(mAuth.getUid()).collection("Password").document()
                                                                                    .set(Password)
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
                                            else {
                                                username.setError("Questa email è già utilizzata");
                                                username.requestFocus();
                                            }
                                        } else {
                                            Log.i("ERRORE", "non sono riuscito a fare la query");
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