package com.android.group.view.utils;

import com.android.group.model.Venue;

import java.util.ArrayList;
import java.util.List;

public final class SearchViewListFilter {

    private static List<Venue> venueList = new ArrayList<>();

    private SearchViewListFilter() {
    }

    public static List<Venue> getFilteredList(String query, List<Venue> venues) {
        venueList.clear();
        for (Venue venue : venues) {
            if (venue.getName().toLowerCase().contains(query.toLowerCase())) {
                venueList.add(venue);
            }
        }
        return venueList;
    }
}
