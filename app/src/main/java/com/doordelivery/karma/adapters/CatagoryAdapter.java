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

import com.doordelivery.karma.Catagories;
import com.doordelivery.karma.R;
import com.doordelivery.karma.ViewItemsActivity;
import com.doordelivery.karma.ViewSubCatsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.CatViewHolder> {

    List<Catagories> catList;
    Context context;


    public CatagoryAdapter(Context context) {
        this.catList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Catagories> newCats){

        int initSize=newCats.size();
        catList.addAll(newCats);
        notifyItemRangeChanged(initSize,newCats.size());

    }

    public String getLastItemId(){
        return catList.get(catList.size()-1).getCatagoryName();
    }


    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.catagory_list_layout,parent,false);

        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {

        holder.tvCatName.setText(catList.get(position).getCatagoryName());
        Picasso.get().load(catList.get(position).getCatagoryImage()).into(holder.ivCatImage);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catList.get(position).isHasSub()) {
                    Intent intent = new Intent(context, ViewSubCatsActivity.class);
                    intent.putExtra("REF_KEY", catList.get(position).getCatagoryName());
                    intent.putExtra("CAT_NAME", catList.get(position).getCatagoryName());;
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
            view=itemView;
            tvCatName=itemView.findViewById(R.id.tv_cat_name);
            ivCatImage=itemView.findViewById(R.id.iv_cat_label);
            constraintLayout=itemView.findViewById(R.id.con_layout);

        }
    }
}
