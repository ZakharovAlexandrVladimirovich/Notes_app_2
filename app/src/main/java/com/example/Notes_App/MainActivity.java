package com.example.Notes_App;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.Notes_App.domain.AppRouteManger;
import com.example.Notes_App.domain.AppRouter;
import com.example.Notes_App.ui.auth.AuthFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements AppRouteManger {

    private AppRouter appRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appRouter = getAppRouter();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.my_notes_option) {
                appRouter.showNotesList();
                return true;
            }

            if (item.getItemId() == R.id.settings_option) {
                //TODO: setting option
                Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.about_option) {
                appRouter.showAbout();
                return true;
            }
            return false;
        });


        if (savedInstanceState == null) {
            appRouter.showAuth();
        }

        getSupportFragmentManager().setFragmentResultListener(AuthFragment.AUTH_RESULT, this, (requestKey, result) -> appRouter.showNotesList());

    }
    //TODO: Landscape orientation mode


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AppRouter getAppRouter() {
        return new AppRouter(getSupportFragmentManager());
    }
}