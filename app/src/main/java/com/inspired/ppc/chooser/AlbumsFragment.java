package com.inspired.ppc.chooser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.model.AbstractAlbum;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusSupportFragment;

/**
 * A {@link Fragment} to display all available user albums.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

@RequiresPresenter(AlbumsPresenter.class)
public class AlbumsFragment extends NucleusSupportFragment<AlbumsPresenter> implements OnItemClickListener<AbstractAlbum> {

    public static final String TAG_ALBUM_DETAILS = "albumDetails";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, v);

        if (savedInstanceState == null) {
            getPresenter().requestAlbums();
        }
        return v;
    }

    public void onItemsNext(List<AbstractAlbum> items) {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new AlbumsAdapter(getContext(), items, this));
    }

    public void onItemsError(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_load_albums, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View item, AbstractAlbum data) {
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, AlbumDetailsFragment.create(data))
                .addToBackStack(TAG_ALBUM_DETAILS)
                .commit();
    }

    private static class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

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
