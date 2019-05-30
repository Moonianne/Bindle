package com.android.group.view.utils;

import com.android.group.model.bindle.BindleBusiness;

import java.util.ArrayList;
import java.util.List;

public final class SearchViewListFilter {

    private static List<BindleBusiness> bindleBusinessList = new ArrayList<>();

    private SearchViewListFilter() {
    }

    public static List<BindleBusiness> getFilteredList(String query, List<BindleBusiness> bindleBusinesses) {
        bindleBusinessList.clear();
        for (BindleBusiness bindleBusiness : bindleBusinesses) {
            if (bindleBusiness.getVenue().getName().toLowerCase().contains(query.toLowerCase())) {
                bindleBusinessList.add(bindleBusiness);
            }
        }
        return bindleBusinessList;
    }
}
