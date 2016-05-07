package apps.gen.genshelf.controllers;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import apps.gen.genshelf.R;
import apps.gen.genshelf.ShelfActivity;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.utils.NotificationCenter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShelfController.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShelfController#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShelfController extends Controller {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.controller_shelf, container, false);
    }

    @Override
    protected void initialize(Context context) {
        setLeftItems(new NavigationItem(new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_menu).sizeDp(24).color(Color.WHITE), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCenter.instance().postNotification(ShelfActivity.NOTIFY_SHOW_DRAWER, ShelfController.this);
            }
        }));
    }
}
