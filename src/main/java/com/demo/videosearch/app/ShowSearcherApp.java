package com.demo.videosearch.app;

import com.demo.videosearch.model.IShow;
import com.demo.videosearch.service.IShowLoader;
import com.demo.videosearch.service.ShowLoader;
import com.demo.videosearch.service.ShowSearcherBackend;
import com.demo.videosearch.ui.IShowSearcherFrontend;
import com.demo.videosearch.ui.ShowSearcherFrontend;

import java.util.List;

/**
 * The provided class
 */
public class ShowSearcherApp {
  public static void main(String[] args) throws Exception {
    //IShowLoader loader = null; // new ShowLoader();
    IShowLoader loader = new ShowLoader();
    List<IShow> shows = loader.loadShows("src/main/resources/data/tv_shows.csv");
    ShowSearcherBackend backend = new ShowSearcherBackend();
    for (IShow show : shows)
      backend.addShow(show);
    IShowSearcherFrontend frontend = new ShowSearcherFrontend(backend);
    frontend.runCommandLoop();
  }
}


