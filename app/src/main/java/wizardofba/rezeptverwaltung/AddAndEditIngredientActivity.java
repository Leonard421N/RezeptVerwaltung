package wizardofba.rezeptverwaltung;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wizardofba.rezeptverwaltung.Models.Ingredient;

public class AddAndEditIngredientActivity extends AppCompatActivity {

    private int CURRENT_STATE = 0;
    private final int NEW_STATE = 0;
    private final int UPDATE_STATE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 2;

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
    ImageView imageView;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_edit_ingredient);

        saveButton = (FloatingActionButton) findViewById(R.id.add_and_edit_ingredient_button_save);
        nameEditText = (EditText) findViewById(R.id.add_and_edit_ingredient_name);
        priceEditText = (EditText) findViewById(R.id.add_and_edit_ingredient_price);
        amount = (EditText) findViewById(R.id.add_and_edit_ingredient_amount);
        spinner = (Spinner) findViewById(R.id.add_and_edit_ingredient_amount_unit_spinner);
        imageView = (ImageView) findViewById(R.id.add_and_edit_ingredient_picture);

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

            int tempPosition = intent.getIntExtra("position", -1);
            Bitmap tempBitmap = MainActivity.getManager().getAllIngredientImgs().get(tempPosition);
            if(tempBitmap != null || tempPosition != -1) {
                imageView.setImageBitmap(tempBitmap);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
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

                    if(image != null && !image.equals("")) {
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

                    MainActivity.getManager().generateRecipePrices();

                    Intent returnIntent = new Intent();
                    setResult(5, returnIntent);
                    finish();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result1 = ContextCompat.checkSelfPermission(AddAndEditIngredientActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int result2 = ContextCompat.checkSelfPermission(AddAndEditIngredientActivity.this, Manifest.permission.CAMERA);
                if (result1 != PackageManager.PERMISSION_GRANTED || result2 != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddAndEditIngredientActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAndEditIngredientActivity.this.getApplicationContext(), "External Storage and Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(AddAndEditIngredientActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, WRITE_EXTERNAL_STORAGE_CODE);
                    }
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_button) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Willst du diese Zutat wirklich löschen?").setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nein", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    MainActivity.getManager().removeIngredient(mIngredient);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted -> start photo intent
                    dispatchTakePictureIntent();
                } else {
                    // permission denied
                }
                return;
            }
            case WRITE_EXTERNAL_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted -> start photo intent
                    dispatchTakePictureIntent();
                } else {
                    // permission denied
                }
                return;
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        Uri imageUri = Uri.parse(currentPhotoPath);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                this.image = currentPhotoPath;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Delete Image File when taking photo ist canceled
            File image = new File(imageUri.getPath());
            image.delete();
            if(image.exists()){
                try {
                    image.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(image.exists()){
                    getApplicationContext().deleteFile(image.getName());
                }
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = Uri.fromFile(image).toString();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
