package com.inspired.ppc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.chooser.OnItemClickListener;
import com.inspired.ppc.model.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An adapter for {@link com.inspired.ppc.chooser.AlbumDetailsFragment}.
 * Created by mykhailo.zvonov on 21/10/2016.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private LayoutInflater mInflater;
    private List<Photo> mItems;

    private OnItemClickListener<Photo> mListener;

    private final int mViewWidth;

    public PhotosAdapter(Context context, List<Photo> photos, OnItemClickListener<Photo> listener, int spanCount) {
        mInflater = LayoutInflater.from(context);
        mItems = photos;
        mListener = listener;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mViewWidth = dm.widthPixels / spanCount;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(mInflater.inflate(R.layout.list_item_photo, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(mItems.get(position), mViewWidth);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_photo)
        ImageView mPhotoImage;

        private Photo mCurrentPhoto;
        private OnItemClickListener<Photo> mListener;

        public PhotoViewHolder(View itemView, OnItemClickListener<Photo> listener) {
            super(itemView);
            mListener = listener;

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Photo photo, int width) {
            mCurrentPhoto = photo;

            App.getPicasso()
                    .load(photo.getImageUrlFor(width))
                    .into(mPhotoImage);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, mCurrentPhoto);
            }
        }
    }
}
