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
 * Created by mykhailo.zvonov on 19/10/2016.
 */

public class AlbumsFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        ButterKnife.bind(this, v);

        return v;
    }
}
