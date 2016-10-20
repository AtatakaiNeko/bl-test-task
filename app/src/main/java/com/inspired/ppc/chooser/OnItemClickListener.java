package com.inspired.ppc.chooser;

import android.view.View;

/**
 * A listener used in the {@link android.support.v7.widget.RecyclerView} to react on clicks.
 * Created by mykhailo.zvonov on 20/10/2016.
 */

public interface OnItemClickListener<T> {

    void onItemClick(View item, T data);
}
