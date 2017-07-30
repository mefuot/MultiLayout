package com.excelbkk.pong.multilayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
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

    private static final float DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_RETRY_BG_COLOUR = Color.BLUE;
    private static final int DEFAULT_TEXT_COLOR = Color.DKGRAY;
    private static final int DEFAULT_BUTTON_TEXT_COLOR = Color.WHITE;

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
    private int retryButtonBackgroundColour = DEFAULT_RETRY_BG_COLOUR;

    private TextOption messageOption;
    private TextOption buttonTextOption;

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
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setVisibility(GONE);
        messageOption = new TextOption(16, Color.DKGRAY);
        buttonTextOption = new TextOption(16, Color.WHITE);
    }

    /**
     * Set empty message that display when empty view show
     *
     * @param msg text message
     */
    public void setEmptyMessage(@NonNull String msg) {
        setEmptyMessage(msg, null);
        resetMessageTextOption();
    }

    /**
     * Set empty message with option(color,size) that display when empty view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setEmptyMessage(@NonNull String msg, TextOption option) {
        emptyMessage = msg;
        transferTextOption(messageOption, option);
    }

    /**
     * Set fail message that display when fail view show
     *
     * @param msg text message
     */
    public void setFailMessage(@NonNull String msg) {
        setFailMessage(msg, null);
        resetMessageTextOption();
    }

    /**
     * Set fail message with option(color,size) that display when fail view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setFailMessage(@NonNull String msg, TextOption option) {
        failMessage = msg;
        transferTextOption(messageOption, option);
    }

    /**
     * Set loading message that display when loading view show
     *
     * @param msg text message
     */
    public void setLoadingMessage(@NonNull String msg) {
        setLoadingMessage(msg, null);
        resetMessageTextOption();
    }

    /**
     * Set loading message with option(color,size) that display when loading view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setLoadingMessage(@NonNull String msg, TextOption option) {
        loadingMessage = msg;
        transferTextOption(messageOption, option);
    }

    /**
     * Set retry button title
     *
     * @param title text message
     */
    public void setRetryButtonTitle(@NonNull String title) {
        setRetryButtonTitle(title, null);
        resetButtonTextOption();
    }

    /**
     * Set retry button title with option(color,size)
     *
     * @param title  text message
     * @param option text option
     */
    public void setRetryButtonTitle(@NonNull String title, TextOption option) {
        retryMessage = title;
        transferTextOption(buttonTextOption, option);
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
    public void setRetryButtonBackgroundColor(int color) {
        this.retryButtonBackgroundColour = color;
    }

    /**
     * Set loading drawable image that display when loading view show
     *
     * @param drawable image drawable
     */
    public void setLoadingDrawable(@NonNull Drawable drawable) {
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
                this.addView(createEmptyView());
                break;
            case LOADING:
                this.addView(createLoadingView());
                break;
            case FAIL:
                this.addView(createFailView());
                break;
            default:
                this.addView(createEmptyView());
                break;
        }
    }

    private ViewGroup initBaseView(int resId) {
        ViewGroup view = (ViewGroup) View.inflate(getContext(), resId, null);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    protected ViewGroup createEmptyView() {
        if (emptyView == null) {
            emptyView = initBaseView(R.layout.view_empty_list);
        }

        TextView textView = (TextView) emptyView.findViewById(R.id.text_empty_list);
        setTextToTextView(textView, emptyMessage);
        setTextOption(textView, messageOption);

        return emptyView;
    }

    protected ViewGroup createFailView() {
        if (failView == null) {
            failView = initBaseView(R.layout.view_load_fail);
        }

        TextView textView = (TextView) failView.findViewById(R.id.text_fail);
        setTextToTextView(textView, failMessage);
        setTextOption(textView, messageOption);

        TextView textRetry = (TextView) failView.findViewById(R.id.text_try_again);
        setTextToTextView(textRetry, retryMessage);
        setTextOption(textRetry, buttonTextOption);

        LinearLayout layout = (LinearLayout) failView.findViewById(R.id.layout_retry);
        if (retryButtonBackgroundDrawable != null) {
            layout.setBackground(retryButtonBackgroundDrawable);
        } else {
            layout.setBackgroundColor(retryButtonBackgroundColour);
        }
        setupFailButton(layout);

        return failView;
    }

    protected ViewGroup createLoadingView() {
        if (loadingView == null) {
            loadingView = initBaseView(R.layout.view_loading);
        }

        TextView textView = (TextView) loadingView.findViewById(R.id.text_loading);
        setTextToTextView(textView, loadingMessage);
        setTextOption(textView, messageOption);

        if (imageLoading == null) {
            imageLoading = (ImageView) loadingView.findViewById(R.id.image_loading);
        }
        if (loadingDrawable != null) {
            imageLoading.setImageDrawable(loadingDrawable);
        }
        imageLoading.startAnimation(getRotateAnimation());

        return loadingView;
    }

    private void setTextToTextView(TextView textView, String message) {
        if (textView != null && message != null) {
            textView.setText(message);
        }
    }

    private void setupFailButton(@NonNull View view) {
        if (canRetry) {
            view.setVisibility(VISIBLE);
            view.setOnClickListener(new OnClickListener() {
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

    /*
     * TextOption method---------------------------------------------------------------------------
     */
    private void setTextOption(TextView textView, TextOption option) {
        if (option != null) {
            textView.setTextColor(option.getColor());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, option.getSize());
        }
    }

    private void resetMessageTextOption() {
        messageOption.setColor(DEFAULT_TEXT_COLOR);
        messageOption.setSize(DEFAULT_TEXT_SIZE);
    }

    private void resetButtonTextOption() {
        buttonTextOption.setColor(DEFAULT_BUTTON_TEXT_COLOR);
        buttonTextOption.setSize(DEFAULT_TEXT_SIZE);
    }

    private void transferTextOption(TextOption mainOption, TextOption option) {
        if (option != null) {
            try {
                mainOption.setColor(option.getColor());
                mainOption.setSize(option.getSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
     * --------------------------------------------------------------------------------------------
     */

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
            this.listener = null;
            this.setVisibility(GONE);
            super.removeAllViews();
        }
    }
}
