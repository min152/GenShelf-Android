package gen.libraries.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apps.gen.genshelf.R;

/**
 * Created by Gen on 2016/3/29.
 */
public class NavigationBar extends RelativeLayout {
    ImageButton mLeftItemButton;
    ImageButton mRightItemButton;
    TextView mTitleView;
    ImageButton mTempLeftItemButton;
    ImageButton mTempRightItemButton;
    TextView mTempTitleView;

    private Drawable mLeftIcon;
    private Drawable mRightIcon;

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar);
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
    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public NavigationBar(Context context) {
        this(context, null);
    }

    void initView(Context context) {
        mTitleView = new TextView(context);
        mTitleView.setText("Title");
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_VERTICAL, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(Helper.dip2px(context, 40));
            layoutParams.setMarginEnd(Helper.dip2px(context, 40));
        }
        layoutParams.setMargins(40, 0, 40, 0);
        mTitleView.setLayoutParams(layoutParams);
        mTitleView.setMaxLines(1);
        mTitleView.setTextSize(22);
        mTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTitleView.setEllipsize(TextUtils.TruncateAt.END);
        addView(mTitleView);

        mLeftItemButton = new ImageButton(context);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_VERTICAL, 1);
        layoutParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        layoutParams.leftMargin = Helper.dip2px(context, 8);
        mLeftItemButton.setLayoutParams(layoutParams);
        mLeftItemButton.setMaxWidth(22);
        mLeftItemButton.setMaxHeight(22);
        mLeftItemButton.setBackgroundResource(R.color.colorClean);
        addView(mLeftItemButton);

        mRightItemButton = new ImageButton(context);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_VERTICAL, 1);
        layoutParams.rightMargin = Helper.dip2px(context, 8);
        layoutParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mRightItemButton.setLayoutParams(layoutParams);
        mRightItemButton.setMaxWidth(22);
        mRightItemButton.setMaxHeight(22);
        mRightItemButton.setBackgroundResource(R.color.colorClean);
        addView(mRightItemButton);
    }

    void updateLayout() {
        if (mLeftIcon != null) {
            mLeftItemButton.setVisibility(VISIBLE);
        }else {
            mLeftItemButton.setVisibility(GONE);
        }
    }

    public void setLeftIcon(Drawable leftIcon) {
        if (!mLeftIcon.equals(leftIcon)) {
            mLeftIcon = leftIcon;
            mLeftItemButton.setImageDrawable(leftIcon);
            updateLayout();
        }
    }

    public Drawable getLeftItem() {
        return mLeftIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        if (!mRightIcon.equals(rightIcon)) {
            mRightIcon = rightIcon;
            mRightItemButton.setImageDrawable(rightIcon);
            updateLayout();
        }
    }

    public void setLeftItem(Drawable drawable, Boolean animated) {
        
    }
}
