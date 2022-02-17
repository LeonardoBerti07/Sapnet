package leonardoberti.example.sapnet;


import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePost extends AppCompatActivity implements DialogProf.DialogProfListener {

    CheckBox Recensione;
    EditText Prof;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Recensione = findViewById(R.id.Recensione);
        Prof = (EditText) findViewById(R.id.ProfView);
    }

    public void RecensioneClick(View view) {
        if (Recensione.isChecked()) {
            OpenDialog();
        }
    }

    public void OpenDialog() {
        DialogProf dialogProf = new DialogProf();
        dialogProf.show(getSupportFragmentManager(), "dialog prof");
    }

    @Override
    public void applyText(String prof) {
        Prof.setText(prof);
    }
}