package wizardofba.rezeptverwaltung.Manage;

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
import wizardofba.rezeptverwaltung.Models.Recepi;
import wizardofba.rezeptverwaltung.R;

public class RecepiAdapter extends RecyclerView.Adapter<RecepiAdapter.RecepiAdapterViewHolder> {

    private List<Recepi> mRecepis;

    public static class RecepiAdapterViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public TextView name;
        public ImageView picture;
        public TextView pricetag;

        public RecepiAdapterViewHolder(View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.name = (TextView) itemView.findViewById(R.id.item_name);
            this.picture = (ImageView) itemView.findViewById(R.id.pic);
            this.pricetag = (TextView) itemView.findViewById(R.id.price_tag);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecepiAdapter() {
        mRecepis = MainActivity.getManager().getAllRecepis();
    }

    public void notifyDataChanged() {
        mRecepis = MainActivity.getManager().getAllRecepis();
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecepiAdapter.RecepiAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);
        return new RecepiAdapterViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull RecepiAdapter.RecepiAdapterViewHolder viewHolder, int i) {
        viewHolder.name.setText(mRecepis.get(i).getName());
        //TODO load pic (picasso?)
        viewHolder.pricetag.setText(String.format("%s€", mRecepis.get(i).getPrice().toString()));
    }

    public String getId(int position) {
        return mRecepis.get(position).getRecepiID();
    }

    @Override
    public int getItemCount() {
        return mRecepis.size();
    }
}
