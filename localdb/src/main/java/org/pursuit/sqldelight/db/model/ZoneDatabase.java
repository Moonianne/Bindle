package org.pursuit.sqldelight.db.model;

import android.content.Context;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.android.AndroidSqliteDriver;
import com.squareup.sqldelight.db.SqlDriver;

import org.jetbrains.annotations.NotNull;
import org.pursuit.usolo.ZoneModel;
import org.pursuit.usolo.localdb.Database;
import org.pursuit.usolo.localdb.localdb.DatabaseImplKt;

import static kotlin.jvm.JvmClassMappingKt.getKotlinClass;

public final class ZoneDatabase {
    private static Database database;

    public static Database getInstance(Context context) {
        SqlDriver sqliteDriver = new AndroidSqliteDriver(Database.Companion.getSchema(), context, "usolo.db");
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
}
