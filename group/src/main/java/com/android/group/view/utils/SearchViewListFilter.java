package com.android.group.view.utils;

import com.android.group.model.bindle.BindleBusiness;

import java.util.ArrayList;
import java.util.List;

public final class SearchViewListFilter {

    private static List<BindleBusiness> venueList = new ArrayList<>();

    private SearchViewListFilter() {
    }

    public static List<BindleBusiness> getFilteredList(String query, List<BindleBusiness> bindleBusinesses) {
        venueList.clear();
        for (BindleBusiness bindleBusiness : bindleBusinesses) {
            if (bindleBusiness.getVenue().getName().toLowerCase().contains(query.toLowerCase())) {
                venueList.add(bindleBusiness);
            }
        }
        return venueList;
    }
}
