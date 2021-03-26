package com.doordelivery.karma.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.doordelivery.karma.R;
import com.doordelivery.karma.activities.ViewItemsActivity;
import com.doordelivery.karma.domains.Brands;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.CatViewHolder> {

    List<Brands> catList;
    Context context;


    public CatagoryAdapter(Context context) {
        this.catList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Brands> newCats) {

        int initSize = newCats.size();
        catList.addAll(newCats);
        notifyItemRangeChanged(initSize, newCats.size());

    }

    public String getLastItemId() {
        return catList.get(catList.size() - 1).getBrandName();
    }


    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.catagory_list_layout, parent, false);

        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {

        holder.tvCatName.setText(catList.get(position).getBrandName());
        switch (catList.get(position).getBrandName()) {
            case "Apple":
                Picasso.get().load(R.drawable.apple).into(holder.ivCatImage);
                break;
            case "Asus":
                Picasso.get().load(R.drawable.asus).into(holder.ivCatImage);
                break;
            case "Google Pixel":
                Picasso.get().load(R.drawable.google_pixels).into(holder.ivCatImage);
                break;
            case "HTC":
                Picasso.get().load(R.drawable.htc).into(holder.ivCatImage);
                break;
            case "Huawei":
                Picasso.get().load(R.drawable.huawei).into(holder.ivCatImage);
                break;
            case "LG":
                Picasso.get().load(R.drawable.lg).into(holder.ivCatImage);
                break;
            case "Lenovo":
                Picasso.get().load(R.drawable.lenovo).into(holder.ivCatImage);
                break;
            case "Motorola":
                Picasso.get().load(R.drawable.motorola).into(holder.ivCatImage);
                break;
            case "Nokia":
                Picasso.get().load(R.drawable.nokia).into(holder.ivCatImage);
                break;
            case "OnePlus":
                Picasso.get().load(R.drawable.oneplus).into(holder.ivCatImage);
                break;
            case "Oppo":
                Picasso.get().load(R.drawable.oppo).into(holder.ivCatImage);
                break;
            case "Panasonic":
                Picasso.get().load(R.drawable.panasonic).into(holder.ivCatImage);
                break;
            case "Samsung":
                Picasso.get().load(R.drawable.samsung).into(holder.ivCatImage);
                break;
            case "Sony":
                Picasso.get().load(R.drawable.sony).into(holder.ivCatImage);
                break;

            case "Vivo":
                Picasso.get().load(R.drawable.vivo).into(holder.ivCatImage);
                break;
            case "Xiaomi":
                Picasso.get().load(R.drawable.xiaomi).into(holder.ivCatImage);
                break;
            case "ZTE":
                Picasso.get().load(R.drawable.zte).into(holder.ivCatImage);
                break;
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewItemsActivity.class);
                intent.putExtra("REF_KEY", catList.get(position).getBrandName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                /*
                if(catList.get(position).isHasSub()) {
                    Intent intent = new Intent(context, ViewSubCatsActivity.class);
                    intent.putExtra("REF_KEY", catList.get(position).getCatagoryName());
                    intent.putExtra("CAT_NAME", catList.get(position).getCatagoryName());;
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else {
                    if (catList.get(position).getCatagoryName().equals("Online Courses")){
                        Intent intent = new Intent(context, OnlineCourseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, ViewItemsActivity.class);
                        intent.putExtra("REF_KEY", catList.get(position).getCatagoryName());
                        intent.putExtra("CAT_NAME", catList.get(position).getCatagoryName());
                        intent.putExtra("MAIN_CAT_NAME", catList.get(position).getCatagoryName());
                        intent.putExtra("hasSub", false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                }

                 */
            }
        });



    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class CatViewHolder extends RecyclerView.ViewHolder {

        TextView tvCatName;
        ImageView ivCatImage;
        ConstraintLayout constraintLayout;
        View view;

        public CatViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvCatName = itemView.findViewById(R.id.tv_cat_name);
            ivCatImage = itemView.findViewById(R.id.iv_cat_label);
            constraintLayout = itemView.findViewById(R.id.con_layout);

        }
    }
}
