package com.inspired.ppc.chooser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inspired.ppc.R;
import com.inspired.ppc.adapter.PhotosAdapter;
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
    public static final int SPAN_COUNT = 3;

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mRecyclerView.setAdapter(new PhotosAdapter(getContext(), photos, this, SPAN_COUNT));
    }

    public void onError(Throwable e) {
        Toast.makeText(getContext(), getString(R.string.error_load_photos, mAlbum.name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View item, Photo data) {
        Intent i = new Intent();
        i.putExtra(ProfilePictureChooserActivity.EXTRA_PHOTO, data);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
    }

}
