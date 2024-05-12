package com.example.kozmulejelento.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kozmulejelento.R;
import com.example.kozmulejelento.model.VizOraJelentes;

import java.util.ArrayList;

public class VizOraAdapter extends RecyclerView.Adapter<VizOraAdapter.ViewHolder> {
    private ArrayList<VizOraJelentes> mData;
    private Context mContext;
    private int lastPosition = -1;

    public VizOraAdapter(Context context, ArrayList<VizOraJelentes> data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public VizOraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.beagyazott_lista, parent, false));
    }

    @Override
    public void onBindViewHolder(VizOraAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VizOraJelentes currentItem = mData.get(position);
        holder.bindTo(currentItem);

        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mOraAzonositoText;
        private TextView mOraAllasText;
        private TextView mDatumText;

        ViewHolder(View itemView) {
            super(itemView);
            mOraAzonositoText = itemView.findViewById(R.id.listOraAzonosito);
            mOraAllasText = itemView.findViewById(R.id.listOraAllas);
            mDatumText = itemView.findViewById(R.id.listDatum);
        }

        void bindTo(VizOraJelentes currentItem) {
            mOraAzonositoText.setText(currentItem.getOraAzonosito());
            mOraAllasText.setText(currentItem.getOraAllas());
            mDatumText.setText(currentItem.getDatum());
        }
    }
}
