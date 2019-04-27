package wizardofba.rezeptverwaltung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    EditText name;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveButton = (Button) findViewById(R.id.add_item_button_save);
        name = (EditText) findViewById(R.id.add_item_name);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameStr = name.getText().toString();
                if(!nameStr.equals("")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", nameStr);
                    setResult(RESULT_FIRST_USER, returnIntent);
                    finish();
                }
            }
        });
    }
}
