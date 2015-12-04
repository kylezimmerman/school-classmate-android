package com.prog3210.classmate.comments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.prog3210.classmate.R;


/**
 * Created by scoombes-cc on 12/2/2015.
 */
public class CommentDialog extends android.support.v4.app.DialogFragment {

    EditText commentBodyText;
    public interface CommentDialogListener{
        public void onDialogSubmitClick(String commentBody);
    }

    CommentDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (CommentDialogListener) activity;
        }
        catch (ClassCastException e){
            //oh dear, oh me
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        String eventName = getArguments().getString("eventName");

        final View view = inflater.inflate(R.layout.comment_view, null);
        commentBodyText = (EditText) view.findViewById(R.id.comment_body);

        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this method has been overriden in onStart() put logic in there
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this method has been overriden in onStart() put logic in there
                    }
                }).setTitle("Discuss " + eventName);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog ad = (AlertDialog) getDialog();

        if (ad != null){
            Button positiveButton = ad.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = ad.getButton(Dialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (commentBodyText.getText().length() > 0){
                        listener.onDialogSubmitClick(commentBodyText.getText().toString());
                        dismiss();
                    }
                    else{
                        Toast.makeText(getActivity(), "Enter a comment first before trying to submit", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentBodyText.getText().length() > 0){
                        //ask user if they want to discard their changes
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setMessage("Discard your comment?")
                            .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismiss();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do Nothing here it will close the alertdialog on its own
                                }
                            });

                        AlertDialog alertDialog = builder.create();

                        alertDialog.show();
                    }
                    else{
                        dismiss();
                    }
                }
            });
        }
    }
}
