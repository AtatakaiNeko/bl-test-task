package com.inspired.ppc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.chooser.OnItemClickListener;
import com.inspired.ppc.model.AbstractAlbum;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An adapter for {@link com.inspired.ppc.chooser.AlbumsFragment}
 * Created by mykhailo.zvonov on 21/10/2016.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private LayoutInflater mInflater;

    private List<AbstractAlbum> mItems;

    private OnItemClickListener<AbstractAlbum> mListener;

    public AlbumsAdapter(Context context, List<AbstractAlbum> albums, OnItemClickListener<AbstractAlbum> listener) {
        mItems = albums;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumViewHolder(mInflater.inflate(R.layout.list_item_album, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.img_album_cover)
        RoundedImageView mImageView;
        @BindView(R.id.text_count)
        TextView mCount;

        private AbstractAlbum mCurrentAlbum;
        private OnItemClickListener<AbstractAlbum> mListener;

        AlbumViewHolder(View itemView, OnItemClickListener<AbstractAlbum> listener) {
            super(itemView);
            mListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(AbstractAlbum album) {
            mCurrentAlbum = album;

            mTitle.setText(album.name);
            mCount.setText(itemView.getContext().getResources().getQuantityString(R.plurals.photo_count, album.getPhotosCount(), album.getPhotosCount()));

            App.getPicasso()
                    .load(album.getCoverPhotoUrl())
                    .into(mImageView);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, mCurrentAlbum);
            }
        }
    }
}
