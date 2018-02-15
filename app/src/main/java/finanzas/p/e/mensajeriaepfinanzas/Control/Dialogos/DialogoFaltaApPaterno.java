package finanzas.p.e.mensajeriaepfinanzas.Control.Dialogos;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import finanzas.p.e.mensajeriaepfinanzas.R;

public class DialogoFaltaApPaterno extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("Falta el Apellido Paterno").setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Aceptar
            }
        });
        return builder.create();
    }
}
