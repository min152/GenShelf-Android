package apps.gen.lib.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import apps.gen.lib.views.ListCell;

/**
 * Created by gen on 16/5/2.
 */
public abstract class CacheAdapter extends BaseAdapter {
    Map<String, Queue<ListCell>> viewCache = new HashMap<>();
    ListCell _template;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        _template = (ListCell)convertView;
        ListCell res = cellView(position);
        if (_template != null && !res.equals(_template)) {
            pushCache(_template);
        }
        res.updateSize();
        return res;
    }

    void pushCache(ListCell cell) {
        Queue<ListCell> queue = null;
        if (!viewCache.containsKey(cell.getIdentifier())) {
            queue = new LinkedBlockingQueue<>();
            viewCache.put(cell.getIdentifier(), queue);
        }else {
            queue = viewCache.get(cell.getIdentifier());
        }
        queue.add(cell);
    }

    protected ListCell popWithIdentifier(String identifier) {
        if (_template != null) {
            if (_template.getIdentifier().equals(identifier))
                return _template;
        }
        if (viewCache.containsKey(identifier)) {
            Queue<ListCell> queue = viewCache.get(identifier);
            if (queue.size() > 0)
                return queue.poll();
        }
        return null;
    }

    public abstract ListCell cellView(int position);
}
