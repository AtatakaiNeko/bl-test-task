package com.inspired.ppc.chooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inspired.ppc.R;
import com.inspired.ppc.adapter.AlbumsAdapter;
import com.inspired.ppc.model.AbstractAlbum;

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


}
