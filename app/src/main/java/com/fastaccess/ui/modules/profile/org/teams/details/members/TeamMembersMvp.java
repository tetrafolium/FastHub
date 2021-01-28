
package com.fastaccess.ui.modules.profile.org.teams.details.members;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fastaccess.data.dao.model.User;
import com.fastaccess.provider.rest.loadmore.OnLoadMore;
import com.fastaccess.ui.base.mvp.BaseMvp;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 03 Dec 2016, 3:45 PM
 */

interface TeamMembersMvp {

    interface View extends BaseMvp.FAView, SwipeRefreshLayout.OnRefreshListener, android.view.View.OnClickListener {
        void onNotifyAdapter(@Nullable List<User> items, int page);

        @NonNull OnLoadMore<Long> getLoadMore();
    }

    interface Presenter extends BaseMvp.FAPresenter,
        BaseViewHolder.OnItemClickListener<User>,
        BaseMvp.PaginationListener<Long> {

        @NonNull ArrayList<User> getFollowers();

        void onWorkOffline(@NonNull String login);
    }
}
