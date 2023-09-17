package com.websarva.wings.android.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NoticeDialogFragment extends DialogFragment {
    public interface NoticeDialogListener{
        public void onDialogPositiveClick(DialogFragment dialogFragment);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (NoticeDialogListener) getActivity().getSupportFragmentManager().findFragmentById(android.R.id.content);
            Log.d("set", Boolean.toString(listener == null));
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            throw new ClassCastException(getActivity().toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.ask_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
}
