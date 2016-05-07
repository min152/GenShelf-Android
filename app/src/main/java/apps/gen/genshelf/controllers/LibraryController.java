package apps.gen.genshelf.controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import apps.gen.genshelf.R;
import apps.gen.genshelf.ShelfActivity;
import apps.gen.genshelf.views.BookCell;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.utils.H;
import apps.gen.lib.utils.NotificationCenter;
import apps.gen.lib.views.ListCell;
import cz.msebera.android.httpclient.Header;

public class LibraryController extends Controller {
    DynamicListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.controller_library, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (DynamicListView)view.findViewById(R.id.list_view);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ListCell cell = (ListCell) convertView;
                if (cell == null) {
                    cell = new BookCell(getContext());
                }
                cell.getTextView().setText("Hel");
                if (position == 1) {
                    cell.getImageView().setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
                } else {
                    cell.getImageView().setImageDrawable(null);
                }
                return cell;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.startDragging(position);
                return true;
            }
        });
    }

    @Override
    protected void initialize(Context context) {
        setTitle("GenShelf");

        setLeftItems(new NavigationItem(new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_menu).sizeDp(22).color(Color.WHITE), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCenter.instance().postNotification(ShelfActivity.NOTIFY_SHOW_DRAWER, LibraryController.this);
            }
        }));
    }
}
