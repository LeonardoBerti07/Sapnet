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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseCourse extends AppCompatActivity {   //qui è dove andrà la lista dei corsi

    SearchView mySearchView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.freccia_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        mySearchView = (SearchView) findViewById(R.id.searchView);
        mySearchView.setFocusable(false);
        ListView lv = (ListView) findViewById(R.id.myList);
        generateListContent();
        lv.setAdapter( new MyListAdapter(this, R.layout.list_item, data));

        /*
        ArrayList<String> course = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, course);
        list.setAdapter(adapter);
        list.setOnItemClickListener((new AdapterView.OnItemClickListener() {       //per far fare qualcosa quando clicca su un elemento della lista, prende le informazioni/vede del clock subito dopo il click, quindi se l’elemento da check diventa uncheck vedrà che è uncheck.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));




        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {              //aggiorno la lista man mano che l'user digita
                if (newText.length() != 0) {
                    //Log.i("è TUTTO UN FALLIMENTO", newText);
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
                    course.clear();
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
            public void callSearch(String query) {
            }
        });

         */
    }

    private void generateListContent() {
        for (int i=0; i<55; i++) {
            data.add("this is row number");
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
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT ).show();
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
}