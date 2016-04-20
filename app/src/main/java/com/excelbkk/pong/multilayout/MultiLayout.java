package com.excelbkk.pong.multilayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pong.p on 4/4/2016.
 */
public class MultiLayout extends RelativeLayout {
    private static final String EMPTY = "empty";
    private static final String LOADING = "loading";
    private static final String FAIL = "fail";

    private static final int DEFAULT_RETRY_COLOUR = Color.BLUE;

    private ViewGroup loadingView;
    private ViewGroup emptyView;
    private ViewGroup failView;

    private ImageView imageLoading;

    private String emptyMessage;
    private String loadingMessage;
    private String failMessage;
    private String retryMessage;

    private boolean canRetry;
    private Drawable loadingDrawable;

    private OnRetryListener listener;
    private Drawable retryButtonBackgroundDrawable;
    private int retryButtonBackgroundColour = DEFAULT_RETRY_COLOUR;

    public MultiLayout(Context context) {
        super(context);
        init();
    }

    public MultiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setVisibility(GONE);
    }

    /**
     * Set empty message that display when empty view show
     *
     * @param msg text message
     */
    public void setEmptyMessage(@NonNull String msg) {
        emptyMessage = msg;
    }

    /**
     * Set fail message that display when fail view show
     *
     * @param msg text message
     */
    public void setFailMessage(@NonNull String msg) {
        failMessage = msg;
    }

    /**
     * Set retry button title
     *
     * @param title text message
     */
    public void setRetryButtonTitle(@NonNull String title) {
        retryMessage = title;
    }
    /**
     * Set drawable background to retry button
     *
     * @param drawable drawable background
     */
    public void setRetryButtonBackgroundDrawable(@NonNull Drawable drawable) {
        this.retryButtonBackgroundDrawable = drawable;
    }
    /**
     * Set background color to retry button
     *
     * @param color background color
     */
    public void setRetryButtonBackgroundColor(@NonNull int color) {
        this.retryButtonBackgroundColour = color;
    }

    /**
     * Set loading message that display when loading view show
     *
     * @param msg text message
     */
    public void setLoadingMessage(@NonNull String msg) {
        loadingMessage = msg;
    }

    /**
     * Set loading drawable image that display when loading view show
     *
     * @param drawable image drawable
     */
    public void setLoadingDrawable(Drawable drawable) {
        this.loadingDrawable = drawable;
    }

    /**
     * Show empty view on this layout
     */
    public void showEmpty() {
        switchView(EMPTY);
    }

    /**
     * Show loading view with rotation animation on this layout
     */
    public void showLoading() {
        switchView(LOADING);
    }

    /**
     * Show text fail on this layout
     */
    public void showFail() {
        canRetry = false;
        switchView(FAIL);
    }

    /**
     * Show text fail on this layout with retry button. Not working on custom fail view.
     *
     * @param listener listener will active when press retry button
     */
    public void showFail(@NonNull OnRetryListener listener) {
        this.listener = listener;
        canRetry = true;
        switchView(FAIL);
    }

    private void switchView(String type) {
        removeAllViews();
        this.setVisibility(VISIBLE);
        switch (type) {
            case EMPTY:
                addEmptyView();
                break;
            case LOADING:
                addLoadingView();
                break;
            case FAIL:
                addFailView();
                break;
            default:
                addEmptyView();
                break;
        }
    }

    private void addEmptyView() {
        this.addView(createEmptyView());
    }

    private void addFailView() {
        this.addView(createFailView());
    }

    private void addLoadingView() {
        this.addView(createLoadingView());
    }

    protected ViewGroup createEmptyView() {
        if (emptyView == null) {
            emptyView = initBaseView(R.layout.view_empty_list);
        }

        TextView textView = (TextView) emptyView.findViewById(R.id.text_empty_list);
        setTextToTextView(textView, emptyMessage);

        return emptyView;
    }

    protected ViewGroup createFailView() {
        if (failView == null) {
            failView = initBaseView(R.layout.view_load_fail);
        }

        TextView textView = (TextView) failView.findViewById(R.id.text_fail);
        setTextToTextView(textView, failMessage);

        TextView textRetry = (TextView) failView.findViewById(R.id.text_try_again);
        setTextToTextView(textRetry, retryMessage);

        LinearLayout layout = (LinearLayout) failView.findViewById(R.id.layout_retry);
        if(retryButtonBackgroundDrawable != null) {
            layout.setBackground(retryButtonBackgroundDrawable);
        }else{
            layout.setBackgroundColor(retryButtonBackgroundColour);
        }
        initFailButton(layout);

        return failView;
    }

    protected ViewGroup createLoadingView() {
        if (loadingView == null) {
            loadingView = initBaseView(R.layout.view_loading);
        }

        TextView textView = (TextView) failView.findViewById(R.id.text_loading);
        setTextToTextView(textView, loadingMessage);

        if (imageLoading == null) {
            imageLoading = (ImageView) loadingView.findViewById(R.id.image_loading);
        }
        if (loadingDrawable != null) {
            imageLoading.setImageDrawable(loadingDrawable);
        }
        imageLoading.startAnimation(getRotateAnimation());

        return loadingView;
    }

    private ViewGroup initBaseView(int resId) {
        ViewGroup view = (ViewGroup) View.inflate(getContext(), resId, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    private void setTextToTextView(TextView textView, String message) {
        if (textView != null && message != null) {
            textView.setText(message);
        }
    }

    private void initFailButton(View view) {
        if (canRetry) {
            view.setVisibility(VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRetry();
                    }
                }
            });
        } else {
            view.setVisibility(GONE);
        }
    }

    protected Animation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(1200);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }

    @Override
    public void removeAllViews() {
        if (getChildCount() > 0) {
            if (imageLoading != null && imageLoading.getAnimation() != null) {
                imageLoading.getAnimation().cancel();
            }
            this.setVisibility(GONE);
            super.removeAllViews();
        }
    }
}
