package map.net.eletricago.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jayway.jsonpath.JsonPath;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import map.net.eletricago.R;
import map.net.eletricago.classes.Pokemon.Pokemon;
import map.net.eletricago.classes.Pokemon.PokemonAdapter;

public class ListFragment extends android.app.Fragment{

    @BindView(R.id.pokedexList)
    ListView pokedexListView;

    @BindView(R.id.backButton)
    Button backButton;


    public ListFragment(){
        // Must be empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Fragment mainScreenFragment = new MainScreenFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();


                transaction.add(R.id.mainFragment, mainScreenFragment);
                transaction.commit();

            }
        });

        Storage storage = SimpleStorage.getInternalStorage(getActivity());

        String content = storage.readTextFile("pokemon_data", "pokemon_data_1.0");

        List<Pokemon>  pokemonList = new ArrayList<>();

        int listLenght = JsonPath.parse(content).read("$.pokemons.length()");

        final PokemonAdapter adapter = new PokemonAdapter(getActivity(), pokemonList);

        for(int i =0; i< listLenght; i++){
            try {
                JSONObject pokemonJSONInPosition = new JSONObject(JsonPath.parse(content).read("$.pokemons["+String.valueOf(i)+"]").toString());
                Pokemon pokemonInPosition = new Pokemon();
                pokemonInPosition.setName(pokemonJSONInPosition.getString("name"));
                pokemonInPosition.setFile_name(pokemonJSONInPosition.getString("file_name"));
                pokemonInPosition.setZone(pokemonJSONInPosition.getString("zone"));
                pokemonInPosition.setCaptured(pokemonJSONInPosition.getBoolean("captured"));
                pokemonList.add(pokemonInPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pokedexListView.setAdapter(adapter);


        return view;
    }
}
