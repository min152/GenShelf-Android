package apps.gen.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import apps.gen.lib.utils.H;

/**
 * Created by gen on 16/4/30.
 */
public class ListCell extends RelativeLayout {
    RelativeLayout mContentView;

    public ListCell(Context context) {
        this(context, null);
    }

    public ListCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    ImageView mImageView;
    TextView mTextView;

    final int DEFAULT_PADDING = 6;
    public ListCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContentView = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(layoutParams);
        int padding = H.dip2px(context, DEFAULT_PADDING);
        mContentView.setPadding(padding,padding,padding,padding);
        super.addView(mContentView);

        initialize(context);
    }

    public ImageView getImageView() {
        if (mImageView == null) {
            mImageView = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(mContentView.getHeight() - H.dip2px(getContext(), DEFAULT_PADDING) * 2, ViewGroup.LayoutParams.MATCH_PARENT);
            mImageView.setLayoutParams(layoutParams);
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            mContentView.addView(mImageView);
        }
        return mImageView;
    }

    public TextView getTextView() {
        if (mTextView == null) {
            mTextView = new TextView(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(textLeft(), 0, 0, 0);
            mTextView.setLayoutParams(layoutParams);
            mTextView.setGravity(Gravity.CENTER_VERTICAL);
            mContentView.addView(mTextView);
        }
        return mTextView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateSize(w, h);
    }

    protected void initialize(Context context) {}

    int textLeft() {
        return mImageView == null || mImageView.getDrawable() == null ? 0 : getHeight() - H.dip2px(getContext(), DEFAULT_PADDING);
    }

    public RelativeLayout getContentView() {
        return mContentView;
    }

    public void updateSize(int w, int h) {
        if (mImageView != null) {
            mImageView.getLayoutParams().width = h - H.dip2px(getContext(), DEFAULT_PADDING) * 2;
        }
        if (mTextView != null) {
            LayoutParams layoutParams = (LayoutParams)mTextView.getLayoutParams();
            layoutParams.setMargins(textLeft(), 0, 0, 0);
        }
    }
}
