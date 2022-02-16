package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class ChooseCourse2 extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course2);
        EditText field = (EditText) findViewById(R.id.nome);         //barra di ricerca
        ListView list = findViewById(R.id.list);                      //listview dei corsi
        ArrayList<String> course = new ArrayList<String>();           //array dei corsi
        ArrayAdapter<String> adattatore = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, course);
        list.setAdapter(adattatore);
        list.setOnItemClickListener((new AdapterView.OnItemClickListener() {       //per far fare qualcosa quando clicca su un elemento della lista, prende le informazioni/vede del clock subito dopo il click, quindi se l’elemento da check diventa uncheck vedrà che è uncheck.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    Log.i("è TUTTO UN FALLIMENTO", s.toString());
                    db.collection("Course")
                            .whereGreaterThanOrEqualTo(FieldPath.documentId(), s.toString()).whereLessThanOrEqualTo(FieldPath.documentId(), s.toString() + '\uf8ff')
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (!course.contains(document.getId())) {
                                                course.add(document.getId());
                                                adattatore.notifyDataSetChanged();
                                            }
                                        }
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    } else {
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    }
                                }
                            });
                }
                else {
                    course.clear();
                    adattatore.notifyDataSetChanged();
                }
            }
            @Override

            public void afterTextChanged(Editable s) {}
        });
    }
}