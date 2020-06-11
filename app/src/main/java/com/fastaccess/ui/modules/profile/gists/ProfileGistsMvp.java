package com.fastaccess.ui.modules.profile.gists;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fastaccess.data.dao.model.Gist;
import com.fastaccess.provider.rest.loadmore.OnLoadMore;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 11 Nov 2016, 12:35 PM
 */

interface ProfileGistsMvp {
    interface View extends BaseMvp.FAView, SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener {

        void onNotifyAdapter(@Nullable List<Gist> items, int page);

        @NonNull OnLoadMore<String> getLoadMore();

        void onStartGistView(@NonNull String gistId);
    }

    interface Presenter extends BaseMvp.FAPresenter,
            BaseViewHolder.OnItemClickListener<Gist>,
            BaseMvp.PaginationListener<String> {

        @NonNull ArrayList<Gist> getGists();

        void onWorkOffline(@NonNull String login);
    }
}
