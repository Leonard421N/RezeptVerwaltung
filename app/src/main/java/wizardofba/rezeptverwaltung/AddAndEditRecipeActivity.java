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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import wizardofba.rezeptverwaltung.Models.Recipe;
import wizardofba.rezeptverwaltung.Utility.IngredientAdapter;

public class AddAndEditRecipeActivity extends AppCompatActivity {

    private final int COLUMN_COUNT = 3;
    private int CURRENT_STATE = 0;
    public static final int NEW_STATE = 0;
    public static final int UPDATE_STATE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 2;

    EditText name;
    FloatingActionButton saveButton;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private static IngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Recipe mRecipe;

    private HashMap<String, Float> ingredients;
    private HashMap<String, Float> recipes;
    private String image;
    String currentPhotoPath;
    private List<Float> amounts = new ArrayList<>();
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveButton = (FloatingActionButton) findViewById(R.id.add_item_button_save);
        name = (EditText) findViewById(R.id.add_item_name);
        imageView = (ImageView) findViewById(R.id.add_item_picture);

        ingredientAdapter = new IngredientAdapter(IngredientAdapter.CUSTOM_STATE);
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
            image = mRecipe.getImageUri();
            if(image != null) {
                Uri imageUri = Uri.parse(image);
                try {
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri));
                } catch (IOException e) {
                    //NO IMAGE SAVED -> OKAY
                }
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int result1 = ContextCompat.checkSelfPermission(AddAndEditRecipeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int result2 = ContextCompat.checkSelfPermission(AddAndEditRecipeActivity.this, Manifest.permission.CAMERA);
                if (result1 != PackageManager.PERMISSION_GRANTED || result2 != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddAndEditRecipeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Toast.makeText(AddAndEditRecipeActivity.this.getApplicationContext(), "External Storage and Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(AddAndEditRecipeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, WRITE_EXTERNAL_STORAGE_CODE);
                    }
                } else {
                    dispatchTakePictureIntent();
                }
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
                    if(image != null) {
                        mRecipe.setImageUri(image);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_button) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Willst du das Rezept wirklich löschen?").setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nein", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    MainActivity.getManager().removeRecepi(mRecipe);
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

