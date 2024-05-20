package com.qw.freemusic.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

/**
 * created by QY
 * description:
 */
public class MusicRecyclerView extends RecyclerView {

    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> lAdapter = getAdapter();
            if (lAdapter != null && emptyView != null) {
                if (lAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(VISIBLE);
                    MusicRecyclerView.this.setVisibility(GONE);
                } else {
                    emptyView.setVisibility(GONE);
                    MusicRecyclerView.this.setVisibility(VISIBLE);
                }
            }
        }
    };

    public MusicRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MusicRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    public void setEmptyView(Context pContext, View emptyView, String text, int color) {
        this.emptyView = emptyView;
        ((TextView) emptyView).setText(text);
        MaterialDrawableBuilder lMaterialDrawableBuilder = MaterialDrawableBuilder.with(pContext)
                .setIcon(MaterialDrawableBuilder.IconValue.MUSIC_NOTE)
                .setColor(color)
                .setSizeDp(30);
        ((TextView) emptyView).setCompoundDrawables(null, lMaterialDrawableBuilder.build(), null, null);
    }
}

