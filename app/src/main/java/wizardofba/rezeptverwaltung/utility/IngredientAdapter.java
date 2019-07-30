package wizardofba.rezeptverwaltung.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import wizardofba.rezeptverwaltung.AddAndEditIngredientActivity;
import wizardofba.rezeptverwaltung.AddAndEditRecipeActivity;
import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.R;
import wizardofba.rezeptverwaltung.models.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private static List<Ingredient> mIngredients;
    private static List<Bitmap> mIngredientImgs;
    private static LinkedHashMap<Ingredient, Float> mCustomIngredients;
    private static Ingredient ingredientToDelete = null;
    private static Context context;

    private static int CURRENT_STATE = 0;
    public static final int BASE_STATE = 0;
    public static final int CUSTOM_STATE = 1;

    public static class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView name;
        public ImageView picture;
        public TextView amount;
        public TextView price;

        public IngredientAdapterViewHolder(final View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view_grid);
            this.name = (TextView) itemView.findViewById(R.id.card_view_grid_item_name);
            this.picture = (ImageView) itemView.findViewById(R.id.card_view_grid_pic);
            this.amount = (TextView) itemView.findViewById(R.id.card_view_grid_amount);
            this.price = (TextView) itemView.findViewById(R.id.card_view_grid_price);

            cardView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    if (CURRENT_STATE == BASE_STATE) {

                        Intent intent = new Intent(v.getContext(), AddAndEditIngredientActivity.class);
                        intent.putExtra("id", mIngredients.get(getPosition()).getIngredientID());
                        intent.putExtra("position", getPosition());
                        v.getContext().startActivity(intent);
                    } else if (CURRENT_STATE == CUSTOM_STATE) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setMessage("Gib eine Menge ein").setTitle("Zutaten-Menge");
                        builder.setView(R.layout.select_amount);

                        builder.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                EditText editAmountText = ((AlertDialog) dialog)
                                        .findViewById(R.id.select_amount_amount);
                                ArrayList<Ingredient> tempIngredients
                                        = new ArrayList<>(mCustomIngredients.keySet());
                                Ingredient tempIngredient = tempIngredients.get(getPosition());
                                Float tempAmount = Float.valueOf(editAmountText.getText().toString());
                                mCustomIngredients.put(tempIngredient, tempAmount);
                                AddAndEditRecipeActivity.getInstance()
                                        .updateIngredient(tempIngredient, tempAmount);
                            }
                        });
                        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        EditText editAmountText = ((AlertDialog) dialog)
                                .findViewById(R.id.select_amount_amount);
                        TextView amountUnit = ((AlertDialog) dialog)
                                .findViewById(R.id.select_amount_unit);
                        ArrayList<Float> tempAmounts = new ArrayList<>(mCustomIngredients.values());
                        ArrayList<Ingredient> tempIngredients = new ArrayList<>(mCustomIngredients.keySet());
                        Ingredient tempIngredient = tempIngredients.get(getPosition());
                        if(editAmountText != null && amountUnit != null) {
                            editAmountText.setText(tempAmounts.get(getPosition()).toString());
                            amountUnit.setText(tempIngredient.getUnit());
                        }
                    }
                }
            });

            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if(ingredientToDelete != null) {
                                AddAndEditRecipeActivity.getInstance().removeIngredient(ingredientToDelete);
                                int i = 0;
                                for(Ingredient ingredient: mCustomIngredients.keySet()) {
                                    if(ingredient.getIngredientID().equals(ingredientToDelete.getIngredientID())) {
                                        mIngredientImgs.remove(i);
                                        break;
                                    }
                                    i++;
                                }
                                mCustomIngredients.remove(ingredientToDelete);
                            }
                            ingredientToDelete = null;
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(CURRENT_STATE != BASE_STATE) {
                        ArrayList<Ingredient> tempIngredients = new ArrayList<>(mCustomIngredients.keySet());
                        if (tempIngredients.size() > 0) {
                            ingredientToDelete = tempIngredients.get(getPosition());
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Willst du die Zutat aus dem Rezept nehmen?")
                                    .setPositiveButton("Ja", dialogClickListener)
                                    .setNegativeButton("Nein", dialogClickListener).show();
                        }
                    }
                    return false;
                }
            });
        }
    }

    public IngredientAdapter() {
        CURRENT_STATE = BASE_STATE;
        mIngredientImgs = MainActivity.getManager().getAllIngredientImgs();
        mIngredients = MainActivity.getManager().getAllIngredients();
    }

    public IngredientAdapter(HashMap<String, Float> customIngredients, Context context) {
        CURRENT_STATE = CUSTOM_STATE;
        this.context = context;
        mCustomIngredients = MainActivity.getManager().createCustomIngredientHashMap(customIngredients);
        mIngredientImgs = MainActivity.getManager().loadAllCustomIngredientBitmaps(mCustomIngredients);
    }

    public void notifyDataChanged() {
        switch (CURRENT_STATE) {
            case BASE_STATE:
                mIngredientImgs = MainActivity.getManager().getAllIngredientImgs();
                mIngredients = MainActivity.getManager().getAllIngredients();
                break;
            case CUSTOM_STATE:
                mIngredientImgs = MainActivity.getManager().loadAllCustomIngredientBitmaps(mCustomIngredients);
                //remove imgs manually
                break;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_cardview, viewGroup, false);
        return new IngredientAdapterViewHolder(v);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientAdapterViewHolder viewHolder, int i) {
        Ingredient tempIngredient;
        switch (CURRENT_STATE) {

            case BASE_STATE:
                tempIngredient = mIngredients.get(i);
                viewHolder.name.setText(tempIngredient.getName());
                viewHolder.amount.setText(String.format("%s " + tempIngredient.getUnit(), tempIngredient.getPriceAndAmount().first.toString()));
                viewHolder.price.setText(String.format("%s €", tempIngredient.getPriceAndAmount().second.toString()));
                break;

            case CUSTOM_STATE:
                if(mCustomIngredients != null && mCustomIngredients.size() > 0) {
                    ArrayList<Ingredient> tempCustomIngredients = new ArrayList<>(mCustomIngredients.keySet());
                    tempIngredient = tempCustomIngredients.get(i);
                    viewHolder.name.setText(tempIngredient.getName());
                    viewHolder.amount.setText(String.format("%s " + tempIngredient.getUnit(), mCustomIngredients.get(tempIngredient)));
                    viewHolder.price.setText(String.format("%.2f €",
                            tempIngredient.getBasePrice()*mCustomIngredients.get(tempIngredient)));
                }
                break;
        }
        if(mIngredientImgs.size() > 0) {
            Bitmap tempBitmap = mIngredientImgs.get(i);
            if (tempBitmap != null) {
                viewHolder.picture.setImageBitmap(tempBitmap);
            }
        }
    }

    public String getId(int position) {

        String res = "";
        switch (CURRENT_STATE) {
            case BASE_STATE:
                res = mIngredients.get(position).getIngredientID();
                break;
            case CUSTOM_STATE:
                ArrayList<Ingredient> tempCustomIngredients = new ArrayList<>(mCustomIngredients.keySet());
                res = tempCustomIngredients.get(position).getIngredientID();
                break;
        }
        return res;
    }

    @Override
    public int getItemCount() {
        int res = 0;
        switch (CURRENT_STATE) {
            case BASE_STATE:
                res = mIngredients.size();
                break;
            case CUSTOM_STATE:
                res = mCustomIngredients.size();
                break;
        }
        return res;
    }

    public static void setCurrentState(int currentState) {
        CURRENT_STATE = currentState;
    }

    public void setCustomIngredients(HashMap<String, Float> customIngredients) {
        mCustomIngredients = MainActivity.getManager().createCustomIngredientHashMap(customIngredients);
        mIngredientImgs = MainActivity.getManager().loadAllCustomIngredientBitmaps(mCustomIngredients);
    }
}