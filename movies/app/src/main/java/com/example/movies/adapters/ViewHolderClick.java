package com.example.movies.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClick<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final IClickHandlerAdapter<T> handlerClick;
    private final IModelPosition<T> iPosition;
    public ViewHolderClick(@NonNull View itemView,@NonNull IClickHandlerAdapter<T> clickHandlerAdapter,@NonNull IModelPosition<T> position) {
        super(itemView);
        handlerClick = clickHandlerAdapter;
        this.iPosition = position;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        handlerClick.onClick(iPosition.getModelFromPosition(position));
    }

    public interface IModelPosition<T> {
        T getModelFromPosition(int position);
    }
}
