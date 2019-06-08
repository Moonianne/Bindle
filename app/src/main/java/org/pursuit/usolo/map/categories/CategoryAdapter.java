package org.pursuit.usolo.map.categories;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.usolo.R;
import org.pursuit.usolo.model.Category;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public final class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private final CompositeDisposable disposables;
    private final List<Category> categories;
    private final SharedPreferences.Editor editor;
    private final OnCategorySelectListener listener;

    public CategoryAdapter(@NonNull final List<Category> categories,
                           @NonNull final SharedPreferences.Editor editor,
                           @NonNull final OnCategorySelectListener listener) {
        disposables = new CompositeDisposable();
        this.categories = categories;
        this.editor = editor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext())
          .inflate(R.layout.chip_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int pos) {
        disposables.add(categoryViewHolder.onBind(categories.get(pos), editor, listener));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        disposables.dispose();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}