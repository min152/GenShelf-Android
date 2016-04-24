package apps.gen.genshelf.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.gen.genshelf.R;
import apps.gen.lib.controllers.Controller;

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
}
