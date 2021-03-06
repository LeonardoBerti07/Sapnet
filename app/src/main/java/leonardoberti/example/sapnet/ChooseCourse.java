package leonardoberti.example.sapnet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChooseCourse extends AppCompatActivity {   //qui è dove andrà la lista dei corsi

    SearchView mySearchView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lv;
    private FirebaseAuth mAuth;
    private String Uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.freccia_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        Uid = mAuth.getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        mySearchView = (SearchView) findViewById(R.id.searchView);
        mySearchView.setFocusable(false);
        ArrayList<String> course = new ArrayList<String>();
        lv = (ListView) findViewById(R.id.myList);
        MyListAdapter adapter = new MyListAdapter(this, R.layout.list_item, course);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((new AdapterView.OnItemClickListener() {       //per far fare qualcosa quando clicca su un elemento della lista, prende le informazioni/vede del clock subito dopo il click, quindi se l’elemento da check diventa uncheck vedrà che è uncheck.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        db.collection("Course")                                            //metto nella lista in ordine alfabetico tutti i corsi
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        boolean flag = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                flag = true;
                                if (!course.contains(document.getId())) {
                                    course.add(document.getId());
                                    Log.i("GINOOOOOOOOOOOOOOOOOO", "diomaialeeeeeeeeeeeeee");
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if (!flag) {
                                course.clear();
                                adapter.notifyDataSetChanged();
                            }
                            //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                        } else {
                            //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                        }
                    }
                });
        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {              //aggiorno la lista man mano che l'user digita
                course.clear();
                if (newText.length() != 0) {
                    if (Character.isLowerCase(newText.charAt(0))) {
                        newText = Character.toUpperCase(newText.charAt(0)) + newText.substring(1);
                    }
                    Log.i("GINOOOOOOOOOOOOOOOOOO", newText);
                    db.collection("Course")
                            .whereGreaterThanOrEqualTo(FieldPath.documentId(), newText).whereLessThanOrEqualTo(FieldPath.documentId(), newText + '\uf8ff')
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    boolean flag = false;
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            flag = true;
                                            if (!course.contains(document.getId())) {
                                                course.add(document.getId());
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (!flag) {
                                            course.clear();
                                            adapter.notifyDataSetChanged();
                                        }
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    } else {
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    }
                                }
                            });
                }
                else {
                    db.collection("Course")                                            //metto nella lista in ordine alfabetico tutti i corsi
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    course.clear();
                                    boolean flag = false;
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            flag = true;
                                            if (!course.contains(document.getId())) {
                                                course.add(document.getId());
                                                Log.i("GINOOOOOOOOOOOOOOOOOO", "diomaialeeeeeeeeeeeeee");
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (!flag) {
                                            course.clear();
                                            adapter.notifyDataSetChanged();
                                        }
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    } else {
                                        //Log.i("è TUTTO UN FALLIMENTO", "PORCANNO CRISTO");
                                    }
                                }
                            });
                }
                return true;
            }
            public void callSearch(String query) {
            }
        });
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


    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                View finalConvertView = convertView;
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT ).show();
                        Button bottone = (Button) v;
                        TextView testo = (TextView) finalConvertView.findViewById(R.id.list_item_text);
                        ArrayList<String> array = new ArrayList<>();
                        array.add((String) testo.getText());
                        if (bottone.getText().equals("segui")) {               //premi il bottone ed è in versione Segui
                            db.collection("User").document(Uid)
                                    .update("Courses", FieldValue.arrayUnion(testo.getText()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //aggiornare il bottone
                                            bottone.setText("non seguire più");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        else {
                            db.collection("User").document(Uid)         //premi il bottone ed è in versione non seguire più
                                    .update("Courses", FieldValue.arrayRemove(testo.getText()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //aggiornare il bottone
                                            bottone.setText("segui");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewholder = (ViewHolder) convertView.getTag();
                mainViewholder.title.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder {
        Button button;
        TextView title;

    }
    //gfyiogui
}