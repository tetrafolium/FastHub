package com.fastaccess.ui.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.ViewGroup;

import com.fastaccess.R;
import com.fastaccess.data.dao.GroupedNotificationModel;
import com.fastaccess.data.dao.model.Notification;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.ForegroundImageView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 */

public class NotificationsViewHolder extends BaseViewHolder<GroupedNotificationModel> {

    @BindView(R.id.title) FontTextView title;
    @BindView(R.id.date) FontTextView date;
    @BindView(R.id.markAsRead) ForegroundImageView markAsRead;
    @BindView(R.id.notificationType) ForegroundImageView notificationType;
    @BindView(R.id.repoName) FontTextView repoName;
    private boolean showUnreadState;

    private NotificationsViewHolder(final @NonNull View itemView, final @Nullable BaseRecyclerAdapter adapter, final boolean showUnreadState) {
        super(itemView, adapter);
        markAsRead.setOnClickListener(this);
        this.showUnreadState = showUnreadState;
    }

    public static NotificationsViewHolder newInstance(final @NonNull ViewGroup viewGroup, final @Nullable BaseRecyclerAdapter adapter, final boolean showUnreadState) {
        return new NotificationsViewHolder(getView(viewGroup, R.layout.notifications_row_item), adapter, showUnreadState);
    }

    @Override public void bind(final @NonNull GroupedNotificationModel model) {
        Notification thread = model.getNotification();
        if (thread != null && thread.getSubject() != null) {
            title.setText(thread.getSubject().getTitle());
            int cardBackground = ViewHelper.getCardBackground(itemView.getContext());
            int color;
            date.setText(ParseDateFormat.getTimeAgo(thread.getUpdatedAt()));
            markAsRead.setVisibility(thread.isUnread() ? View.VISIBLE : View.GONE);
            if (thread.getSubject().getType() != null) {
                notificationType.setImageResource(thread.getSubject().getType().getDrawableRes());
                notificationType.setContentDescription(thread.getSubject().getType().name());
            } else {
                notificationType.setImageResource(R.drawable.ic_info_outline);
            }
            if (showUnreadState) {
                repoName.setVisibility(View.GONE);
                if (AppHelper.isNightMode(itemView.getResources())) {
                    color = ContextCompat.getColor(itemView.getContext(), R.color.material_blue_grey_800);
                } else {
                    color = ContextCompat.getColor(itemView.getContext(), R.color.material_blue_grey_200);
                }
                ((CardView) itemView).setCardBackgroundColor(thread.isUnread() ? color : cardBackground);
            } else {
                repoName.setVisibility(View.VISIBLE);
                repoName.setText(thread.getRepository().getFullName());
            }
        }
    }
}
