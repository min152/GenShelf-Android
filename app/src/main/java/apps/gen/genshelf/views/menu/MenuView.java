package apps.gen.genshelf.views.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.util.Insertable;

import java.util.ArrayList;
import java.util.Collections;

import apps.gen.genshelf.R;
import apps.gen.lib.utils.CacheAdapter;
import apps.gen.lib.utils.H;
import apps.gen.lib.views.ListCell;

/**
 * Created by gen on 16/5/8.
 */
public class MenuView extends ListView {
    public static class MenuItem {
        enum Type {
            Item,
            Header
        }

        String name;
        Drawable icon;
        Type type;
        View.OnClickListener onClickListener;

        public static MenuItem header(String name, Drawable icon) {
            MenuItem header = new MenuItem();
            header.name = name;
            header.type = Type.Header;
            header.icon = icon;
            return header;
        }
        public static MenuItem header(String name) {
            return header(name, null);
        }

        public static MenuItem item(String name, Drawable icon, View.OnClickListener onClickListener) {
            MenuItem item = new MenuItem();
            item.name = name;
            item.icon = icon;
            item.onClickListener = onClickListener;
            item.type = Type.Item;
            return item;
        }
    }
    class MenuAdapter extends CacheAdapter {

        ArrayList<MenuItem> items = new ArrayList<MenuView.MenuItem>();
        Context mContext;
        public MenuAdapter(Context context) {
            mContext = context;
        }
        public void setItems(MenuItem[] items) {
            this.items.clear();
            Collections.addAll(this.items, items);
        }

        @Override
        public ListCell cellView(int position) {
            MenuView.MenuItem item = items.get(position);
            switch (item.type) {
                case Header:
                {
                    String id = "header";
                    ListCell cell = popWithIdentifier(id);
                    if (cell == null) {
                        cell = new ListCell(mContext, id);
                        cell.setBackgroundColor(mContext.getResources().getColor(R.color.colorHeader));
                        cell.getContentView().getLayoutParams().height = H.dip2px(mContext, 22);
                        cell.getContentView().setPadding(H.dip2px(mContext, 6), 0, 0, 0);
                    }
                    cell.getLabelView().setText(item.name);
                    return cell;
                }
                case Item: {
                    String id = "item";
                    ListCell cell = popWithIdentifier(id);
                    if (cell == null) {
                        cell = new ListCell(mContext, id);
                        cell.getContentView().getLayoutParams().height = H.dip2px(mContext, 46);
                    }
                    cell.getLabelView().setText(item.name);
                    cell.getImageView().setImageDrawable(item.icon);
                    return cell;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).hashCode();
        }

        @Override
        public boolean isEnabled(int position) {
            MenuView.MenuItem item = items.get(position);
            return item.type == MenuView.MenuItem.Type.Item;
        }
    }

    public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, final MenuItem[] items) {
        super(context);
        MenuAdapter adapter = new MenuAdapter(context);
        adapter.setItems(items);
        setAdapter(adapter);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items[position].onClickListener != null) {
                    items[position].onClickListener.onClick(view);
                }
            }
        });
    }
}
