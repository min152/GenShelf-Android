package apps.gen.lib.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apps.gen.lib.utils.Configs;
import apps.gen.lib.R;
import apps.gen.lib.utils.H;

import com.fichardu.interpolator.*;

/**
 * Created by Gen on 2016/3/29.
 */
public class NavigationBar extends RelativeLayout implements View.OnClickListener {
    public interface OnClickListener {
        void onClick(View v, int index);
    }

    public enum AnimationType {
        NONE,
        PUSH,
        POP,
        FADE
    }

    ImageButton mLeftItemButton;
    ImageButton mRightItemButton;
    TextView mTitleView;

    Drawable mLeftIcon;
    Drawable mRightIcon;

    RelativeLayout mContentView;

    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
            String title = a.getString(R.styleable.NavigationBar_title);
            if (title != null)
                mTitleView.setText(title);
            Drawable leftIcon = a.getDrawable(R.styleable.NavigationBar_leftIcon);
            if (leftIcon != null)
                mLeftItemButton.setImageDrawable(leftIcon);
            Drawable rightIcon = a.getDrawable(R.styleable.NavigationBar_rightIcon);
            if (rightIcon != null)
                mRightItemButton.setImageDrawable(rightIcon);
        }
    }
    public NavigationBar(Context context) {
        this(context, null);
    }

    void initView(Context context) {
        mContentView = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(layoutParams);
        addView(mContentView);

        mTitleView = initTitle(context);
        mContentView.addView(mTitleView);

        mLeftItemButton =initLeftButton(context);
        mContentView.addView(mLeftItemButton);

        mRightItemButton = initRightButton(context);
        mContentView.addView(mRightItemButton);
    }

    void updateLayout() {
        if (mLeftIcon != null) {
            mLeftItemButton.setVisibility(VISIBLE);
        }else {
            mLeftItemButton.setVisibility(GONE);
        }
        if (mRightIcon != null) {
            mRightItemButton.setVisibility(VISIBLE);
        }else {
            mRightItemButton.setVisibility(GONE);
        }
    }

    public void setLeftIcon(Drawable leftIcon) {
        if (mLeftIcon != leftIcon) {
            mLeftIcon = leftIcon;
            mLeftItemButton.setImageDrawable(leftIcon);
            updateLayout();
            checkLeftIcon();
        }
    }

    public Drawable getLeftItem() {
        return mLeftIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        if (mRightIcon != rightIcon) {
            mRightIcon = rightIcon;
            mRightItemButton.setImageDrawable(rightIcon);
            updateLayout();
            checkRightIcon();
        }
    }

    final float AnimationOffset = 100;
    public void setLeftIcon(Drawable leftIcon, AnimationType animationType) {
        switch (animationType) {
            case NONE:
                setLeftIcon(leftIcon);
                break;
            case FADE:
            {
                Context context = getContext();
                final ImageButton newButton = initRightButton(context);
                newButton.setImageDrawable(leftIcon);
                newButton.setOnClickListener(this);
                addView(newButton);

                AlphaAnimation aa = new AlphaAnimation(0, 1);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                newButton.startAnimation(aa);

                if (mLeftItemButton != null) mLeftItemButton.clearAnimation();
                aa = new AlphaAnimation(1, 0);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                mLeftItemButton.startAnimation(aa);
                final View willRemove = mLeftItemButton;
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mLeftItemButton = newButton;
            }
            default:
                Context context = getContext();
                final ImageButton newButton = initLeftButton(context);
                newButton.setImageDrawable(leftIcon);
                newButton.setOnClickListener(this);
                addView(newButton);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                AlphaAnimation aa = new AlphaAnimation(0, 1);
                TranslateAnimation ta = new TranslateAnimation(animationType==AnimationType.PUSH?H.dip2px(context, AnimationOffset):-H.dip2px(context, AnimationOffset), 0, 0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                newButton.startAnimation(set);

                if (mLeftItemButton != null) mLeftItemButton.clearAnimation();
                set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                aa = new AlphaAnimation(1, 0);
                ta = new TranslateAnimation(mLeftItemButton.getTranslationX(), animationType==AnimationType.PUSH?-H.dip2px(context, AnimationOffset):H.dip2px(context, AnimationOffset),0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                mLeftItemButton.startAnimation(set);
                final View willRemove = mLeftItemButton;
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mLeftItemButton = newButton;
                mLeftIcon = leftIcon;
                checkLeftIcon();
        }
    }

    public void setRightIcon(Drawable rightIcon, AnimationType animationType) {
        switch (animationType) {
            case NONE:
                setRightIcon(rightIcon);
                break;
            case FADE:
            {
                Context context = getContext();
                final ImageButton newButton = initRightButton(context);
                newButton.setImageDrawable(rightIcon);
                newButton.setOnClickListener(this);
                addView(newButton);

                AlphaAnimation aa = new AlphaAnimation(0, 1);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                newButton.startAnimation(aa);

                if (mRightItemButton != null) mRightItemButton.clearAnimation();
                aa = new AlphaAnimation(1, 0);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                mRightItemButton.startAnimation(aa);
                final View willRemove = mRightItemButton;
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mRightItemButton = newButton;
            }
            default:
                Context context = getContext();
                final ImageButton newButton = initRightButton(context);
                newButton.setImageDrawable(rightIcon);
                newButton.setOnClickListener(this);
                addView(newButton);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                AlphaAnimation aa = new AlphaAnimation(0, 1);
                TranslateAnimation ta = new TranslateAnimation(animationType == AnimationType.PUSH? H.dip2px(context, AnimationOffset):-H.dip2px(context, AnimationOffset), 0, 0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                newButton.startAnimation(set);

                if (mRightItemButton != null) mRightItemButton.clearAnimation();
                set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                aa = new AlphaAnimation(1, 0);
                ta = new TranslateAnimation(mRightItemButton.getTranslationX(), animationType == AnimationType.PUSH?-H.dip2px(context, AnimationOffset):H.dip2px(context, AnimationOffset),0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                mRightItemButton.startAnimation(set);
                final View willRemove = mRightItemButton;
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mRightItemButton = newButton;
                mRightIcon = rightIcon;
                checkRightIcon();
        }
    }

    public  void setTitle(String title, AnimationType animationType) {
        Context context = getContext();
        switch (animationType) {
            case NONE:
                mTitleView.setText(title);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)mTitleView.getLayoutParams();
                layoutParams.setMargins(H.dip2px(context, mTitleLeft), 0, H.dip2px(context, mTitleRight), 0);
                break;
            case FADE:
            {
                final TextView newTitleView = initTitle(context);
                newTitleView.setText(title);
                addView(newTitleView);

                AlphaAnimation aa = new AlphaAnimation(0, 1);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                newTitleView.startAnimation(aa);

                if (mTitleView != null) mTitleView.clearAnimation();
                aa = new AlphaAnimation(1, 0);
                aa.setInterpolator(new EaseInOutCubicInterpolator());
                aa.setDuration(Configs.AnimationDuring);
                aa.setFillAfter(true);
                mTitleView.startAnimation(aa);
                final View willRemove = mTitleView;
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTitleView = newTitleView;
            }
            default: {
                final TextView newTitleView = initTitle(context);
                newTitleView.setText(title);
                addView(newTitleView);

                AnimationSet set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                AlphaAnimation aa = new AlphaAnimation(0, 1);
                TranslateAnimation ta = new TranslateAnimation(animationType == AnimationType.PUSH ? H.dip2px(context, AnimationOffset) : -H.dip2px(context, AnimationOffset), 0, 0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                newTitleView.startAnimation(set);

                if (mTitleView != null) mTitleView.clearAnimation();
                set = new AnimationSet(true);
                set.setInterpolator(new EaseInOutCubicInterpolator());
                aa = new AlphaAnimation(1, 0);
                ta = new TranslateAnimation(mTitleView.getTranslationX(), animationType == AnimationType.PUSH ? -H.dip2px(context, AnimationOffset) : H.dip2px(context, AnimationOffset),0, 0);
                set.addAnimation(aa);
                set.addAnimation(ta);
                set.setDuration(Configs.AnimationDuring);
                set.setFillAfter(true);
                mTitleView.startAnimation(set);
                final View willRemove = mTitleView;
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        NavigationBar.this.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(willRemove);
                            }
                        }, 0);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTitleView = newTitleView;
            }
        }
    }

    ImageButton initLeftButton(Context context) {
        ImageButton newButton = new ImageButton(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newButton.setLayoutParams(layoutParams);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.leftMargin = H.dip2px(context, 8);
        newButton.setMaxWidth(22);
        newButton.setMaxHeight(22);
        newButton.setBackgroundResource(R.color.colorClean);
        newButton.setOnClickListener(this);
        return newButton;
    }

    ImageButton initRightButton(Context context) {
        ImageButton newButton = new ImageButton(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        newButton.setLayoutParams(layoutParams);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
        layoutParams.rightMargin = H.dip2px(context, 8);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        newButton.setMaxWidth(22);
        newButton.setMaxHeight(22);
        newButton.setBackgroundResource(R.color.colorClean);
        newButton.setOnClickListener(this);
        return newButton;
    }

    final int MARGIN_MAX = 40, MARGIN_MIN = 8;
    void checkLeftIcon() {
        mTitleLeft = mLeftIcon == null ? MARGIN_MIN : MARGIN_MAX;
    }
    void checkRightIcon() {
        mTitleRight = mRightIcon == null ? MARGIN_MIN : MARGIN_MAX;
    }
    int mTitleLeft = MARGIN_MIN;
    int mTitleRight = MARGIN_MIN;
    void setTitleLeft(int titleLeft) {
        mTitleLeft = titleLeft;
    }
    void setTitleRight(int titleRight) {
        mTitleRight = titleRight;
    }

    TextView initTitle(Context context) {
        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
        layoutParams.setMargins(H.dip2px(context, mTitleLeft), 0, H.dip2px(context, mTitleRight), 0);
        textView.setLayoutParams(layoutParams);
        textView.setMaxLines(1);
        textView.setTextSize(22);
        textView.setTextColor(getResources().getColor(R.color.colorTitle));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return textView;
    }

    OnClickListener mLeftListener;
    OnClickListener mRightListener;
    public void setLeftListener(OnClickListener leftListener) {
        mLeftListener = leftListener;
    }
    public OnClickListener getLeftListener() {
        return mLeftListener;
    }
    public void setRightListener(OnClickListener rightListener) {
        mRightListener = rightListener;
    }
    public OnClickListener getRightListener() {
        return mRightListener;
    }
    int count = 0;
    @Override
    public void onClick(View v) {
        if (v.equals(mLeftItemButton) && mLeftListener != null) {
            mLeftListener.onClick(v,0);
        }else if (v.equals(mRightItemButton) && mRightListener != null) {
            mRightListener.onClick(v,0);
        }
    }
}
