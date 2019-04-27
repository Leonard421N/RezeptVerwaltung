package wizardofba.rezeptverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import wizardofba.rezeptverwaltung.Manage.Manager;
import wizardofba.rezeptverwaltung.Manage.RecepiAdapter;
import wizardofba.rezeptverwaltung.Models.Recepi;

public class MainActivity extends AppCompatActivity {

    public static int  CURRENT_STATE = 0;
    public static final int STATE_RECEPIS = 0;
    public static final int STATE_INGREDIENTS = 1;
    public static final int STATE_SETTINGS = 2;

    private static Manager manager;
    private RecyclerView recyclerView;
    private static RecepiAdapter recepiAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = Manager.getInstance(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addFab = (FloatingActionButton) findViewById(R.id.fab_add);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(CURRENT_STATE) {

                    case STATE_RECEPIS:
                        
                        getManager().addRecepi(new Recepi("Kekse", 2.1f));
                        recepiAdapter.notifyDataChanged();
                        recyclerView.scrollToPosition(recepiAdapter.getItemCount());
                        /*
                        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                        startActivity(intent);
                        */
                        break;

                    case STATE_INGREDIENTS:
                        break;

                }
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recepiAdapter = new RecepiAdapter();
        //TODO load Data
        recyclerView.setAdapter(recepiAdapter);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.recepis:
                    CURRENT_STATE = STATE_RECEPIS;
                    fillRecyclerView();
                    return true;
                case R.id.ingredients:
                    CURRENT_STATE = STATE_INGREDIENTS;
                    fillRecyclerView();
                    return true;
                case R.id.settings:
                    CURRENT_STATE = STATE_SETTINGS;
                    return true;
            }
            return false;
        }
    };

    public static void notifyUpdate() {
        recepiAdapter.notifyDataChanged();
    }

    public static Manager getManager() {
        return manager;
    }

    private void fillRecyclerView() {

        switch(CURRENT_STATE) {
            case STATE_RECEPIS:
                break;
            case STATE_INGREDIENTS:
                break;
        }
    }

}
