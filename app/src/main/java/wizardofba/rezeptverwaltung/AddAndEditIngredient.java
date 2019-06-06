package wizardofba.rezeptverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import wizardofba.rezeptverwaltung.Models.Ingredient;

public class AddAndEditIngredient extends AppCompatActivity {

    private int CURRENT_STATE = 0;
    private final int NEW_STATE = 0;
    private final int UPDATE_STATE = 1;
    Ingredient mIngredient;


    private Pair<Float, Float> price;
    private String image;
    private String description;

    EditText nameEditText;
    EditText priceEditText;
    EditText amount;
    FloatingActionButton saveButton;
    Spinner spinner;
    String[] tempStrArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_ingredient);

        saveButton = (FloatingActionButton) findViewById(R.id.add_and_edit_ingredient_button_save);
        nameEditText = (EditText) findViewById(R.id.add_and_edit_ingredient_name);
        priceEditText = (EditText) findViewById(R.id.add_and_edit_ingredient_price);
        amount = (EditText) findViewById(R.id.add_and_edit_ingredient_amount);
        spinner = (Spinner) findViewById(R.id.add_and_edit_ingredient_amount_unit_spinner);

        tempStrArray = getResources().getStringArray(R.array.string_array_units);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.string_array_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id != null && !id.equals("")) {
            CURRENT_STATE = UPDATE_STATE;

            mIngredient = MainActivity.getManager().getIngredientPerUUID(id);
            nameEditText.setText(mIngredient != null ? mIngredient.getName() : "");
            amount.setText(mIngredient != null ? mIngredient.getPrice().first.toString() : "");
            priceEditText.setText(mIngredient != null ? mIngredient.getPrice().second.toString() : "");

            int i = 0;
            for(; i < tempStrArray.length; i++) {
                if(mIngredient.getUnit().equals(tempStrArray[i])) {
                    spinner.setSelection(i, true);
                    i = tempStrArray.length;
                }
            }

        } else {
            CURRENT_STATE = NEW_STATE;
            mIngredient = new Ingredient();
            description = "";
            spinner.setSelection(2, true);
        }

        nameEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    //Clear focus here from edittext
                    nameEditText.clearFocus();
                }
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String nameStr = nameEditText.getText().toString();
                float priceFloat;
                float amountFloat;

                try {
                     priceFloat = Float.valueOf(priceEditText.getText().toString());
                     amountFloat = Float.valueOf(amount.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    priceFloat = 0f;
                    amountFloat = 0f;
                }

                if(!nameStr.equals("")) {

                    mIngredient.setName(nameStr);
                    mIngredient.setPrice(new Pair<Float, Float>(amountFloat, priceFloat));
                    mIngredient.setUnit(spinner.toString());

                    if(image != null && image != null) {
                        mIngredient.setImageUri(image);
                    }

                    if(description != null && !description.equals("")) {
                        mIngredient.setDescription(description);
                    }

                    mIngredient.setUnit(tempStrArray[spinner.getSelectedItemPosition()]);

                    switch (CURRENT_STATE) {

                        case NEW_STATE:
                            MainActivity.getManager().addIngredient(mIngredient);
                            break;
                        case UPDATE_STATE:
                            MainActivity.getManager().updateIngredient(mIngredient);
                            break;
                    }

                    Intent returnIntent = new Intent();
                    setResult(5, returnIntent);
                    finish();
                }
            }
        });
    }
}
