package com.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/** A class used to represent a Playlist */
class VideoPlaylist {
    public String name;
    private List<String> videoIds;
    public int numberOfVideos;

    VideoPlaylist(String name) {
        this.name = name;
        videoIds = new ArrayList<>();
        numberOfVideos = 0;
    }

    public void addVideo(String id) {
        for (String videoId: videoIds) {
            if (videoId.equals(id)) return;
        }
        videoIds.add(id);
        numberOfVideos++;
    }

    public void removeVideo(String id) {
        for (String videoId: videoIds) {
            if(videoId.equals(id)) {
                videoIds.remove(videoId);
                numberOfVideos--;
                return;
            }
        }
    }

    public void clearList() {
        if (numberOfVideos > 0) videoIds.clear();
        numberOfVideos = 0;
    }

    public List<String> getVideoIds() {
        return videoIds;
    }

}
