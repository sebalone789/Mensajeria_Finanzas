package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import finanzas.p.e.mensajeriaepfinanzas.R;

/**
 * Created by usuario on 12/06/2017.
 */

public class DialogoConfirmacion extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.mensajeConfirmacion).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Aceptar
            }
        });
        return builder.create();
    }
}
