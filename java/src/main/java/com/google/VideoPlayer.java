package com.google;

import java.util.*;

// This isn't my best work
// But had to get it in by the deadline


public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private static Video currentVideo;
  private static boolean isPlaying;
  private List<VideoPlaylist> allPlayLists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    currentVideo = null;
    isPlaying = false;
    allPlayLists = new ArrayList<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> allVideos = this.videoLibrary.getVideos();
    String[] titles = new String[allVideos.size()];
    StringBuilder allTitles = new StringBuilder();
    allTitles.append("Here's a list of all available videos: ");
    for (int i = 0; i < titles.length; i++) {
      Video v = allVideos.get(i);
      List<String> tags = v.getTags();
      StringBuilder thisTag = new StringBuilder();
      for (int j = 0; j < tags.size(); j++) {
        if (j == tags.size() - 1) thisTag.append(tags.get(j));
        else thisTag.append(tags.get(j)).append(" ");
      }
      String tag = thisTag.toString();
      StringBuilder title = new StringBuilder();
      title.append(v.getTitle()).append(" ");
      title.append("(");
      title.append(v.getVideoId());
      title.append(") ");
      title.append("[");
      title.append(tag).append("]");
      if (v.isFlagged) {
        title.append(" - FLAGGED (reason: "+v.flagReason+")");
      }

      String thisTitle = title.toString();
      titles[i] = thisTitle;


    }
    Arrays.sort(titles, String.CASE_INSENSITIVE_ORDER);
    System.out.println(allTitles);

    for (String s: titles) {
      System.out.println("  "+s);
    }
  }

  public void playVideo(String videoId) {
    Video newVideo = this.videoLibrary.getVideo(videoId);
    if (newVideo == null) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    }
    if (newVideo.isFlagged) {
      if (newVideo.flagReason.isBlank()) {
        System.out.println("Cannot play video: Video is currently flagged (reason: Not supplied)");
      } else {
        System.out.println("Cannot play video: Video is currently flagged (reason: "+newVideo.flagReason+")");
      }

      return;
    }
    if (isPlaying || currentVideo != null) {
      System.out.println("Stopping video: "+currentVideo.getTitle());
    }
    currentVideo = newVideo;
    System.out.println("Playing video: "+currentVideo.getTitle());
    isPlaying = true;
  }

  public void stopVideo() {
    if (!isPlaying && currentVideo == null) {
      System.out.println("Cannot stop video: No video is currently playing");
      return;
    }
    isPlaying = false;
    System.out.println("Stopping video: "+currentVideo.getTitle());
    currentVideo = null;
  }

  public void playRandomVideo() {
    int maxIndex = this.videoLibrary.getNumberOfVideos() - 1;
    int randomIndex = (int) (Math.floor(maxIndex * Math.random()));
    List<Video> allVideos = this.videoLibrary.getVideos();
    boolean allFlagged = true;
    for (Video v: allVideos) {
      if (!v.isFlagged) {
        allFlagged = false;
        break;
      }
    }
    if (allVideos.size() == 0 || allFlagged) {
      System.out.println("No videos available");
      return;
    }
    while(allVideos.get(randomIndex).isFlagged) {
      randomIndex = (int) (Math.floor(maxIndex * Math.random()));
    }
    if (isPlaying || currentVideo != null) {
      System.out.println("Stopping video: "+currentVideo.getTitle());
    }
    currentVideo = allVideos.get(randomIndex);

    System.out.println("Playing video: "+currentVideo.getTitle());
    isPlaying = true;
  }

  public void pauseVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }
    if (isPlaying) {
      System.out.println("Pausing video: "+currentVideo.getTitle());
      isPlaying = false;
    } else
      System.out.println("Video already paused: "+currentVideo.getTitle());
  }

  public void continueVideo() {
    if (currentVideo == null) {
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }
    if (isPlaying) {
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }
    System.out.println("Continuing video: "+currentVideo.getTitle());
    isPlaying = true;
  }

  public void showPlaying() {
    if (currentVideo == null) {
      System.out.println("No video is currently playing");
      return;
    }
    List<String> tags = currentVideo.getTags();
    StringBuilder thisTag = new StringBuilder();
    for (int i = 0; i < tags.size(); i++) {
      if (i == tags.size() - 1) thisTag.append(tags.get(i));
      else thisTag.append(tags.get(i)).append(" ");
    }
    String tag = thisTag.toString();
    StringBuilder showPlaying = new StringBuilder();
    showPlaying.append("Currently playing: ");
    showPlaying.append(currentVideo.getTitle()).append(" ");
    showPlaying.append("(");
    showPlaying.append(currentVideo.getVideoId());
    showPlaying.append(") ");
    showPlaying.append("[");
    showPlaying.append(tag);
    showPlaying.append("]");
    if (!isPlaying) {
      showPlaying.append(" - PAUSED");
    }
    System.out.println(showPlaying);
  }

  public void createPlaylist(String playlistName) {
    VideoPlaylist newList = new VideoPlaylist(playlistName);
    for (VideoPlaylist playlist: allPlayLists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        return;
      }
    }
    allPlayLists.add(newList);
    System.out.println("Successfully created new playlist: "+playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    for (VideoPlaylist playlist: allPlayLists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        int n = playlist.numberOfVideos;
        Video v = this.videoLibrary.getVideo(videoId);
        if (v == null) {
          System.out.println("Cannot add video to "+playlistName+": Video does not exist");
          return;
        }
        if (v.isFlagged) {
          if (v.flagReason.isBlank()) {
            System.out.println("Cannot add video to "+playlistName+": Video is currently flagged (reason: Not supplied)");
          } else {
            System.out.println("Cannot add video to "+playlistName+": Video is currently flagged (reason: "+v.flagReason+")");
          }

          return;
        }
        playlist.addVideo(videoId);
        if (playlist.numberOfVideos == n) {
          System.out.println("Cannot add video to "+playlistName+": Video already added");
          return;
        }
        System.out.println("Added video to "+playlistName+": "+v.getTitle());
        return;
      }
    }
    System.out.println("Cannot add video to "+playlistName+": Playlist does not exist");
  }

  public void showAllPlaylists() {
    if (allPlayLists.size() == 0) {
      System.out.println("No playlists exist yet");
      return;
    }
    String[] playlistNames = new String[allPlayLists.size()];
    for (int i = 0; i < allPlayLists.size(); i++) {
      playlistNames[i] = allPlayLists.get(i).name;
    }
    Arrays.sort(playlistNames, String.CASE_INSENSITIVE_ORDER);
    System.out.println("Showing all playlists: ");
    for (String playlistName: playlistNames) {
      System.out.println("  "+playlistName);
    }
  }

  public void showPlaylist(String playlistName) {
    for (VideoPlaylist playlist : allPlayLists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        System.out.println("Showing playlist: " +playlistName);
        if (playlist.numberOfVideos == 0) {
          System.out.println("  No videos here yet");
          return;
        }
        List<String> listState = playlist.getVideoIds();
        for (String videoId : listState) {
          Video v = this.videoLibrary.getVideo(videoId);

          List<String> tags = v.getTags();
          StringBuilder tagNames = new StringBuilder();
          for (int j = 0; j < tags.size(); j++) {
            if (j == tags.size() - 1) tagNames.append(tags.get(j));
            else tagNames.append(tags.get(j)).append(" ");
          }

          StringBuilder sb = new StringBuilder();

          sb.append("  ").append(v.getTitle()).append(" (").append(videoId).append(") [").append(tagNames).append("]");
          if (v.isFlagged) {
            sb.append(" - FLAGGED (reason: "+v.flagReason+")");
          }
          System.out.println(sb);
        }
        return;
      }
    }
    System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    for (VideoPlaylist playlist: allPlayLists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        List<Video> allVideos = this.videoLibrary.getVideos();
        for (Video v: allVideos) {
          if (v.getVideoId().equals(videoId)) {
            int n = playlist.numberOfVideos;
            playlist.removeVideo(videoId);
            if (playlist.numberOfVideos == n) {
              System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
              return;
            }
            System.out.println("Removed video from "+playlistName+": "+v.getTitle());
            return;
          }
        }
        System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
        return;
      }
    }
    System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
  }

  public void clearPlaylist(String playlistName) {
    for (VideoPlaylist playlist: allPlayLists) {
      if (playlist.name.equalsIgnoreCase(playlistName)) {
        playlist.clearList();
        System.out.println("Successfully removed all videos from "+playlistName);
        return;
      }
    }
    System.out.println("Cannot clear playlist "+playlistName+": Playlist does not exist");
  }

  public void deletePlaylist(String playlistName) {
    for (VideoPlaylist playlist: allPlayLists) {
      if (playlist.name.equals(playlistName.toLowerCase())) {
        allPlayLists.remove(playlist);
        System.out.println("Deleted playlist: "+playlistName);
        return;
      }
    }
    System.out.println("Cannot delete playlist "+playlistName+": Playlist does not exist");
  }

  public void searchVideos(String searchTerm) {
    String searchFor = searchTerm.toLowerCase();

    List<Video> allVideos = this.videoLibrary.getVideos();
    List<String> searchRes = new ArrayList<>();


    for (Video v : allVideos) {
      if(!v.isFlagged) {
        if (v.getTitle().toLowerCase().contains(searchFor)) {
          searchRes.add(v.getVideoId());
        }
      }
    }

    if (searchRes.size() != 0) {
      String[] tmp = new String[searchRes.size()];
      searchRes.toArray(tmp);
      Arrays.sort(tmp, String.CASE_INSENSITIVE_ORDER);
      searchRes = Arrays.asList(tmp);
      int count = 1;
      System.out.println("Here are the results for " + searchTerm + ":");
      for (String id : searchRes) {
        Video v = this.videoLibrary.getVideo(id);
        List<String> tags = v.getTags();
        StringBuilder tagNames = new StringBuilder();
        for (int j = 0; j < tags.size(); j++) {
          if (j == tags.size() - 1) tagNames.append(tags.get(j));
          else tagNames.append(tags.get(j)).append(" ");
        }

        System.out.println("  " + count + ") " + v.getTitle() + " (" + id + ") [" + tagNames + "]");
        count++;
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      var scanner = new Scanner(System.in);

      var input = scanner.nextLine();
      
      try {
        int number = Integer.parseInt(input);
        if (number <= searchRes.size()) {
          playVideo(searchRes.get(number - 1));
          return;
        }
        return;
      } catch (NumberFormatException ex) {
        return;
      }
      
    }
    System.out.println("No search results for " + searchTerm);
  }
  public void searchVideosWithTag(String videoTag) {
    String searchTag = videoTag.toLowerCase();

    List<Video> allVideos = this.videoLibrary.getVideos();
    List<String> searchRes = new ArrayList<>();

    for (Video v: allVideos) {
      if (!v.isFlagged) {
        for (String tag: v.getTags()) {
          if (tag.equals(searchTag)) {
            searchRes.add(v.getVideoId());
            break;
          }
        }
      }
    }

    if (searchRes.size() != 0) {
      String[] tmp = new String[searchRes.size()];
      searchRes.toArray(tmp);
      Arrays.sort(tmp, String.CASE_INSENSITIVE_ORDER);
      searchRes = Arrays.asList(tmp);

      int count = 1;
      System.out.println("Here are the results for " + videoTag + ":");
      for (String id : searchRes) {
        Video v = this.videoLibrary.getVideo(id);
        List<String> tags = v.getTags();
        StringBuilder tagNames = new StringBuilder();
        for (int j = 0; j < tags.size(); j++) {
          if (j == tags.size() - 1) tagNames.append(tags.get(j));
          else tagNames.append(tags.get(j)).append(" ");
        }

        System.out.println("  " + count + ") " + v.getTitle() + " (" + id + ") [" + tagNames + "]");
        count++;
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      var scanner = new Scanner(System.in);

      var input = scanner.nextLine();
      
      try {
        int number = Integer.parseInt(input);
        if (number <= searchRes.size()) {
          playVideo(searchRes.get(number - 1));
          return;
        }
        return;
      } catch (NumberFormatException ex) {
        return;
      }
      
    }
    System.out.println("No search results for "+ videoTag);
  }

  public void flagVideo(String videoId) {
    List<Video> allVideos = this.videoLibrary.getVideos();
    for(Video v: allVideos) {
      if (v.getVideoId().equals(videoId)) {
        if (v.isFlagged) {
          System.out.println("Cannot flag video: Video is already flagged");
          return;
        }
        v.isFlagged = true;
        if (currentVideo == v) stopVideo();
        System.out.println("Successfully flagged video: "+v.getTitle()+" (reason: Not supplied)");

        return;
      }
    }
    System.out.println("Cannot flag video: Video does not exist");
  }

  public void flagVideo(String videoId, String reason) {
    List<Video> allVideos = this.videoLibrary.getVideos();
    for(Video v: allVideos) {
      if (v.getVideoId().equals(videoId)) {
        if (v.isFlagged) {
          System.out.println("Cannot flag video: Video is already flagged");
          return;
        }
        v.isFlagged = true;
        v.flagReason = reason;
        if(currentVideo == v) stopVideo();
        System.out.println("Successfully flagged video: "+v.getTitle()+" (reason: "+ v.flagReason+")");
        return;
      }
    }
    System.out.println("Cannot flag video: Video does not exist");
  }

  public void allowVideo(String videoId) {
    Video v = this.videoLibrary.getVideo(videoId);
    if (v == null) {
      System.out.println("Cannot remove flag from video: Video does not exist");
      return;
    }
    if (v.isFlagged) {
      v.isFlagged = false;
      System.out.println("Successfully removed flag from video: "+v.getTitle());
      return;
    }
    System.out.println("Cannot remove flag from video: Video is not flagged");
  }
}