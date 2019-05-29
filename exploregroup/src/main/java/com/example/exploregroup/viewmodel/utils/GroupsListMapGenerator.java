package com.example.exploregroup.viewmodel.utils;

import org.pursuit.firebasetools.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GroupsListMapGenerator {

    public static final String ACTIVE_GROUPS = "active_groups";
    public static final String PENDING_GROUPS = "pending_groups";

    private static List<Group> activeGroups = new ArrayList<>();
    private static List<Group> pendingGroups = new ArrayList<>();
    private static Map<String, List<Group>> mapOfGroupLists = new HashMap<>();

    private GroupsListMapGenerator() {}

    public static Map<String, List<Group>> getMapOfGroupLists(Group group) {
        if (!mapOfGroupLists.containsKey(ACTIVE_GROUPS)) {
            mapOfGroupLists.put(ACTIVE_GROUPS, activeGroups);
        }
        if (!mapOfGroupLists.containsKey(PENDING_GROUPS)) {
            mapOfGroupLists.put(PENDING_GROUPS, pendingGroups);
        }
        if (group.getUserCount() >= 4) {
            activeGroups.add(group);
        } else {
            pendingGroups.add(group);
        }
        return mapOfGroupLists;
    }

    public static void clear(){
        activeGroups.clear();
        pendingGroups.clear();
        mapOfGroupLists.clear();
    }
}
