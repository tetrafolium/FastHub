package com.fastaccess.ui.adapter.viewholder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fastaccess.R;
import com.fastaccess.data.dao.ReactionsModel;
import com.fastaccess.data.dao.ReviewCommentModel;
import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.ParseDateFormat;
import com.fastaccess.helper.ViewHelper;
import com.fastaccess.provider.scheme.LinkParserHelper;
import com.fastaccess.provider.timeline.CommentsHelper;
import com.fastaccess.provider.timeline.HtmlHelper;
import com.fastaccess.provider.timeline.handler.drawable.DrawableGetter;
import com.fastaccess.ui.adapter.callback.OnToggleView;
import com.fastaccess.ui.adapter.callback.ReactionsCallback;
import com.fastaccess.ui.widgets.AvatarLayout;
import com.fastaccess.ui.widgets.FontTextView;
import com.fastaccess.ui.widgets.recyclerview.BaseRecyclerAdapter;
import com.fastaccess.ui.widgets.recyclerview.BaseViewHolder;

import butterknife.BindView;

public class ReviewCommentsViewHolder extends BaseViewHolder<ReviewCommentModel> {

@BindView(R.id.avatarView) AvatarLayout avatarView;
@BindView(R.id.name) FontTextView name;
@BindView(R.id.date) FontTextView date;
@BindView(R.id.comment) FontTextView comment;
@BindView(R.id.toggle) AppCompatImageView toggle;
@BindView(R.id.toggleHolder) LinearLayout toggleHolder;
@BindView(R.id.thumbsUp) FontTextView thumbsUp;
@BindView(R.id.thumbsDown) FontTextView thumbsDown;
@BindView(R.id.laugh) FontTextView laugh;
@BindView(R.id.hurray) FontTextView hurray;
@BindView(R.id.sad) FontTextView sad;
@BindView(R.id.heart) FontTextView heart;
@BindView(R.id.commentMenu) ImageView commentMenu;
@BindView(R.id.commentOptions) RelativeLayout commentOptions;
@BindView(R.id.owner) FontTextView owner;
@BindView(R.id.reactionsList) View reactionsList;
@BindView(R.id.thumbsUpReaction) FontTextView thumbsUpReaction;
@BindView(R.id.thumbsDownReaction) FontTextView thumbsDownReaction;
@BindView(R.id.laughReaction) FontTextView laughReaction;
@BindView(R.id.hurrayReaction) FontTextView hurrayReaction;
@BindView(R.id.sadReaction) FontTextView sadReaction;
@BindView(R.id.heartReaction) FontTextView heartReaction;
@BindView(R.id.rocketReaction) FontTextView rocketReaction;
@BindView(R.id.eyeReaction) FontTextView eyeReaction;
@BindView(R.id.rocket) FontTextView rocket;
@BindView(R.id.eyes) FontTextView eyes;
private OnToggleView onToggleView;
private ReactionsCallback reactionsCallback;
private ViewGroup viewGroup;
private String repoOwner;
private String poster;

@Override public void onClick(final View v) {
	if (v.getId() == R.id.toggle || v.getId() == R.id.toggleHolder) {
		if (onToggleView != null) {
			long id = getId();
			onToggleView.onToggle(id, !onToggleView.isCollapsed(id));
			onToggle(onToggleView.isCollapsed(id), true);
		}
	} else {
		addReactionCount(v);
		super.onClick(v);
	}
}

private ReviewCommentsViewHolder(final @NonNull View itemView, final ViewGroup viewGroup, final @Nullable BaseRecyclerAdapter adapter,
                                 final @NonNull OnToggleView onToggleView, final @NonNull ReactionsCallback reactionsCallback,
                                 final String repoOwner, final String poster) {
	super(itemView, adapter);
	if (adapter != null && adapter.getRowWidth() == 0) {
		itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				@Override public boolean onPreDraw() {
				        itemView.getViewTreeObserver().removeOnPreDrawListener(this);
				        adapter.setRowWidth(itemView.getWidth() - ViewHelper.dpToPx(itemView.getContext(), 48));
				        return false;
				}
			});
	}
	this.onToggleView = onToggleView;
	this.viewGroup = viewGroup;
	this.reactionsCallback = reactionsCallback;
	this.repoOwner = repoOwner;
	this.poster = poster;
	itemView.setOnClickListener(null);
	itemView.setOnLongClickListener(null);
	toggle.setOnClickListener(this);
	commentMenu.setOnClickListener(this);
	toggleHolder.setOnClickListener(this);
	laugh.setOnClickListener(this);
	sad.setOnClickListener(this);
	thumbsDown.setOnClickListener(this);
	thumbsUp.setOnClickListener(this);
	hurray.setOnClickListener(this);
	laugh.setOnLongClickListener(this);
	sad.setOnLongClickListener(this);
	thumbsDown.setOnLongClickListener(this);
	thumbsUp.setOnLongClickListener(this);
	hurray.setOnLongClickListener(this);
	heart.setOnLongClickListener(this);
	heart.setOnClickListener(this);
	laughReaction.setOnClickListener(this);
	sadReaction.setOnClickListener(this);
	thumbsDownReaction.setOnClickListener(this);
	thumbsUpReaction.setOnClickListener(this);
	hurrayReaction.setOnClickListener(this);
	heartReaction.setOnClickListener(this);
	laughReaction.setOnLongClickListener(this);
	sadReaction.setOnLongClickListener(this);
	thumbsDownReaction.setOnLongClickListener(this);
	thumbsUpReaction.setOnLongClickListener(this);
	hurrayReaction.setOnLongClickListener(this);
	heartReaction.setOnLongClickListener(this);
	rocketReaction.setOnLongClickListener(this);
	rocketReaction.setOnClickListener(this);
	rocket.setOnLongClickListener(this);
	rocket.setOnClickListener(this);
	eyeReaction.setOnLongClickListener(this);
	eyeReaction.setOnClickListener(this);
	eyes.setOnLongClickListener(this);
	eyes.setOnClickListener(this);
}

public static ReviewCommentsViewHolder newInstance(final ViewGroup viewGroup, final BaseRecyclerAdapter adapter,
                                                   final @NonNull OnToggleView onToggleView, final @NonNull ReactionsCallback reactionsCallback,
                                                   final String repoOwner, final String poster) {
	return new ReviewCommentsViewHolder(getView(viewGroup, R.layout.review_comments_row_item),
	                                    viewGroup, adapter, onToggleView, reactionsCallback, repoOwner, poster);
}

@Override public void bind(final @NonNull ReviewCommentModel commentModel) {
	if (commentModel.getUser() != null) {
		avatarView.setUrl(commentModel.getUser().getAvatarUrl(), commentModel.getUser().getLogin(), commentModel.getUser()
		                  .isOrganizationType(), LinkParserHelper.isEnterprise(commentModel.getHtmlUrl()));
		name.setText(commentModel.getUser().getLogin());
		if (commentModel.getAuthorAssociation() != null && !"none".equalsIgnoreCase(commentModel.getAuthorAssociation())) {
			owner.setText(commentModel.getAuthorAssociation().toLowerCase());
			owner.setVisibility(View.VISIBLE);
		} else {
			boolean isRepoOwner = TextUtils.equals(commentModel.getUser().getLogin(), repoOwner);
			if (isRepoOwner) {
				owner.setVisibility(View.VISIBLE);
				owner.setText(R.string.owner);
			} else {
				boolean isPoster = TextUtils.equals(commentModel.getUser().getLogin(), poster);
				if (isPoster) {
					owner.setVisibility(View.VISIBLE);
					owner.setText(R.string.original_poster);
				} else {
					owner.setText("");
					owner.setVisibility(View.GONE);
				}
			}
		}
	}
	date.setText(ParseDateFormat.getTimeAgo(commentModel.getCreatedAt()));
	if (!InputHelper.isEmpty(commentModel.getBodyHtml())) {
		int width = adapter != null ? adapter.getRowWidth() : 0;
		HtmlHelper.htmlIntoTextView(comment, commentModel.getBodyHtml(), width > 0 ? width : viewGroup.getWidth());
	} else {
		comment.setText("");
	}
	if (commentModel.getReactions() != null) {
		ReactionsModel reaction = commentModel.getReactions();
		appendEmojies(reaction);
	}
	if (onToggleView != null) onToggle(onToggleView.isCollapsed(getId()), false);
}

private void addReactionCount(final View v) {
	if (adapter != null) {
		ReviewCommentModel comment = (ReviewCommentModel) adapter.getItem(getAdapterPosition());
		if (comment != null) {
			boolean isReacted = reactionsCallback == null || reactionsCallback.isPreviouslyReacted(comment.getId(), v.getId());
			ReactionsModel reactionsModel = comment.getReactions() != null ? comment.getReactions() : new ReactionsModel();
			switch (v.getId()) {
			case R.id.heart:
			case R.id.heartReaction:
				reactionsModel.setHeart(!isReacted ? reactionsModel.getHeart() + 1 : reactionsModel.getHeart() - 1);
				break;
			case R.id.sad:
			case R.id.sadReaction:
				reactionsModel.setConfused(!isReacted ? reactionsModel.getConfused() + 1 : reactionsModel.getConfused() - 1);
				break;
			case R.id.thumbsDown:
			case R.id.thumbsDownReaction:
				reactionsModel.setMinusOne(!isReacted ? reactionsModel.getMinusOne() + 1 : reactionsModel.getMinusOne() - 1);
				break;
			case R.id.thumbsUp:
			case R.id.thumbsUpReaction:
				reactionsModel.setPlusOne(!isReacted ? reactionsModel.getPlusOne() + 1 : reactionsModel.getPlusOne() - 1);
				break;
			case R.id.laugh:
			case R.id.laughReaction:
				reactionsModel.setLaugh(!isReacted ? reactionsModel.getLaugh() + 1 : reactionsModel.getLaugh() - 1);
				break;
			case R.id.hurray:
			case R.id.hurrayReaction:
				reactionsModel.setHooray(!isReacted ? reactionsModel.getHooray() + 1 : reactionsModel.getHooray() - 1);
				break;
			case R.id.rocket:
			case R.id.rocketReaction:
				reactionsModel.setRocket(!isReacted ? reactionsModel.getRocket() + 1 : reactionsModel.getRocket() - 1);
				break;
			case R.id.eyes:
			case R.id.eyeReaction:
				reactionsModel.setEyes(!isReacted ? reactionsModel.getEyes() + 1 : reactionsModel.getEyes() - 1);
				break;
			}
			comment.setReactions(reactionsModel);
			appendEmojies(reactionsModel);
		}
	}
}

private void appendEmojies(final ReactionsModel reaction) {
	CommentsHelper.appendEmojies(reaction, thumbsUp, thumbsUpReaction, thumbsDown, thumbsDownReaction, hurray, hurrayReaction, sad,
	                             sadReaction, laugh, laughReaction, heart, heartReaction, rocket, rocketReaction, eyes, eyeReaction, reactionsList);
}

private long getId() {
	if (adapter != null) {
		ReviewCommentModel comment = (ReviewCommentModel) adapter.getItem(getAdapterPosition());
		return comment.getId();
	}
	return -1;
}

private void onToggle(final boolean expanded, final boolean animate) {
	if (animate) {
		TransitionManager.beginDelayedTransition(viewGroup, new ChangeBounds());
	}
	toggle.setRotation(!expanded ? 0.0F : 180F);
	commentOptions.setVisibility(!expanded ? View.GONE : View.VISIBLE);
	reactionsList.setVisibility(expanded ? View.GONE : View.VISIBLE);
	reactionsList.setVisibility(expanded ? View.GONE : reactionsList.getTag() == null || (!((Boolean) reactionsList.getTag()))
	                            ? View.GONE : View.VISIBLE);
}

@Override protected void onViewIsDetaching() {
	DrawableGetter drawableGetter = (DrawableGetter) comment.getTag(R.id.drawable_callback);
	if (drawableGetter != null) {
		drawableGetter.clear(drawableGetter);
	}
}
}
