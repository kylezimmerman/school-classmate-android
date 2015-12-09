/*
    CommentDialog.java

    custom dialog that allows a user to enter a comment

    Sean Coombes, Kyle Zimmerman, Justin Coschi
 */
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

import com.prog3210.classmate.LogHelper;
import com.prog3210.classmate.R;

public class CommentDialog extends android.support.v4.app.DialogFragment {

    /***
     * Listener interface that will be called on the activity that impletements the interface
     */
    EditText commentBodyText;
    public interface CommentDialogListener{
        void onDialogSubmitClick(String commentBody);
    }

    CommentDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //attaches the listener to the activity that called the dialog
        try {
            listener = (CommentDialogListener) activity;
        }
        catch (ClassCastException e){
            LogHelper.logError(getContext(), "CommentDialog", "Error loading dialog, comments won't be saved", e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //sets up dialog using view created for commetns
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String eventName = getArguments().getString("eventName");

        final View view = inflater.inflate(R.layout.comment_view, null);
        commentBodyText = (EditText) view.findViewById(R.id.comment_body);

        //sets controls that will be displayed on the dialog
        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this method has been overridden in onStart() put logic in there
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this method has been overridden in onStart() put logic in there
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

            //over rides the normal buttons behavior from oncreate dialog method
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (commentBodyText.getText().length() > 0){
                        //sends comment back to host activity
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
                                    //Do nothing here it will close the inner Alertdialog on its own
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
