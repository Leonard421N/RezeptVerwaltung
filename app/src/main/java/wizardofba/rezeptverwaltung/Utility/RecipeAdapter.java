package wizardofba.rezeptverwaltung.Utility;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wizardofba.rezeptverwaltung.AddItemActivity;
import wizardofba.rezeptverwaltung.MainActivity;
import wizardofba.rezeptverwaltung.Models.Recipe;
import wizardofba.rezeptverwaltung.R;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecepiAdapterViewHolder> {

    private static List<Recipe> mRecipes;

    public static class RecepiAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView name;
        public ImageView picture;
        public TextView pricetag;

        public RecepiAdapterViewHolder(final View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.picture = (ImageView) itemView.findViewById(R.id.pic);
            this.pricetag = (TextView) itemView.findViewById(R.id.price_tag);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                    intent.putExtra("id", mRecipes.get(getPosition()).getRecipeID());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecipeAdapter() {
        mRecipes = MainActivity.getManager().getAllRecipes();
    }

    public void notifyDataChanged() {
        mRecipes = MainActivity.getManager().getAllRecipes();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapter.RecepiAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        return new RecepiAdapterViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecepiAdapterViewHolder viewHolder, int i) {
        Recipe tempRecipe = mRecipes.get(i);
        viewHolder.name.setText(tempRecipe.getName());
        //TODO load pic (picasso?)
        viewHolder.pricetag.setText(String.format("%s€", tempRecipe.getPrice().toString()));
        viewHolder.picture.setImageBitmap(MediaLoader.getInstance().loadBitmapFromUri(Uri.parse(tempRecipe.getImageUri())));
    }

    public String getId(int position) {
        return mRecipes.get(position).getRecipeID();
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
