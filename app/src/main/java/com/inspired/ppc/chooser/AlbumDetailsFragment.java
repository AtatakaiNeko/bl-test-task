package com.inspired.ppc.chooser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.model.AbstractAlbum;
import com.inspired.ppc.model.Photo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

/**
 * A screen to display content of the specific album and "Photos of me" as well.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

@RequiresPresenter(AlbumDetailsPresenter.class)
public class AlbumDetailsFragment extends NucleusSupportFragment<AlbumDetailsPresenter> implements OnItemClickListener<Photo> {

    private static final String ARG_ALBUM = "album";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private AbstractAlbum mAlbum;

    public static AlbumDetailsFragment create(AbstractAlbum album) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);

        AlbumDetailsFragment f = new AlbumDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbum = getArguments().getParcelable(ARG_ALBUM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, v);

        if (savedInstanceState == null) {
            getPresenter().requestAlbum(mAlbum);
        }
        return v;
    }

    public void onItemsNext(List<Photo> photos) {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(new PhotosAdapter(getContext(), photos, this));
    }

    public void onError(Throwable e) {

    }

    @Override
    public void onItemClick(View item, Photo data) {

    }

    static class PhotosAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

        private LayoutInflater mInflater;
        private List<Photo> mItems;

        private OnItemClickListener<Photo> mListener;

        public PhotosAdapter(Context context, List<Photo> photos, OnItemClickListener<Photo> listener) {
            mInflater = LayoutInflater.from(context);
            mItems = photos;
            mListener = listener;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotoViewHolder(mInflater.inflate(R.layout.list_item_photo, parent, false), mListener);
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            holder.bind(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems != null ? mItems.size() : 0;
        }
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

        public void bind(Photo photo) {
            mCurrentPhoto = photo;

            App.getPicasso()
                    .load(photo.picture)
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
