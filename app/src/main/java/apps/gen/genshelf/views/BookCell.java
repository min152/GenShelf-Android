package apps.gen.genshelf.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import apps.gen.lib.utils.H;
import apps.gen.lib.views.ListCell;

/**
 * Created by Gen on 2016/5/2.
 */
public class BookCell extends ListCell {

    public BookCell(Context context) {
        super(context);
    }

    public BookCell(Context context, String identifier) {
        super(context, identifier);
    }

    @Override
    protected void initialize(Context context) {
        super.initialize(context);

        getContentView().getLayoutParams().height = H.dip2px(context, 88);
    }
}
