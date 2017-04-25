package georgia.languagelandscape.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import georgia.languagelandscape.R;

public class MetaDataFieldFragment extends Fragment {

    public static final String ARGS_QUESTION = "question number";
    private Context context;
    private EditText field;
    private TextView prompt;
    private int question;

    public static final int name = 0;
    public static final int languages = 1;
    public static final int speakers = 2;
    public static final int aboutWhat = 3;
    public static final int genre = 4;
    public static final int whichProject = 5;
    public static final int description = 6;
    public static final int keywords = 7;
    public static final int publicEdit = 8;
    public static final int moreDetail = 9;
    public static final int[] mandatoryFields = {languages, speakers, publicEdit};
    public static final int[] allFields = {name, languages, speakers, aboutWhat, genre, whichProject, description, keywords, publicEdit, moreDetail};
    public static final int[] necessaryFields = {name, languages, speakers, description};

    public MetaDataFieldFragment() {
    }

    public interface MetaDataFieldFragmentListener {
        public void onUserInput(String inputString, int which);
    }

    public static MetaDataFieldFragment newInstance(int question) {
        MetaDataFieldFragment metaDataFragment = new MetaDataFieldFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_QUESTION, question);
        metaDataFragment.setArguments(bundle);

        return metaDataFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meta_fields, container, false);

        prompt = (TextView) view.findViewById(R.id.prompt);
        field = (EditText) view.findViewById(R.id.field);

        question = getArguments().getInt(ARGS_QUESTION);
        switch (question) {
            case name:
                prompt.setText(R.string.name_prompt);
                field.setHint(R.string.name_hint);
                break;
            case languages:
                prompt.setText(R.string.languages_prompt);
                field.setHint(R.string.languages_hint);
                break;
            case speakers:
                prompt.setText(R.string.speakers_prompt);
                field.setHint(R.string.speakers_hint);
                break;
            case aboutWhat:
                prompt.setText(R.string.aboutWhat_prompt);
                field.setHint(R.string.aboutWhat_hint);
                break;
            case genre:
                prompt.setText(R.string.genre_prompt);
                field.setHint(R.string.genre_hint);
                break;
            case whichProject:
                prompt.setText(R.string.whichProject_prompt);
                field.setHint(R.string.whichProject_hint);
                break;
            case description:
                prompt.setText(R.string.description_prompt);
                field.setHint(R.string.description_hint);
                field.setLines(5);
                field.setVerticalScrollBarEnabled(true);
                field.setGravity(Gravity.TOP);
                break;
            case keywords:
                prompt.setText(R.string.keywords_prompt);
                field.setHint(R.string.keywords_hint);
                break;
            case publicEdit:
                prompt.setText(R.string.publicEdit_prompt);
                field.setHint(R.string.publicEdit_hint);
                break;
            case moreDetail:
                prompt.setText(R.string.moreDetail_prompt);
                field.setHint(R.string.moreDetail_hint);
                break;
        }

        field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_NEXT:
                        return true;
                    default:
                        return false;
                }
            }
        });

        field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("test", String.valueOf(hasFocus));
                if (!hasFocus) {
                    ((MetaDataFieldFragmentListener) context)
                            .onUserInput(field.getText().toString(), question);
                }
            }
        });

        return view;
    }

    public String getFieldText() {
        Log.d("test", String.valueOf((field == null)));
        return (field == null) ? "" : field.getText().toString();
    }

    public void focusField() {
        field.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
