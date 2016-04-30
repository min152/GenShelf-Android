package apps.gen.lib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import apps.gen.lib.utils.H;


/**
 * Created by gen on 16/4/22.
 */
public class TabBar extends RelativeLayout implements View.OnClickListener {

    public interface OnClickListener {
        void onClick(View v, int index);
    }

    public static class TabItem {
        Drawable mIcon;
        public Drawable getIcon() {
            return mIcon;
        }
        public void setIcon(Drawable icon) {
            mIcon = icon;
        }

        String mTitle;
        public String getTitle() {
            return mTitle;
        }
        public void setTitle(String title) {
            mTitle = title;
        }

        public TabItem(String title, Drawable icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    class TabButton extends RelativeLayout {
        TextView mLabelView;
        public TextView getLabelView() {
            return mLabelView;
        }
        ImageView mImageView;
        public ImageView getImageView() {
            return mImageView;
        }

        public TabButton(Context context) {
            this(context, null);
        }

        public TabButton(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabButton(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView(context);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public TabButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initView(context);
        }

        void initView(Context context) {
            mLabelView = new TextView(context);
            mLabelView.setGravity(Gravity.CENTER);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, H.dip2px(context, 20));
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            mLabelView.setLayoutParams(layoutParams);
            mImageView = new ImageView(context);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, H.dip2px(context, 20));
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER);
            addView(linearLayout);

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mImageView.setLayoutParams(layoutParams1);

            addView(mLabelView);
            linearLayout.addView(mImageView);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        TabItem mItem;
        TabItem getItem() {
            return mItem;
        }
        void setItem(TabItem item) {
            if (mItem != item) {
                mItem = item;
                mLabelView.setText(item.getTitle());
                mImageView.setImageDrawable(item.getIcon());
            }
        }
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public TabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TabBar(Context context) {
        this(context, null);
    }

    ArrayList<TabItem> mItems = new ArrayList<>();
    public void setItems(TabItem[] items) {
        mItems.clear();
        Collections.addAll(mItems, items);
        updateItems();
    }
    public void addItem(TabItem item) {
        mItems.add(item);
        updateItems();
    }
    public void removeItem(TabItem item) {
        mItems.remove(item);
        updateItems();
    }
    public void insertItem(TabItem item, int index) {
        mItems.add(index, item);
        updateItems();
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateItems();
    }

    ArrayList<TabButton> mButtons = new ArrayList<>();
    void updateItems() {
        int width = getWidth(), height = getHeight();
        if (width == 0 || height == 0) return;
        for (TabButton button : mButtons) {
            removeView(button);
        }
        Context context = getContext();
        mButtons.clear();
        int count = 0;
        for (TabItem item : mItems) {
            final TabButton button = new TabButton(context);
            button.setItem(item);
            LayoutParams layoutParams = new LayoutParams(height, height);
            button.setLayoutParams(layoutParams);
            progressView(button, count, width, height, new H.BaseInterface2<Integer, Integer>() {
                @Override
                public void run(Integer p, Integer o) {
                    button.setY(p);
                    button.setX(o);
                }
            });
            button.setOnClickListener(this);
            addView(button);
            mButtons.add(button);
            count ++;
        }
    }
    void progressView(TabButton button, int index, int width, int height, H.BaseInterface2<Integer,Integer> in) {
        int size = mItems.size();
        int itemSpan = (width - height * size)/(size + 1);
        int top = 0;
        int left = itemSpan * (index + 1) + height * index;
        in.run(top, left);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v, mButtons.indexOf(v));
        }
    }
}
