package apps.gen.genshelf;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import apps.gen.genshelf.controllers.LibraryController;
import apps.gen.genshelf.controllers.ShelfController;
import apps.gen.genshelf.views.menu.MenuView;
import apps.gen.lib.Activity;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.controllers.NavigationController;
import apps.gen.lib.utils.H;
import apps.gen.lib.utils.NotificationCenter;

public class ShelfActivity extends Activity{
    public static final String NOTIFY_SHOW_DRAWER = "SHOW_DRAWER";

    Fragment mCurrentFragment;
    LibraryController mLibraryController;
    ShelfController mShelfController;

    LibraryController getLibraryController() {
        if (mLibraryController == null) {
            mLibraryController = Controller.instantiate(this, LibraryController.class);
        }
        return mLibraryController;
    }
    ShelfController getShelfController() {
        if (mShelfController == null) {
            mShelfController = Controller.instantiate(this, ShelfController.class);
        }
        return mShelfController;
    }

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

        final NavigationController navigationController = (NavigationController)getSupportFragmentManager().findFragmentById(R.id.navigation_controller);
        MenuView menuView = new MenuView(this, new MenuView.MenuItem[] {
                MenuView.MenuItem.header(""),
                MenuView.MenuItem.item(getResources().getString(R.string.shelf),
                        new IconicsDrawable(this)
                                .color(Color.BLACK)
                                .sizeDp(22)
                                .icon(GoogleMaterial.Icon.gmd_photo_library),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!(navigationController.top() instanceof ShelfController)) {
                                    navigationController.setControllers(new Controller[]{getShelfController()}, true);
                                    drawerLayout.closeDrawer(navigationView);
                                }
                            }
                        }
                ),
                MenuView.MenuItem.item(getResources().getString(R.string.library),
                        new IconicsDrawable(this)
                                .color(Color.BLACK)
                                .sizeDp(22)
                                .icon(GoogleMaterial.Icon.gmd_account_balance),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!(navigationController.top() instanceof LibraryController)) {
                                    navigationController.setControllers(new Controller[]{getLibraryController()}, true);
                                    drawerLayout.closeDrawer(navigationView);
                                }
                            }
                        }
                ),
                MenuView.MenuItem.item(getResources().getString(R.string.search),
                        new IconicsDrawable(this)
                                .color(Color.BLACK)
                                .sizeDp(22)
                                .icon(GoogleMaterial.Icon.gmd_search),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }
                ),
                MenuView.MenuItem.item(getResources().getString(R.string.progress),
                        new IconicsDrawable(this)
                                .color(Color.BLACK)
                                .sizeDp(22)
                                .icon(GoogleMaterial.Icon.gmd_history),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }
                ),
                MenuView.MenuItem.item(getResources().getString(R.string.settings),
                        new IconicsDrawable(this)
                                .color(Color.BLACK)
                                .sizeDp(22)
                                .icon(GoogleMaterial.Icon.gmd_settings),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }
                )
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        menuView.setLayoutParams(layoutParams);
        menuView.setPadding(0, H.dip2px(20), 0, 0);
        navigationView.addView(menuView);

        navigationController.push(getLibraryController());
    }
}
