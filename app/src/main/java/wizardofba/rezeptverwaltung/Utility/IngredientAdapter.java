package wizardofba.rezeptverwaltung.Utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizardofba.rezeptverwaltung.AddAndEditIngredientActivity;
import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private static List<Ingredient> mIngredients;
    private static List<Bitmap> mIngredientImgs;

    private List<Float> amounts;
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
                @Override
                public void onClick(View v) {
                    if(CURRENT_STATE == BASE_STATE) {
                        Intent intent = new Intent(v.getContext(), AddAndEditIngredientActivity.class);
                        intent.putExtra("id", mIngredients.get(getPosition()).getIngredientID());
                        intent.putExtra("position", getPosition());
                        v.getContext().startActivity(intent);
                    } else if (CURRENT_STATE == CUSTOM_STATE) {

                    }
                }
            });
        }
    }

    public IngredientAdapter(int STATE_FLAG) {
        CURRENT_STATE = STATE_FLAG;
        mIngredientImgs = MainActivity.getManager().getAllIngredientImgs();
        mIngredients = MainActivity.getManager().getAllIngredients();
    }

    public void notifyDataChanged() {
        mIngredientImgs = MainActivity.getManager().getAllIngredientImgs();
        mIngredients = MainActivity.getManager().getAllIngredients();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_cardview, viewGroup, false);
        return new IngredientAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientAdapterViewHolder viewHolder, int i) {
        Ingredient tempIngredient = mIngredients.get(i);
        viewHolder.name.setText(tempIngredient.getName());
        if(mIngredientImgs.size() > 0) {
            Bitmap tempBitmap = mIngredientImgs.get(i);
            if (tempBitmap != null) {
                viewHolder.picture.setImageBitmap(tempBitmap);
            }
        }
        //TODO load pic (picasso?)
        switch (CURRENT_STATE) {

            case BASE_STATE:
                viewHolder.amount.setText(String.format("%s " + tempIngredient.getUnit(), tempIngredient.getPrice().first.toString()));
                viewHolder.price.setText(String.format("%s €", tempIngredient.getPrice().second.toString()));
                break;

            case CUSTOM_STATE:
                viewHolder.amount.setText(String.format("%s " + tempIngredient.getUnit(), tempIngredient.getPrice().first.toString()));
                viewHolder.price.setText(String.format("%s €", tempIngredient.getPrice().second.toString()));
                break;

        }
    }

    public String getId(int position) {
        return mIngredients.get(position).getIngredientID();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public static void setCurrentState(int currentState) {
        CURRENT_STATE = currentState;
    }
}