package apps.gen.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

    public ListCell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContentView = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(layoutParams);
        super.addView(mContentView);

        initialize(context);
    }

    protected void initialize(Context context) {

    }

    public RelativeLayout getContentView() {
        return mContentView;
    }
}
