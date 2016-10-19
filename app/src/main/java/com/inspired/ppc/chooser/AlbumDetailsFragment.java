package com.inspired.ppc.chooser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inspired.ppc.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A screen to display content of the specific album and "Photos of me" as well.
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class AlbumDetailsFragment extends Fragment {

    private static final String ARG_ALBUM_ID = "album_id";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String mAlbumId;

    public static AlbumDetailsFragment create(String albumId) {
        Bundle args = new Bundle();
        args.putString(ARG_ALBUM_ID, albumId);

        AlbumDetailsFragment f = new AlbumDetailsFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAlbumId = getArguments().getString(ARG_ALBUM_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, v);

        return v;
    }
}
