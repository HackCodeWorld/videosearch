package com.demo.videosearch.service;
import com.demo.videosearch.model.IShow;
import com.demo.videosearch.model.Show;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Instances of classes that implement this interface can be used to load a
 * list of shows from a specified csv source file.
 * The following csv columns are used to load these show attributes:
 * - Title: the complete title for a show
 * - Year: the year that the show was first produced
 * - Rotten Tomatoes: the review score (out of 100) for this show
 * - Netflix: 1 = available on this service, otherwise 0
 * - Hulu: 1 = available on this service, otherwise 0
 * - Prime Video: 1 = available on this service, otherwise 0
 * - Disney+: 1 = available on this service, otherwise 0
 */
@SuppressWarnings("unchecked")
public class ShowLoader implements IShowLoader {
    /**
     * This method loads the list of songs described within a CSV file.
     *
     * @param filepath is relative to executable's working directory
     * @return a list of show objects that were read from specified file
     */
    public List<IShow> loadShows(String filepath) throws FileNotFoundException {
        String line = "";
        String providers;
        List<IShow> lt = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //each line canceling out quotes inside
            ArrayList<String> lines = checkQuotes(line);

            if (line.contains("Title,Year")) {
                continue;
            }
            String rate = lines.get(6);
            String[] strlt = rate.split("/");
            int rating = Integer.parseInt(strlt[0]);

            //adjust for providers formatting
            String providersNames = providersFormat(lines.get(7) + lines.get(8) + lines.get(9) + lines.get(10));

            // set attributes to each IShow object
            int year = Integer.parseInt(lines.get(3));
            Show show = new Show(lines.get(2), year, rating,
                    providersNames);
            lt.add(show);
        }

        return lt;
    }

    /**
     * helper method to get the string representations of all available providers' names
     *
     * @param providers all available providers' names in one and zero format
     * @return String result - all available providers' names
     */
    private String providersFormat(String providers) {
        String result = "";
        for (int i = 0; i < providers.length(); i++) {
            char c = providers.charAt(i); //it's available if char is '1'
            //based on teh index of string to find location of the streaming source provider
            //and follow up with these cases
            if ((i == 0) && c == '1') {
                result += "Netflix";
            }
            if (i == 1 && c == '1') {
                result = result.isEmpty() ? result + "Hulu" : result + ",Hulu";
            }
            if (i == 2 && c == '1') {
                result = result.isEmpty() ? result + "Prime Video" : result + ",Prime Video";
            }
            if (i == 3 && c == '1') {
                result = result.isEmpty() ? result + "Disney+" : result + ",Disney+";
            }
        }
        return result;
    }

    /**
     * this method check problems of formatting in each line with simple and complex quotes and comma
     * because of CSV inside the field, and change it to the original meaning's formatting
     *
     * @param line the line without formatting
     * @return ArrayList<String> lineAfter - for that specific row of data with correct formatting
     */
    private ArrayList<String> checkQuotes(String line) {
        int countQuotes = 0; //number of quotes in each field of that row
        String[] lines = line.split(",");
        ArrayList<String> lineAfter = new ArrayList(); //single line's list with each field
        List<String> strBefore = new ArrayList();//// like the previous string of "Love,
        String strCombineQuotes = ""; // like the string combo "Love, Me"
        for (String str : lines) {
            String strAfter = ""; // like the following string of  Me"
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == ('\"')) {
                    countQuotes++;
                }
                strAfter += str.charAt(i);
            }
            if (countQuotes % 2 == 1) { //it's odd quote count
                strBefore.add(strAfter.substring(countQuotes));
                continue;
            }
            if (countQuotes != 0 && countQuotes % 2 == 0) { //it's an even quote count
                String atEndOrStartWithoutQuote = "\"";// csv replaces at end / begin following single " -> "
                String atEndOrStart = "\"\"\"";// csv replaces " at end / begin with " following "" -> """
                String atMiddle = "\"\"";// csv replaces " at middle with " following " ->""
                boolean atEndStart = strAfter.indexOf("\"") == 0 || strAfter.indexOf("\"") == strAfter.length() - 1;
                if (countQuotes >= 3) { //fixed : better time complexity optimization using conditions
                    //strAfter got complex quotes inside
                    if (strAfter.contains(atEndOrStartWithoutQuote) && atEndStart) {
                        // at begin or end but without " so there will be "
                        strCombineQuotes = strAfter.replaceFirst("\"", "");
                    }
                    if (strAfter.contains(atEndOrStart) && atEndStart) {
                        // " at begin or end so there will be """
                        strCombineQuotes = strCombineQuotes.replace(atEndOrStart, "\"");
                    }
                    if (strAfter.contains(atMiddle) && (strAfter.indexOf("\"") != 0 ||
                            strAfter.indexOf("\"") != strAfter.length() - 1)) {
                        // csv replaces " at middle with " following " ->""
                        strCombineQuotes = strCombineQuotes.replace(atMiddle, "\"");
                    }
                }
                if (countQuotes == 2) { // only got two quotes in a title
                    //simple strAfter (ie. "xxx,xxx" / "xxx")
                    strCombineQuotes = strAfter.substring(0, strAfter.length() - 1);
                    String strBeforeList = "";
                    for (int i = 0; i < strBefore.size(); i++) {
                        if (i == strBefore.size() - 1) {
                            strBeforeList += strBefore.get(i) + ",";
                        } else {
                            strBeforeList += strBefore.get(i) + ", ";
                        }
                    }//combine two parts together to one
                    strCombineQuotes = strBeforeList + strCombineQuotes;
                }
                lineAfter.add(strCombineQuotes);
                countQuotes = 0;
                continue;
            }
            lineAfter.add(strAfter);
        }
        return lineAfter;

    }

}
