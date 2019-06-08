package org.pursuit.usolo.map.categories;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;

import org.pursuit.usolo.R;
import org.pursuit.usolo.model.Category;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

final class CategoryViewHolder extends RecyclerView.ViewHolder {
    private static final String CURRENT_FILTER = "FILTER";

    CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    Disposable onBind(@NonNull final Category category,
                      @NonNull final SharedPreferences.Editor editor,
                      @NonNull final OnCategorySelectListener listener) {
        itemView.<ImageView>findViewById(R.id.chip_icon).setImageResource(category.getIcon());
        itemView.<TextView>findViewById(R.id.chip_category_title).setText(category.getName());
        return RxView.clicks(itemView)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(click -> {
              Log.d("jimenez", "onBind: " + category.getName());
              editor.putString(CURRENT_FILTER, category.getName()).commit();
              listener.onCategorySelected(category.getName());
          });
    }
}