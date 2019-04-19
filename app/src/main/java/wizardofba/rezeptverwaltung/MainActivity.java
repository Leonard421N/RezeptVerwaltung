package wizardofba.rezeptverwaltung;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import wizardofba.rezeptverwaltung.Manage.RecepiAdapter;

public class MainActivity extends AppCompatActivity {

    public static int  CURRENT_STATE = 0;
    public static final int STATE_RECEPIS = 0;
    public static final int STATE_INGREDIENTS = 1;
    public static final int STATE_SETTINGS = 2;

    private RecyclerView recyclerView;
    private RecepiAdapter recepiAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

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

    private void fillRecyclerView() {

        switch(CURRENT_STATE) {
            case STATE_RECEPIS:
                break;
            case STATE_INGREDIENTS:
                break;
        }
    }

}
