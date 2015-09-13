package com.danielme.android.recyclerview.endless;

import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.danielme.android.recyclerview.endless.R;

import java.util.List;

/**
 * @author danielme.com
 */
public class MaterialPaletteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    private static final int TYPE_COLOR = 0;
    private static final int TYPE_FOOTER = 1;

    public MaterialPaletteAdapter(@NonNull List<Item> data, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Color) {
            return TYPE_COLOR;
        } else if (data.get(position) instanceof Footer) {
            return TYPE_FOOTER;
        } else {
            throw new RuntimeException("ItemViewType unknown");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_COLOR) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            PaletteViewHolder pvh = new PaletteViewHolder(row, recyclerViewOnItemClickListener);
            return pvh;
        } else {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(row);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PaletteViewHolder) {
            Color color = (Color) data.get(position);

            PaletteViewHolder paletteViewHolder = (PaletteViewHolder) holder;
            paletteViewHolder.getTitleTextView().setText(color.getName());
            paletteViewHolder.getSubtitleTextView().setText(color.getHex());

            GradientDrawable gradientDrawable = (GradientDrawable) paletteViewHolder.getCircleView().getBackground();
            int colorId = android.graphics.Color.parseColor(color.getHex());
            gradientDrawable.setColor(colorId);
        }
        //FOOTER: nothing to do

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class PaletteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private View circleView;
        private TextView titleTextView;
        private TextView subtitleTextView;

        private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;


        public PaletteViewHolder(View itemView, RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
            super(itemView);
            circleView = itemView.findViewById(R.id.circleView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            subtitleTextView = (TextView) itemView.findViewById(R.id.subtitleTextView);
            this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
            itemView.setOnClickListener(this);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getSubtitleTextView() {
            return subtitleTextView;
        }

        public View getCircleView() {
            return circleView;
        }

        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar getProgressBar() {
            return progressBar;
        }

        private ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.footer);
        }
    }

}