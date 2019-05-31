package org.pursuit.sqldelight.db.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import org.jetbrains.annotations.NotNull;
import org.pursuit.usolo.MessageModelQueries;
import org.pursuit.usolo.ZoneModel;
import org.pursuit.usolo.ZoneModelQueries;
import org.pursuit.usolo.localdb.Database;
import org.pursuit.usolo.localdb.localdb.DatabaseImplKt;

import static kotlin.jvm.JvmClassMappingKt.getKotlinClass;

public final class BindleDatabase {
    private static BindleDatabase instance;
    private static Database database;
    private static ZoneModelQueries zoneModelQueries;
    private static MessageModelQueries messageModelQueries;

    private BindleDatabase(@NonNull Context context) {
        database = getDatabase(context);
        zoneModelQueries = database.getZoneModelQueries();
        messageModelQueries = database.getMessageModelQueries();
    }

    public static BindleDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new BindleDatabase(context);
        }
        return instance;
    }

    private Database getDatabase(@NonNull Context context) {
        SqlDriver sqliteDriver =
          new AndroidSqliteDriver(Database.Companion.getSchema(), context, "usolo.db");
        if (database == null) {
            ColumnAdapter<LatLng, String> columnAdapter = new ColumnAdapter<LatLng, String>() {
                @NotNull
                @Override
                public LatLng decode(String s) {
                    final String[] split = s.split(":");
                    return new LatLng(Double.valueOf(split[0]),
                      Double.valueOf(split[1]),
                      Double.valueOf(split[2]));
                }

                @Override
                public String encode(@NotNull LatLng latLng) {
                    return latLng.getLatitude() + ":" + latLng.getLongitude() + ":" + latLng.getAltitude();
                }
            };
            database = DatabaseImplKt.newInstance(
              getKotlinClass(Database.class),
              sqliteDriver,
              new ZoneModel.Adapter(columnAdapter)
            );
        }
        return database;
    }

    public final ZoneModelQueries getZoneModelQueries() {
        return zoneModelQueries;
    }

    public final  MessageModelQueries getMessageModelQueries() {
        return messageModelQueries;
    }
}
