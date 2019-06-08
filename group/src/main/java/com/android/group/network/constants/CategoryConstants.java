package com.android.group.network.constants;

public class CategoryConstants {

    //Nightlife
    public static final String NIGHT_LIFE = "4d4b7105d754a06376d81259";
    public static final String ARCADE = "4bf58dd8d48988d1e1931735";
    public static final String COMEDY_CLUB = "4bf58dd8d48988d18e941735";
    public static final String NIGHTLIFE_COLLECTION = NIGHT_LIFE + "," + ARCADE + "," + COMEDY_CLUB;

    //Eat & Drink
    public static final String FOOD = "4d4b7105d754a06374d81259";
    public static final String BAR = "4bf58dd8d48988d116941735";
    public static final String BREWERY = "50327c8591d4c4b30a586d5d";
    public static final String EAT_AND_DRINK_COLLECTION = FOOD + "," + BAR + "," + BREWERY;

    // Outdoors & Recreation
    public static final String OUTDOORS_AND_REC = "4d4b7105d754a06377d81259";
    public static final String STADIUM = "4bf58dd8d48988d184941735";
    public static final String OUTDOORS_AND_RECREATION_COLLECTION = OUTDOORS_AND_REC + "," + STADIUM;

    //Sight-Seeing
    public static final String ART_GALLERY = "4bf58dd8d48988d1e2931735";
    public static final String HISTORIC_SITE = "4deefb944765f83613cdba6e";
    public static final String MUESUM = "4bf58dd8d48988d181941735";
    public static final String SIGHTSEEING_COLLECTION = ART_GALLERY + "," + HISTORIC_SITE + "," + MUESUM;

    //Firebase group category definitions
    public static final String NIGHTLIFE = "Nightlife";
    public static final String EAT_AND_DRINK = "Eat & Drink";
    public static final String SIGHTSEEING = "Sight-Seeing";
    public static final String OUTDOORS_AND_RECREATION = "Outdoors & Recreation";

    public static final String[] FIREBASE_CATEGORY_VERSION = {NIGHTLIFE, EAT_AND_DRINK, SIGHTSEEING, OUTDOORS_AND_RECREATION};
    public static final String[] NETWORK_CATEGORY_VERSION = {
      NIGHTLIFE_COLLECTION,
      EAT_AND_DRINK_COLLECTION,
      SIGHTSEEING_COLLECTION,
      OUTDOORS_AND_RECREATION_COLLECTION};

}
