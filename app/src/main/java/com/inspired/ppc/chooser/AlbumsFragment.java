package com.inspired.ppc.chooser;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspired.ppc.App;
import com.inspired.ppc.R;
import com.inspired.ppc.model.AbstractAlbum;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

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
public class AlbumsFragment extends NucleusSupportFragment<AlbumsPresenter> {
    private static final Transformation ROUNDED_TRANSFORMATION = new RoundedTransformationBuilder()
            .borderColor(Color.TRANSPARENT)
            .borderWidthDp(1)
            .cornerRadiusDp(30)
            .oval(false)
            .build();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, v);

        getPresenter().requestAlbums();
        return v;
    }

    public void onItemsNext(List<AbstractAlbum> items) {
        for (AbstractAlbum album : items) {
            Log.d("TAG", "Album: " + album.name + " Count: " + album.getPhotosCount());
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new AlbumsAdapter(getContext(), items));
    }

    public void onItemsError(Throwable throwable) {
        Log.e("TAG", "Error", throwable);
    }

    private static class AlbumsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

        private LayoutInflater mInflater;

        private List<AbstractAlbum> mItems;

        public AlbumsAdapter(Context context, List<AbstractAlbum> albums) {
            mItems = albums;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlbumViewHolder(mInflater.inflate(R.layout.list_item_album, parent, false));
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

    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.img_album_cover)
        ImageView mImageView;
        @BindView(R.id.text_count)
        TextView mCount;

        private AbstractAlbum mCurrentAlbum;

        public AlbumViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(AbstractAlbum album) {
            mCurrentAlbum = album;

            mTitle.setText(album.name);
            mCount.setText(itemView.getContext().getResources().getQuantityString(R.plurals.photo_count, album.getPhotosCount(), album.getPhotosCount()));

            App.getPicasso()
                    .load(album.getCoverPhotoUrl())
                    .fit()
                    .transform(ROUNDED_TRANSFORMATION)
                    .into(mImageView);
        }
    }
}
