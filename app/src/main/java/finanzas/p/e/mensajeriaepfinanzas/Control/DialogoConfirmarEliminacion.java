package finanzas.p.e.mensajeriaepfinanzas.Control;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import finanzas.p.e.mensajeriaepfinanzas.R;

/**
 * Created by usuario on 14/06/2017.
 */

public class DialogoConfirmarEliminacion extends DialogFragment{
    public interface DialogoConfirmarEliminacionListener{
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    DialogoConfirmarEliminacionListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener=(DialogoConfirmarEliminacionListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement DialogoConfirmarEliminacionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.mensajeConfirmacionEliminacion).setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Aceptar
                mListener.onDialogPositiveClick(DialogoConfirmarEliminacion.this);
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cancelar
                mListener.onDialogNegativeClick(DialogoConfirmarEliminacion.this);
            }
        });
        return builder.create();
    }
}
