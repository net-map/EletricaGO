package map.net.eletricago.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import map.net.eletricago.R;
import map.net.eletricago.classes.Pokemon;

public class MainScreenFragment extends android.app.Fragment {

    @BindView(R.id.pokemonMiniature)
    ImageView pokemonMiniatureImageView;

    @BindView(R.id.pokemonName)
    TextView pokemonNameTextView;

    @BindView(R.id.capturedText)
    TextView capturedTextView;

    @BindView(R.id.zoneName)
    TextView zoneNameTextView;

    @BindView(R.id.captureButton)
    Button captureButton;



    public MainScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        ButterKnife.bind(this, view);


        final Pokemon pokemon = loadPokemonDataJSON("Corredor biblioteca");
        setMiniatureNameStatus(pokemon);

        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment cameraFragment = CameraFragment.newInstance(pokemon);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();


                transaction.add(R.id.mainFragment, cameraFragment);
                transaction.commit();

            }
        });



        return view;
    }

    private Pokemon loadPokemonDataJSON(String zoneName) {
        final Configuration configuration = Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();

        Storage storage = SimpleStorage.getInternalStorage(getActivity());

        String content = storage.readTextFile("pokemon_data", "pokemon_data_1.0");

        String pokemonString = JsonPath.using(configuration).parse(content).read("$.pokemons[?(@.zone == '" + zoneName + "')]").toString();

        Pokemon pokemon = new Pokemon();
        try {
            JSONObject pokemonJSON = new JSONObject(pokemonString.substring(1, pokemonString.length()-1));
            pokemon.setName(pokemonJSON.getString("name"));
            pokemon.setFile_name(pokemonJSON.getString("file_name"));
            pokemon.setZone(pokemonJSON.getString("zone"));
            pokemon.setCaptured(pokemonJSON.getBoolean("captured"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    private void setMiniatureNameStatus(Pokemon pokemon){
        try {
            Drawable drawable = Drawable.createFromStream(getActivity().getAssets().open("pokemon_miniature/"+pokemon.getFile_name()+".png"), null);
            pokemonMiniatureImageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pokemonNameTextView.setText(pokemon.getName());

        String captureStatus ="";
        if(pokemon.getCaptured()){
            captureStatus = "Já capturado!";
        }
        else{
            captureStatus = "Não capturado!";
        }
        capturedTextView.setText(captureStatus);

    }

}
