package wizardofba.rezeptverwaltung;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import wizardofba.rezeptverwaltung.manage.Manager;
import wizardofba.rezeptverwaltung.utility.Animation;
import wizardofba.rezeptverwaltung.utility.IngredientAdapter;
import wizardofba.rezeptverwaltung.utility.RecipeAdapter;

public class MainActivity extends AppCompatActivity {

    public static int  CURRENT_STATE = 0;
    public static final int STATE_RECEPIS = 0;
    public static final int STATE_INGREDIENTS = 1;
    public static final int STATE_SETTINGS = 2;

    private static Manager manager;
    private RecyclerView recyclerView;
    private static RecipeAdapter recipeAdapter;
    private static IngredientAdapter ingredientAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAppIcon();

        manager = Manager.getInstance(this);

        recipeAdapter = new RecipeAdapter();
        ingredientAdapter = new IngredientAdapter();

        recyclerView = findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeAdapter);

        addFab = findViewById(R.id.fab_add);

        addFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;

                switch(CURRENT_STATE) {

                    case STATE_RECEPIS:
                        intent = new Intent(MainActivity.this,
                                AddAndEditRecipeActivity.class);
                        startActivityForResult(intent, RESULT_FIRST_USER);
                        break;

                    case STATE_INGREDIENTS:
                        intent = new Intent(MainActivity.this,
                                AddAndEditIngredientActivity.class);
                        startActivityForResult(intent, 5);
                        break;

                }
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setAppIcon() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        setTaskDescription( new ActivityManager.TaskDescription(null, icon));
    }

    @Override
    protected void onResume() {
        super.onResume();
        IngredientAdapter.setCurrentState(IngredientAdapter.BASE_STATE);
        fillRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_FIRST_USER){
            //manager.addRecepi(new Recipe(data.getStringExtra("nameEditText")));
        } else if(resultCode == 5) {

        }
        notifyUpdate();
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
                    /*
                case R.id.settings:
                    CURRENT_STATE = STATE_SETTINGS;
                    return true;
                    */
            }
            return false;
        }
    };

    public static void notifyUpdate() {

        switch(CURRENT_STATE) {

            case STATE_RECEPIS:
                recipeAdapter.notifyDataChanged();
                break;
            case STATE_INGREDIENTS:
                ingredientAdapter.notifyDataChanged();
                break;
        }
    }

    public static Manager getManager() {
        return manager;
    }

    public Resources getSomeResources() {
        return getResources();
    }

    private void fillRecyclerView() {

        switch(CURRENT_STATE) {
            case STATE_RECEPIS:
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recipeAdapter);
                break;
            case STATE_INGREDIENTS:
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(ingredientAdapter);
                break;
        }
        notifyUpdate();
    }

}
