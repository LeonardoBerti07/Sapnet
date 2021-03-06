package leonardoberti.example.sapnet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogProf extends AppCompatDialogFragment {

    private EditText NomeProfessore;
    private DialogProfListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Inserisci il nome del prof che tiene il corso")
                .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String prof = NomeProfessore.getText().toString();
                        listener.applyText(prof);
                    }
                });
        NomeProfessore = view.findViewById(R.id.NomeProf);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogProfListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogProfListener");
        }
    }

    public interface DialogProfListener {
         void applyText(String prof);
    }
}
