package apps.gen.genshelf;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import apps.gen.genshelf.controllers.LibraryController;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.controllers.NavigationController;
import apps.gen.lib.utils.H;
import apps.gen.lib.utils.NotificationCenter;
import apps.gen.lib.views.ListCell;

public class ShelfActivity extends AppCompatActivity{
    public static final String NOTIFY_SHOW_DRAWER = "SHOW_DRAWER";

    Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        final NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        final DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NotificationCenter.instance().addListener(NOTIFY_SHOW_DRAWER, new NotificationCenter.Runnable() {
            @Override
            public void run(NotificationCenter.Notification notification) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        ListView listView = new ListView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(layoutParams);
        navigationView.addView(listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 2;
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
                    cell = new ListCell(ShelfActivity.this);
                    cell.getContentView().getLayoutParams().height = H.dip2px(ShelfActivity.this, 88);
                }
                switch (position) {
                    case 0:
                        cell.getImageView().setImageDrawable(new IconicsDrawable(ShelfActivity.this).icon(GoogleMaterial.Icon.gmd_album).color(Color.BLACK).sizeDp(22));
                        cell.getTextView().setText("Shelf");
                        break;
                    case 1:
                        cell.getImageView().setImageDrawable(new IconicsDrawable(ShelfActivity.this).icon(GoogleMaterial.Icon.gmd_history).color(Color.BLACK).sizeDp(22));
                        cell.getTextView().setText("Home");
                        break;
                }
                return cell;
            }
        });

        NavigationController navigationController = (NavigationController)getSupportFragmentManager().findFragmentById(R.id.navigation_controller);
        navigationController.push(Controller.instantiate(this, LibraryController.class));

    }
}
