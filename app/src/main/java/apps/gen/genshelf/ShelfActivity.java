package apps.gen.genshelf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import apps.gen.genshelf.controllers.LibraryController;
import apps.gen.lib.controllers.Controller;
import apps.gen.lib.controllers.NavigationController;

public class ShelfActivity extends AppCompatActivity{

    Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        NavigationController navigationController = (NavigationController)getSupportFragmentManager().findFragmentById(R.id.navigation_controller);
        navigationController.push(Controller.instantiate(this, LibraryController.class));
    }
}
