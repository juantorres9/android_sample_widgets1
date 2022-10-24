package com.example.myactivitytests.modules;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Represents a loading button with progress loading using counter.
 */
public class CustomLoadingButtonProgress extends RelativeLayout {
    private static final int DEFAULT_PROGRESS_MAX = 75;

    private int mProgressMax = DEFAULT_PROGRESS_MAX;

    /**
     * Rectangle image reprensenting progress button background.
     */
    private ImageView mImageView;

    /**
     * Whether draw transparent yellow background when button is not progressing.
     */
    private boolean mDrawBackground;

    private boolean mAlternatable;

    /**
     * Margin between white rectangle progress and borders.
     */
    private int mMarginInPixels = 0;

    /**
     * Current progress of the button. Between 0 and {@link #DEFAULT_PROGRESS_MAX}.
     */
    private int mProgress = 0;

    /**
     * If button has a child containing a text, text can be set using the {@link #setText(String)} method.
     */
    private TextView mTextView;

    /**
     * If button has no child containing a text yet, text is saved and restored when {@link #mTextView} wont be null.
     */
    private String mSavedTextView = "";

    private final Resources mResources;

    private final boolean isAlt;

    public CustomLoadingButtonProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        isAlt = Storage.getStorage(context).isDemoMode();

        mResources = context != null ? context.getResources() : null;

      //  TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.loading_button, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
    //       mDrawBackground = typedArray.getBoolean(R.styleable.loading_button_background_color, true);
      //      mAlternatable = typedArray.getBoolean(R.styleable.loading_button_alternatable, false);
        } finally {
       //     typedArray.recycle();
        }

        init();
    }

    /**
     * Init the button loading background.
     */
    private void init() {
        if (mDrawBackground) {
            if(isAlt && mAlternatable) {
            //    this.setBackground(getContext().getResources().getDrawable(R.drawable.button_loading_alt, null));
            } else {
              //  this.setBackground(getContext().getResources().getDrawable(R.drawable.button_loading, null));
            }
        } else {
            //this.setBackground(getContext().getResources().getDrawable(R.drawable.button_loading_without_solid, null));
        }

        final CustomLoadingButtonProgress thisFinal = this;

        thisFinal.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (thisFinal.getViewTreeObserver().isAlive()) {
                    thisFinal.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                // if there is a textview in children list, set the member mTextView

                for (int index = 0; index < thisFinal.getChildCount(); ++index) {
                    View child = thisFinal.getChildAt(index);
                    if (child instanceof TextView) {
                        mTextView = (TextView) child;
                        if (!mSavedTextView.equals("")) {
                            mTextView.setText(mSavedTextView);
                        }
                    }
                }

                // draw the button background
                mImageView = new ImageView(getContext());
                Drawable rectangleDrawable;
                if(isAlt && mAlternatable) {
         //           rectangleDrawable = getContext().getDrawable(R.drawable.rectangle_pixee_blue);
                } else {
            //        rectangleDrawable = getContext().getDrawable(R.drawable.rectangle_yellow);
                }
              //  mImageView.setImageDrawable(rectangleDrawable);
                LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mMarginInPixels = 3;
                imageParams.width = 0;
                imageParams.height = thisFinal.getHeight() - mMarginInPixels*2;
                if(imageParams.height <= 0){
                    imageParams.height = 2048;
                }
                imageParams.setMargins(mMarginInPixels, mMarginInPixels, mMarginInPixels, mMarginInPixels);
                mImageView.setLayoutParams(imageParams);
                mImageView.requestLayout();
                mImageView.invalidate();
                mImageView.setVisibility(VISIBLE);
                thisFinal.addView(mImageView, 0);
                return true;
            }
        });
    }

    /**
     * Reset progress button.
     */
    public void reset() {
        mProgress = 0;
        setProgress();
    }

    @Override
    public boolean performClick() {
        return false;
    }

    /**
     * Add progress => increasing background width.
     * @param pProgress increment.
     */
    public void addProgress(int pProgress) {
        mProgress += pProgress;
        setProgress();
    }

    private void drawTexture(int progress){
        final CustomLoadingButtonProgress thisFinal = this;
        if (mImageView != null) {
            this.post(() -> {
                mImageView.getLayoutParams().width = (thisFinal.getWidth() - 2 * mMarginInPixels) * progress / mProgressMax;
                mImageView.requestLayout();
            });
        }
    }

    public void fillTexture(){
        drawTexture(DEFAULT_PROGRESS_MAX);
    }

    /**
     * Set the progress using {@link #mProgress}.
     */
    private void setProgress() {
        drawTexture(mProgress);
    }

    /**
     * Set the maximum progress of the button.
     * @param pProgressMax max progress integer.
     */
    public void setProgressMax(int pProgressMax) {
        this.mProgressMax = pProgressMax;
    }

    /**
     * Know if button is fully filled.
     * @return true if {@link #mProgress} > {@link #mProgressMax}.
     */
    public boolean isFinished() {
        return mProgress >= mProgressMax;
    }

    public int getProgress(){
        return mProgress;
    }

    public int getProgressMax(){
        return mProgressMax;
    }

    /**
     * If button contains a child text, it can be set using this method.
     * @param pText text to set in the button.
     */
    public void setText(String pText) {
        if (this.mTextView != null) {
   //         ViewUtils.setText(this.mTextView, pText);
        } else {
            mSavedTextView = pText;
        }
    }

    public String getTypename(){
        final Resources r = mResources;
        final int id = getId();
        return r.getResourceEntryName(id);
    }

    public String toString(){
        return getTypename();
    }
}
