package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    ImageButton show_or_hide_psw;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) { // So if it's not equal to null, that means that we have a signed user, which then means we want to log in.
            Log.i(mAuth.getCurrentUser().getEmail(), "we skip the authentication because we are already logged in");
            logIn();
        }
    }

    protected void logIn() {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    public void goClicked(View view) {                          //check if we can log in the user and if we cant we sign up the user
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    logIn();
                } else {
                    // If sign in fails, display a message to the user and sign up the user
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task1) {
                            if (task1.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                logIn();
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(Login.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    
}