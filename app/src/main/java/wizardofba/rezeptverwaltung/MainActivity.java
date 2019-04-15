package wizardofba.rezeptverwaltung;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import wizardofba.rezeptverwaltung.Manage.ItemAdapter;

public class MainActivity extends AppCompatActivity {

    public static final int STATE_RECEPIS = 0;
    public static final int STATE_INGREDIENTS = 1;

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.recepis:
                    fillRecyclerView(STATE_RECEPIS);
                    return true;
                case R.id.ingredients:
                    fillRecyclerView(STATE_INGREDIENTS);
                    return true;
                case R.id.settings:
                    return true;
            }
            return false;
        }
    };

    private void fillRecyclerView(int state) {

        switch(state) {
            case STATE_RECEPIS:
                break;
            case STATE_INGREDIENTS:
                break;
        }
    }

}
