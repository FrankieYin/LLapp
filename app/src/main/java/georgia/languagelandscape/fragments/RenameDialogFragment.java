/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.util.RecordingAdaptor;

/**
 * A more complex {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RenameDialogListener} interface to handle interaction events.
 */
public class RenameDialogFragment extends DialogFragment {

    private Context context;

    public RenameDialogFragment() {
    }

    public interface RenameDialogListener {
        public void onRenameClick(Recording recording, String toName, int adaptorPosition);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        // force to close the soft keyboard whenever the dialog is dismissed
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Recording recording = getArguments().getParcelable(Recording.PARCEL_KEY);
        final int adaptorPosition = getArguments().getInt(RecordingAdaptor.ADAPTOR_POSITION);

        // need the layout to set the custom view for the dialog
        final LinearLayout dialogLayout = (LinearLayout) LayoutInflater
                .from(context)
                .inflate(R.layout.dialog_rename_recording, null);
        final EditText renameField = (EditText)
                dialogLayout.findViewById(R.id.dialog_rename_edit);

        renameField.setText(recording.getTitle());

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_rename_title)
                .setView(dialogLayout)
                .setPositiveButton(R.string.dialog_rename_positiveButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String toName = renameField.getText().toString().trim();
                                if (!isNameValid(toName)) {
                                    renameField.setError("Title is not valid");
                                } else {
                                    RenameDialogListener listener =
                                            (RenameDialogListener) getActivity();
                                    listener.onRenameClick(recording, toName, adaptorPosition);
                                    dialog.dismiss();
                                }
                            }
                        })
                .setNegativeButton(R.string.dialog_negativeButton,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        // automatically focus the edittext on create dialog
        renameField.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        return builder.create();
    }

    /**
     * The factory method that creates new instance of this fragment
     * @param recording the recording to be renamed; needed for inserting the renamed recording into
     *                  database
     * @param position adaptor position
     * @return an instance of this fragment
     */
    public static RenameDialogFragment newInstance(Recording recording, int position){
        RenameDialogFragment dialog = new RenameDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(Recording.PARCEL_KEY, recording);
        args.putInt(RecordingAdaptor.ADAPTOR_POSITION, position);
        dialog.setArguments(args);

        return dialog;
    }

    private boolean isNameValid(String name) {
        return !name.equals("");
    }

}
