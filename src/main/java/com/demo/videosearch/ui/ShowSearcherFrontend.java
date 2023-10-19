package com.demo.videosearch.ui;
import com.demo.videosearch.model.IShow;
import com.demo.videosearch.service.ShowSearcherBackend;
import com.demo.videosearch.ui.IShowSearcherFrontend;

import java.util.List;
import java.util.Scanner;

public class ShowSearcherFrontend implements IShowSearcherFrontend {
	private enum Mode {
	    MENU,
	    TITLE,
	    YEAR,
	    FILTER,
	}
	
	private ShowSearcherBackend backend;
	private Scanner scanner;
	private Mode mode;
	private boolean running;
	private int total;
	private String cmd;
	  
	// constructor args (ShowSearcherBackend) reads input from System.in
	public ShowSearcherFrontend(ShowSearcherBackend backend) {
	    this.backend = backend;
		scanner = new Scanner(System.in);
		mode = Mode.MENU;
	    running = true;
	    total = backend.getNumberOfShows();
	}
	
    // constructor args (String, ShowSearcherBackend) reads input from String
	public ShowSearcherFrontend(String input, ShowSearcherBackend backend) {
		this.backend = backend;
		cmd = input;
		mode = Mode.MENU;
	    running = true;
	    total = backend.getNumberOfShows();
	}

    /**
     * This method drives the entire read, eval, print loop (repl) for the
     * Song Search App.  This loop will continue to run until the user 
     * explicitly enters the quit command. 
     */
    public void runCommandLoop() {
    	while (running) {
    		switch (mode) {
    	    	case MENU:
    	        	displayCommandMenu();
    	        	break;
    	        case TITLE:
    	        	titleSearch();
    	        	break;
    	        case YEAR:
    	        	yearSearch();
    	        	break;   
    	        case FILTER:
    	        	filterByProvider();
    	        	break;
    		}
    	}
    }

    // to help make it easier to test the functionality of this program, 
    // the following helper methods will help support runCommandLoop():

    public void displayCommandMenu() {
    	// prints command options to System.out
    	System.out.println("Command Menu:");
    	System.out.println("\t1) Search by [T]itle Word");
    	System.out.println("\t2) Search by [Y]ear First Produced");
    	System.out.println("\t3) [F]ilter by Streaming Provider");
    	System.out.println("\t4) [Q]uit");
    	System.out.println("Choose a command from the menu above (number or letter in bracket): ");
    	String cmd = scanner.nextLine();
    	
    	switch (cmd) {
        	case "T": case "t": case "1":
        		mode = Mode.TITLE;
        		return;
        	case "Y": case "y": case "2":
        		mode = Mode.YEAR;
        		return;
        	case "F": case "f": case "3":
        		mode = Mode.FILTER;
        		return;
        	case "Q": case "q": case "4":
        		running = false;
        		return;
    	}
    	
    }

    public void displayShows(List<IShow> shows) {
    	// displays a list of shows
    	
    	// display the number of shows found
    	int id = 1;
    	for(IShow show: shows) {
    		String provider = "";
    		if(show.isAvailableOn("Netflix"))
    			provider += "Netflix ";
    		if(show.isAvailableOn("Hulu"))
    			provider += "Hulu ";
    		if(show.isAvailableOn("Prime Video"))
    			provider += "Prime Video ";
    		if(show.isAvailableOn("Disney+"))
    			provider += "Disney+ ";
    		System.out.println(id + ". " + show.getTitle() + "\n\t" + show.getRating()
    							+ "/100 (" + show.getYear() + ") on: " + provider);
    		id++;
    	}
    }
   
    public void titleSearch() {
    	// reads word from System.in, displays results

    	// reads the word from System.in
    	System.out.println("Choose a word that you would like to search for: ");
    	String filter = scanner.nextLine();
    	
    	// next use the backend filter method to get the list of shows
    	List<IShow> filteredByWord = backend.searchByTitleWord(filter);
    	int count = filteredByWord.size();
    	
    	//display the filtered show list using the displayShows method
    	System.out.println("Found" + count + "/" + total + "matches.");
    	displayShows(filteredByWord);
    	
    	// return to command menu
    	displayCommandMenu();
    }
    
    public void yearSearch() {
    	// reads year from System.in, displays results
    	
    	// reads the year from System.in and convert to int
    	System.out.println("Enter a year (2015) that you would like to search for: ");
    	String yr = scanner.nextLine();
    	int year = Integer.parseInt(yr);
    	
    	// next use the backend search by year method to get the list of shows
    	List<IShow> filteredByYear = backend.searchByYear(year);
    	int count = filteredByYear.size();
    	
    	//display the filtered show list using the displayShows method
    	System.out.println("Found" + count + "/" + total + "matches.");
    	displayShows(filteredByYear);

    	// return to command menu
    	displayCommandMenu();
    }
    
    private void filterByProvider() {		
    	// display the list of providers currently being filtered for
    	boolean loop = true;
    	while(loop) {
    		System.out.println("Providers that shows are being searched for: ");
    		if(backend.getProviderFilter("Netflix")) {
    			System.out.println("\t1) _x_ [N]eflix");
    		} else {
    			System.out.println("\t1) ___ [N]eflix");
    		}
    		if(backend.getProviderFilter("Hulu")) {
    			System.out.println("\t2) _x_ [H]ulu");
    		} else {
    			System.out.println("\2) ___ [H]ulu");
    		}
    		if(backend.getProviderFilter("Prime Video")) {
    		System.out.println("\t3) _x_ [P]rime Video");
    		} else {
    			System.out.println("\t3) ___ [P]rime Video");
    		}
    		if(backend.getProviderFilter("Disney+")) {
    			System.out.println("\t4) _x_ [D]isney+");
    		} else {
    			System.out.println("\t4) ___ [D]isney+");
    		}
    		System.out.println("5) [Q]uit toggling provider filters");

    		// Get Cmd from user
    		System.out.println("Choose the provider that you'd like to toggle the filter for (number or letter in bracket): ");
    		String cmd = scanner.nextLine();
        	
    		// toggle or quit
        	switch (cmd) {
            	case "N": case "n": case "1":
            		backend.toggleProviderFilter("Netflix");
            		backend.setProviderFilter("Netflix", backend.getProviderFilter("Netflix"));
            		break;
            	case "H": case "h": case "2":
            		backend.toggleProviderFilter("Hulu");
            		backend.setProviderFilter("Hulu", backend.getProviderFilter("Hulu"));
            		break;
            	case "P": case "p": case "3":
            		backend.toggleProviderFilter("Prime Video");
            		backend.setProviderFilter("Prime Video", backend.getProviderFilter("Prime Video"));
            		break;
            	case "D": case "d": case "4":
            		backend.toggleProviderFilter("Disney+");
            		backend.setProviderFilter("Disney+", backend.getProviderFilter("Disney+"));
            		break;
            	case "Q": case "q": case "5":
            		loop = false;
            		break;
        	}
    	}

    	// return to command menu
    	displayCommandMenu();
    }
}
