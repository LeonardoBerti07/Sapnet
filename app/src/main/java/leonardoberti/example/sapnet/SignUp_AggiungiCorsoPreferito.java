package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SignUp_AggiungiCorsoPreferito extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SearchView searchView;
    private boolean activityStartup = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_aggiungi_corso_preferito);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.freccia_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void Add(View view) {
        Intent intent = new Intent(getApplicationContext(), ChooseCourse.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    protected void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.salta:
                //Intent intent = new Intent(getApplicationContext(), Login.class);
                //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.salta, menu);
        return true;
    }
}