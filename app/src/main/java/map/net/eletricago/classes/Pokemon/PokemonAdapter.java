package map.net.eletricago.classes.Pokemon;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import map.net.eletricago.R;

import static android.graphics.Color.BLACK;

public class PokemonAdapter extends ArrayAdapter<Pokemon> {


    public PokemonAdapter(Context context, List<Pokemon> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Pokemon pokemon = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pokedex_list_item, parent, false);
        }

        /* Set up elements for view */
        TextView zoneName = (TextView) convertView.findViewById(R.id.zoneName);
        if (pokemon != null) {
            zoneName.setText( pokemon.getZone());
        }

        TextView pokemonName = (TextView) convertView.findViewById(R.id.pokemonName);
        ImageView miniature = (ImageView) convertView.findViewById(R.id.pokemonMiniature);

        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(getContext().getAssets().open("pokemon_miniature/" + pokemon.getFile_name() + ".png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(pokemon.getCaptured()){
            pokemonName.setText(pokemon.getName());

        }
        else{
            pokemonName.setText("????????");
            if (drawable != null) {
                drawable.mutate().setColorFilter( BLACK, PorterDuff.Mode.MULTIPLY);
            }
        }

        miniature.setImageDrawable(drawable);



        return convertView;
    }
}