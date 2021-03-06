/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import georgia.languagelandscape.R;
import georgia.languagelandscape.data.Project;

/**
 * Adaptor for Project list.
 */
public class ProjectAdaptor extends RecyclerView.Adapter<ProjectAdaptor.ViewHolder>{

    private Context context;
    private List<Project> projects;

    public ProjectAdaptor(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView count;
        public TextView fullName;
        public TextView owner;
        public TextView numRecording;
        public TextView description;
        public Button moreButton;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            count = (TextView) itemView.findViewById(R.id.project_count);
            fullName = (TextView) itemView.findViewById(R.id.project_full_name);
            owner = (TextView) itemView.findViewById(R.id.project_owner);
            numRecording = (TextView) itemView.findViewById(R.id.project_num_recording);
            description = (TextView) itemView.findViewById(R.id.project_description);
            moreButton = (Button) itemView.findViewById(R.id.more);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_projects, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Project project = projects.get(position);

        holder.count.setText(String.format("%d - ", position + 1));
        holder.fullName.setText(project.getFullName());
        int num = project.getRecordingCount();
        if (num == 0) {
            holder.numRecording.setText(R.string.project_list_no_recording);
        } else {
            holder.numRecording.setText(String.format("%d recordings", num));
        }
        holder.owner.setText(String.format("by %s", project.getOwner().getUsername()));
        holder.description.setText(project.getDescription());
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: show a popup
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
