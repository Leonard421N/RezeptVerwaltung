package wizardofba.rezeptverwaltung;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wizardofba.rezeptverwaltung.Models.Recipe;
import wizardofba.rezeptverwaltung.Utility.IngredientAdapter;

public class AddItemActivity extends AppCompatActivity {

    private final int COLUMN_COUNT = 3;
    private int CURRENT_STATE = 0;
    public static final int NEW_STATE = 0;
    public static final int UPDATE_STATE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    EditText name;
    FloatingActionButton saveButton;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private static IngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Recipe mRecipe;

    private HashMap<String, Float> ingredients;
    private HashMap<String, Float> recipes;
    private byte[] image;
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
                //open camera or gallery
                if (ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddItemActivity.this,
                            Manifest.permission.CAMERA)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(AddItemActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    }
                }
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

}

