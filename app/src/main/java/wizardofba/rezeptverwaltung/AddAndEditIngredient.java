package wizardofba.rezeptverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddAndEditIngredient extends AppCompatActivity {

    EditText name;
    EditText price;
    EditText amount;
    FloatingActionButton saveButton;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_ingredient);

        saveButton = (FloatingActionButton) findViewById(R.id.add_and_edit_ingredient_button_save);
        name = (EditText) findViewById(R.id.add_and_edit_ingredient_name);
        price = (EditText) findViewById(R.id.add_and_edit_ingredient_price);
        amount = (EditText) findViewById(R.id.add_and_edit_ingredient_amount);
        spinner = (Spinner) findViewById(R.id.add_and_edit_ingredient_amount_unit_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.string_array_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(2, true);

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
                float priceFloat;
                float amountFloat;

                try {
                     priceFloat = Float.valueOf(price.getText().toString());
                     amountFloat = Float.valueOf(amount.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    priceFloat = 0f;
                    amountFloat = 0f;
                }

                if(!nameStr.equals("")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", nameStr);
                    returnIntent.putExtra("price", priceFloat);
                    returnIntent.putExtra("amount", amountFloat);

                    setResult(5, returnIntent);
                    finish();
                }
            }
        });
    }
}
