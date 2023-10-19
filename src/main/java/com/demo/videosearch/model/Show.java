package com.demo.videosearch.model;

import com.demo.videosearch.model.IShow;

/**
 * Instances of classes that implement this interface represent a single
 * streaming show object that can be stored, sorted, and searched for based on
 * these accessors below.
 */
@SuppressWarnings("unchecked")
public class Show implements IShow {
    private String title;
    private int year;
    private int rating;
    private String providers;

    public Show(String title, int year, int rating, String providers) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.providers = providers;
    }

    /**
     * retrieve the title of this show object
     *
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * retrieve the year that this show was first produced
     *
     * @return
     */
    public int getYear() {
        return this.year;
    }

    /**
     * retrieve the Rotten Tomatoes Rating (out of 100)
     *
     * @return
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * toString() for testing
     *
     * @return
     */
    @Override
    public String toString() {
        return "IShow{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rating=" + rating +
                ", providers='" + providers + '\'' +
                '}';
    }

    /**
     * checks show availability
     *
     * @param provider
     * @return
     */
    public boolean isAvailableOn(String provider) {
        String[] ltProviders = this.providers.split(",");
        for (String str : ltProviders) {
            if (str.equals(provider)) {
                return true;
            }
        }
        return false;
    }

    /**
     * compareTo() method supports sorting shows in descending order by rating
     *
     * @param o IShow o to compare other object
     * @return 1, 0,-1 based on the needs of descending or ascending
     */
    @Override
    public int compareTo(IShow o) {
        if (o.getRating() < this.rating) {
            return -1;
        } else if (o.getRating() > this.rating) {
            return 1;
        } else {
            return 0;
        }
    }
}


