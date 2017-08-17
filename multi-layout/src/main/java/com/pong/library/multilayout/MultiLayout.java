package com.pong.library.multilayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
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
    private static final String NONE = "none";

    private static final float DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_RETRY_BG_COLOUR = Color.GRAY;
    private static final int DEFAULT_TEXT_COLOR = Color.DKGRAY;
    private static final int DEFAULT_BUTTON_TEXT_COLOR = Color.WHITE;

    private String currentView;

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
    @LinearLayoutCompat.OrientationMode
    private int loadingOrientation;

    private OnRetryListener listener;
    private Drawable retryButtonBackgroundDrawable;
    private int retryButtonBackgroundColor = DEFAULT_RETRY_BG_COLOUR;

    private TextOption emptyMessageOption;
    private TextOption loadingMessageOption;
    private TextOption failMessageOption;
    private TextOption buttonTextOption;

    public MultiLayout(Context context) {
        super(context);
        init(null);
    }

    public MultiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setupDefaultView();
        setupAttribute(attrs);
    }

    private void setupDefaultView() {
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        this.setVisibility(GONE);
        emptyMessageOption = new TextOption(16, Color.DKGRAY);
        loadingMessageOption = new TextOption(16, Color.DKGRAY);
        failMessageOption = new TextOption(16, Color.DKGRAY);
        buttonTextOption = new TextOption(16, Color.WHITE);
        loadingOrientation = LinearLayout.HORIZONTAL;
    }

    private void setupAttribute(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.MultiLayout, 0, 0);

        if (attrs != null) {
            emptyMessage = getString(a.getText(R.styleable.MultiLayout_empty_msg));
            loadingMessage = getString(a.getText(R.styleable.MultiLayout_loading_msg));
            failMessage = getString(a.getText(R.styleable.MultiLayout_error_msg));

            Drawable loading = a.getDrawable(R.styleable.MultiLayout_loading_indicator);
            if (loading != null) {
                loadingDrawable = loading;
            }

            a.recycle();
        }
    }

    private String getString(CharSequence charSequence) {
        return (charSequence != null) ? charSequence.toString() : null;
    }

    /**
     * Set empty message that display when empty view show
     *
     * @param msg text message
     */
    public void setEmptyMessage(@NonNull String msg) {
        setEmptyMessage(msg, null);
        resetMessageTextOption(emptyMessageOption);
    }

    /**
     * Set empty message with option(color,size) that display when empty view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setEmptyMessage(@NonNull String msg, TextOption option) {
        emptyMessage = msg;
        transferTextOption(emptyMessageOption, option);
    }

    /**
     * Set fail message that display when fail view show
     *
     * @param msg text message
     */
    public void setFailMessage(@NonNull String msg) {
        setFailMessage(msg, null);
        resetMessageTextOption(failMessageOption);
    }

    /**
     * Set fail message with option(color,size) that display when fail view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setFailMessage(@NonNull String msg, TextOption option) {
        failMessage = msg;
        transferTextOption(failMessageOption, option);
    }

    /**
     * Set loading message that display when loading view show
     *
     * @param msg text message
     */
    public void setLoadingMessage(@NonNull String msg) {
        setLoadingMessage(msg, null);
        resetMessageTextOption(loadingMessageOption);
    }

    /**
     * Set loading message with option(color,size) that display when loading view show
     *
     * @param msg    text message
     * @param option text option
     */
    public void setLoadingMessage(@NonNull String msg, TextOption option) {
        loadingMessage = msg;
        transferTextOption(loadingMessageOption, option);
    }

    /**
     * Set loading layout of indicator and text (Default is horizontal)
     *
     * @param orientation layout orientation
     */
    public void setLoadingOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        loadingOrientation = orientation;
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
        this.retryButtonBackgroundColor = color;
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
     * @param listener listener will active when press retry button.
     *                 (must set listener again when rotate screen or any restore state)
     */
    public void showFail(@NonNull OnRetryListener listener) {
        this.listener = listener;
        canRetry = true;
        switchView(FAIL);
    }

    private void switchView(String type) {
        removeAllViews();
        this.setVisibility(VISIBLE);
        currentView = type;

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
                this.setVisibility(GONE);
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
        setTextOption(textView, emptyMessageOption);

        return emptyView;
    }

    protected ViewGroup createFailView() {
        if (failView == null) {
            failView = initBaseView(R.layout.view_load_fail);
        }

        TextView textView = (TextView) failView.findViewById(R.id.text_fail);
        setTextToTextView(textView, failMessage);
        setTextOption(textView, failMessageOption);

        TextView textRetry = (TextView) failView.findViewById(R.id.text_try_again);
        setTextToTextView(textRetry, retryMessage);
        setTextOption(textRetry, buttonTextOption);

        LinearLayout layout = (LinearLayout) failView.findViewById(R.id.layout_retry);
        if (retryButtonBackgroundDrawable != null) {
            layout.setBackground(retryButtonBackgroundDrawable);
        } else {
            layout.setBackgroundColor(retryButtonBackgroundColor);
        }
        setupFailButton(layout);

        return failView;
    }

    protected ViewGroup createLoadingView() {
        if (loadingView == null) {
            loadingView = initBaseView(R.layout.view_loading);
        }

        LinearLayout layout = (LinearLayout) loadingView.findViewById(R.id.layout_loading);
        layout.setOrientation(loadingOrientation);

        TextView textView = (TextView) loadingView.findViewById(R.id.text_loading);
        setTextToTextView(textView, loadingMessage);
        setTextOption(textView, loadingMessageOption);

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

    private void resetMessageTextOption(TextOption textOption) {
        textOption.setColor(DEFAULT_TEXT_COLOR);
        textOption.setSize(DEFAULT_TEXT_SIZE);
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
            this.setVisibility(GONE);
            currentView = NONE;
            super.removeAllViews();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentView = this.currentView;
        ss.emptyMessage = this.emptyMessage;
        ss.loadingMessage = this.loadingMessage;
        ss.failMessage = this.failMessage;
        ss.retryMessage = this.retryMessage;
        ss.canRetry = this.canRetry;
        ss.loadingOrientation = this.loadingOrientation;
        ss.retryButtonBackgroundColor = this.retryButtonBackgroundColor;
        ss.emptyMessageOption = this.emptyMessageOption;
        ss.loadingMessageOption = this.loadingMessageOption;
        ss.failMessageOption = this.failMessageOption;
        ss.buttonTextOption = this.buttonTextOption;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.emptyMessage = ss.emptyMessage;
        this.loadingMessage = ss.loadingMessage;
        this.failMessage = ss.failMessage;
        this.retryMessage = ss.retryMessage;
        this.canRetry = ss.canRetry;
        this.loadingOrientation = ss.loadingOrientation;
        this.retryButtonBackgroundColor = ss.retryButtonBackgroundColor;
        this.emptyMessageOption = ss.emptyMessageOption;
        this.loadingMessageOption = ss.loadingMessageOption;
        this.failMessageOption = ss.failMessageOption;
        this.buttonTextOption = ss.buttonTextOption;
    }

    private static class SavedState extends BaseSavedState {
        String currentView;

        String emptyMessage;
        String loadingMessage;
        String failMessage;
        String retryMessage;

        boolean canRetry;
        int loadingOrientation;
        int retryButtonBackgroundColor;

        TextOption emptyMessageOption;
        TextOption loadingMessageOption;
        TextOption failMessageOption;
        TextOption buttonTextOption;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);

            this.currentView = in.readString();

            this.emptyMessage = in.readString();
            this.loadingMessage = in.readString();
            this.failMessage = in.readString();
            this.retryMessage = in.readString();

            this.canRetry = in.readByte() != 0;

            this.loadingOrientation = in.readInt();
            this.retryButtonBackgroundColor = in.readInt();

            this.emptyMessageOption = in.readParcelable(TextOption.class.getClassLoader());
            this.loadingMessageOption = in.readParcelable(TextOption.class.getClassLoader());
            this.failMessageOption = in.readParcelable(TextOption.class.getClassLoader());
            this.buttonTextOption = in.readParcelable(TextOption.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(this.currentView);

            out.writeString(this.emptyMessage);
            out.writeString(this.loadingMessage);
            out.writeString(this.failMessage);
            out.writeString(this.retryMessage);

            out.writeByte((byte) (canRetry ? 1 : 0));

            out.writeInt(this.loadingOrientation);
            out.writeInt(this.retryButtonBackgroundColor);

            out.writeParcelable(this.emptyMessageOption, 0);
            out.writeParcelable(this.loadingMessageOption, 0);
            out.writeParcelable(this.failMessageOption, 0);
            out.writeParcelable(this.buttonTextOption, 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
