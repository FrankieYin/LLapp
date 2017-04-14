package georgia.languagelandscape.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.fragments.RenameDialogFragment;

public class RecordingAdaptor extends RecyclerView.Adapter<RecordingAdaptor.ViewHolder>{

    private Context context;
    private List<Recording> recordings;
    public static final String ADAPTOR_POSITION = "adaptor position";

    public RecordingAdaptor(Context context, List<Recording> recordings) {
        this.context = context;
        this.recordings = recordings;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public TextView rlTitle;
        public TextView rlLocation;
        public TextView rlSpeakers;
        public TextView rlLanguages;
        public TextView rlDate;
        public Button moreButton;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            rlTitle = (TextView) itemView.findViewById(R.id.recordingList_title);
            rlLocation = (TextView) itemView.findViewById(R.id.recordingList_location);
            rlSpeakers = (TextView) itemView.findViewById(R.id.recordingList_speakers_content);
            rlLanguages = (TextView) itemView.findViewById(R.id.recordingList_languages_content);
            rlDate = (TextView) itemView.findViewById(R.id.recordingList_date);
            moreButton = (Button) itemView.findViewById(R.id.recordingList_more);
        }
    }

    private class PopupViewHolder {
        public ViewGroup layout;
        public TextView title;
        public TextView date;
        public TextView description;
        public TextView duration;
        public TextView language;
        public TextView location;
        public TextView speaker;
        public LinearLayout uploadLayout = null;
        public LinearLayout renameLayout = null;
        public LinearLayout deleteLayout = null;

        public PopupViewHolder(ViewGroup v) {
            title = (TextView) v.findViewById(R.id.more_title);
            date = (TextView) v.findViewById(R.id.more_date);
            description = (TextView) v.findViewById(R.id.more_description);
            duration = (TextView) v.findViewById(R.id.more_duration);
            language = (TextView) v.findViewById(R.id.more_language);
            location = (TextView) v.findViewById(R.id.more_location);
            speaker = (TextView) v.findViewById(R.id.more_speaker);
            uploadLayout = (LinearLayout) v.findViewById(R.id.option_upload);
            renameLayout = (LinearLayout) v.findViewById(R.id.option_rename);
            deleteLayout = (LinearLayout) v.findViewById(R.id.option_delete);
        }
    }

    @Override
    public RecordingAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_recordings, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecordingAdaptor.ViewHolder holder, int position) {
        final Recording recording = recordings.get(position);
        final FrameLayout rootLayout = (FrameLayout)
                ((FragmentActivity) context).findViewById(R.id.myrecodings_root);

        holder.rlTitle.setText(recording.getTitle());
        holder.rlLanguages.setText(recording.getLanguage().get(0));
        holder.rlSpeakers.setText(recording.getSpeakers().get(0));
        holder.rlLocation.setText(recording.getLocation());
        holder.rlDate.setText(recording.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recording.play();
            }
        });
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup layout = (ViewGroup) inflator.inflate(R.layout.popup_edit_recording, null);
                final PopupWindow popupWindow = new PopupWindow(layout, 600, 850, true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rootLayout.getForeground().setAlpha(0);
                    }
                });
                PopupViewHolder popupViewHolder = new PopupViewHolder(layout);
                popupViewHolder.title.setText(recording.getTitle());
                popupViewHolder.date.setText(recording.getDate());
                popupViewHolder.description.setText(recording.getDescription());

                Long duration = recording.getDuration();
                int seconds = (int) (duration / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                popupViewHolder.duration.setText(String.format("%02d:%02d", minutes, seconds));

                popupViewHolder.language.setText(recording.getLanguage().get(0));
                popupViewHolder.location.setText(recording.getLocation());
                popupViewHolder.speaker.setText(recording.getSpeakers().get(0));

                popupViewHolder.renameLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RenameDialogFragment dialog = RenameDialogFragment
                                .newInstance(recording, holder.getAdapterPosition());
                        dialog.show(((FragmentActivity)context).getSupportFragmentManager(),
                                "rename dialog");
                        popupWindow.dismiss();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(8);
                }
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                rootLayout.getForeground().setAlpha(120);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }
}