package org.pursuit.usolo.map.categories;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.pursuit.usolo.R;
import org.pursuit.usolo.model.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryTitle;
    private CardView categoryCard;
    private ImageView categoryIcon;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryCard = itemView.findViewById(R.id.chip_cardview);
        categoryIcon = itemView.findViewById(R.id.chip_icon);
        categoryTitle = itemView.findViewById(R.id.chip_category_title);
    }

    public void onBind(Category category) {

        categoryIcon.setImageResource(category.getIcon());
        categoryTitle.setText(category.getName());
        categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
