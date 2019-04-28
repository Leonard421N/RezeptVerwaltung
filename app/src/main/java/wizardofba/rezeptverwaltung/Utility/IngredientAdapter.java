package wizardofba.rezeptverwaltung.Utility;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Ingredient;
import wizardofba.rezeptverwaltung.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private static List<Ingredient> mIngredients;

    public static class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView name;
        public ImageView picture;
        public TextView amount;

        public IngredientAdapterViewHolder(final View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view_grid);
            this.name = (TextView) itemView.findViewById(R.id.card_view_grid_item_name);
            this.picture = (ImageView) itemView.findViewById(R.id.card_view_grid_pic);
            this.amount = (TextView) itemView.findViewById(R.id.card_view_grid_amount);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * JUST FOR TESTING
                     * */
                    //MainActivity.getManager().removeRecepi(mRecepis.get(getPosition()));
                    //MainActivity.notifyUpdate();

                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public IngredientAdapter() {
        mIngredients = MainActivity.getManager().getAllIngredients();
    }

    public void notifyDataChanged() {
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
        viewHolder.name.setText(mIngredients.get(i).getName());
        //TODO load pic (picasso?)
        viewHolder.amount.setText(String.format("%s ml", mIngredients.get(i).getPrice().first.toString()));
    }

    public String getId(int position) {
        return mIngredients.get(position).getIngredientID();
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }
}