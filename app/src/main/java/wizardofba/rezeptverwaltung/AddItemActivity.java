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

import java.util.ArrayList;

import wizardofba.rezeptverwaltung.Utility.IngredientAdapter;

public class AddItemActivity extends AppCompatActivity {

    private final int COLUMN_COUNT = 3;
    EditText name;
    FloatingActionButton saveButton;
    private RecyclerView recyclerView;
    private static IngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<String> ingredientIDs = new ArrayList<>();
    ArrayList<Float> ingredientAmounts = new ArrayList<>();
    ArrayList<String> recipeIDs = new ArrayList<>();
    ArrayList<Float> recipeAmounts = new ArrayList<>();

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

        name.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
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
                String[] ingredientIDsTemp = (String[]) ingredientIDs.toArray();
                Float[] ingredientAmountsTemp = (Float[]) ingredientAmounts.toArray();
                String[] recipeIDsTemp = (String[]) recipeIDs.toArray();
                Float[] recipeAmountsTemp = (Float[]) recipeAmounts.toArray();

                if(!nameStr.equals("")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", nameStr);
                    returnIntent.putExtra("ingredients", ingredientIDsTemp);
                    returnIntent.putExtra("ingredientAmounts", ingredientAmountsTemp);
                    returnIntent.putExtra("recipe", recipeIDsTemp);
                    returnIntent.putExtra("recipeAmounts", recipeAmountsTemp);

                    setResult(RESULT_FIRST_USER, returnIntent);
                    finish();
                }
            }
        });
    }
}
