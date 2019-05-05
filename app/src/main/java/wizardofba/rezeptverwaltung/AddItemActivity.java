package wizardofba.rezeptverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import wizardofba.rezeptverwaltung.Models.Recipe;
import wizardofba.rezeptverwaltung.Utility.IngredientAdapter;

public class AddItemActivity extends AppCompatActivity {

    private final int COLUMN_COUNT = 3;
    private int CURRENT_STATE = 0;
    private final int NEW_STATE = 0;
    private final int UPDATE_STATE = 1;

    EditText name;
    FloatingActionButton saveButton;
    private RecyclerView recyclerView;
    private static IngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Recipe mRecipe;

    private HashMap<String, Float> ingredients;
    private HashMap<String, Float> recipes;
    private byte[] image;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveButton = (FloatingActionButton) findViewById(R.id.add_item_button_save);
        name = (EditText) findViewById(R.id.add_item_name);

        ingredientAdapter = new IngredientAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_add_item);
        layoutManager = new GridLayoutManager(this, COLUMN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(ingredientAdapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id != null && !id.equals("")) {
            CURRENT_STATE = UPDATE_STATE;
            mRecipe = MainActivity.getManager().getRecepiPerUUID(id);
            name.setText(mRecipe != null ? mRecipe.getName() : "");
        } else {
            CURRENT_STATE = NEW_STATE;
            mRecipe = new Recipe();
            ingredients = new HashMap<>();
            recipes = new HashMap<>();
            description = "";
        }

        name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    name.clearFocus();
                }
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameStr = name.getText().toString();

                if(!nameStr.equals("")) {
                    mRecipe.setName(nameStr);
                    if(recipes != null && recipes.size() != 0) {
                        mRecipe.setRecipes(recipes);
                    }
                    if(ingredients != null && ingredients.size() != 0) {
                        mRecipe.setIngredients(ingredients);
                    }
                    if(image != null && image != null) {
                        mRecipe.setImage(image);
                    }
                    if(description != null && !description.equals("")) {
                        mRecipe.setDescription(description);
                    }

                    switch (CURRENT_STATE) {

                        case NEW_STATE:
                            MainActivity.getManager().addRecepi(mRecipe);
                            break;
                        case UPDATE_STATE:
                            MainActivity.getManager().updateRecipe(mRecipe);
                            break;
                    }

                    Intent returnIntent = new Intent();
                    setResult(RESULT_FIRST_USER, returnIntent);
                    finish();
                }
            }
        });
    }
}
