/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.data;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import georgia.languagelandscape.MyRecordingsActivity;

/**
 * The primary data model class of LL app
 */
public class Recording implements Parcelable{

    private String recordingID = null;
    private String title = null;
    private long duration = 0L; // in milliseconds
    private String description = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String location = null;
    private ArrayList<String> language = null;
    private String date = null;
    private User uploader = null;
    private ArrayList<String> speakers = null;
    private String filePath = null;

    private boolean paused = true;
    private boolean completed = false;
    private boolean released = false;
    private MediaPlayer player = null;

    public static final String defaultAudioFormat = ".3gp";
    public static final String defaultRecordingTitle = "New Recording";
    public static final String PARCEL_KEY = "parcel";
    public static final String PARCEL_ID_KEY = "parcel id";
    public static final String PARCEL_TITLE_KEY = "parcel title";

    public Recording() {
        if (recordingID == null) {
            recordingID = UUID.randomUUID().toString();
        }
    }

    public Recording(
            String ID,
            String title,
            long duration,
            String description,
            double latitude,
            double longitude,
            String location,
            ArrayList<String> language,
            String date,
            User uploader,
            ArrayList<String> speakers,
            String filePath) {

        if (ID == null) {
            recordingID = UUID.randomUUID().toString();
        } else {
            recordingID = ID;
        }

        this.title = title;
        this.duration = duration;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.language = language;
        this.date = date;
        this.uploader = uploader;
        this.speakers = speakers;
        this.filePath = filePath;
    }

    public String getRecordingID() {
        return recordingID;
    }

    public void setRecordingID(String recordingID) {
        this.recordingID = recordingID;
    }

    public void setRecordingID() {
        this.recordingID = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getLanguage() {
        return language;
    }

    public void setLanguage(ArrayList<String> language) {
        this.language = language;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public ArrayList<String> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(ArrayList<String> speakers) {
        this.speakers = speakers;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id='" + recordingID + '\'' +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                ", language=" + language +
                ", date='" + date + '\'' +
                ", uploader='" + uploader + '\'' +
                ", speakers=" + speakers + '\'' +
                ", filePath=" + filePath +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();

        dest.writeString(recordingID);
        dest.writeString(title);
        dest.writeLong(duration);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(location);
        dest.writeStringList(language);
        dest.writeString(date);
        dest.writeString(gson.toJson(uploader));
        dest.writeStringList(speakers);
        dest.writeString(filePath);
    }


    protected Recording(Parcel in) {
        Gson gson = new Gson();

        recordingID = in.readString();
        title = in.readString();
        duration = in.readLong();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        location = in.readString();
        language = in.createStringArrayList();
        date = in.readString();
        Type type = new TypeToken<User>() {}.getType();
        uploader = gson.fromJson(in.readString(), type);
        speakers = in.createStringArrayList();
        filePath = in.readString();
    }

    public static final Creator<Recording> CREATOR = new Creator<Recording>() {
        @Override
        public Recording createFromParcel(Parcel in) {
            return new Recording(in);
        }

        @Override
        public Recording[] newArray(int size) {
            return new Recording[size];
        }
    };

    public String getDurationString() {
        int seconds = (int) (duration / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * play this recording starting from {@param at} millisecond
     * @param at the time from where to play the recording, in millisecond
     */
    public void play(int at){
        if (paused) {
            // start/resume playing the recording
            prepare();
            player.seekTo(at);
            player.start();
            completed = false;
            paused = false;
        } else if (!paused) {
            // pause the recording
            player.pause();
            paused = true;
        }
    }

    /**
     * helper to initialise and prepare a Mediaplayer
     */
    private void prepare() {
        if (player != null) return;
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                released = true;
                mp.release();
                player = null;
                Log.d("markers", "onCompletion");
                paused = true;
                completed = true;
                MyRecordingsActivity.recordingPlaying = false;
            }
        });
        try {
            player.setDataSource(filePath);
            player.prepare();
            released = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * called by activities and fragments to stop the recording
     * when necessary during their lifecycle
     */
    public void stop() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.pause();
        }
        player.stop();
        player.release();
        paused = true;
        completed = true;
        MyRecordingsActivity.recordingPlaying = false;
        player = null;
    }

    /**
     * called when a user choose to upload this recording to the server
     * @return true when recording is uploaded on the server and hence visible to the map
     *         false otherwise.
     */
    public boolean isUploaded() {
        // TODO: to be implemented
        return true;
    }

    /**
     * Get the current playtime of this recording
     * @return 0 if the recording has never been played;
     *         the Mediaplayer's current position when the recording is paused but not completed;
     *         -1 indicating an invalid state.
     */
    public int getCurrentPlaytime() {
        if (player == null) {
            return 0;
        } else if (!released) {
            return player.getCurrentPosition();
        } else {
            return -1;
        }
    }

    /**
     * Set the Mediaplayer position of this recording to {@param milisec} and start playing
     * @param millisec desired position in millisecond
     */
    public void setCurrentPlaytime(int millisec) {
        if (player == null) {
            prepare();
        }
        player.seekTo(millisec);
    }
}
