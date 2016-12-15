package map.net.eletricago.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNode;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.camview.CameraLiveView;
import map.net.eletricago.R;
import map.net.eletricago.classes.Pokemon;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * This fragment shows the camera view
 */
public class CameraFragment extends android.app.Fragment {

    @BindView(R.id.camView)
    CameraLiveView mCamera;

    @BindView(R.id.pokemonGifImageView)
    GifImageView mPokemonGifImageView;

    @BindView(R.id.pokeballImageButton)
    ImageButton pokeballImageButton;


    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(Pokemon pokemon) {
        CameraFragment fragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pokemon", pokemon);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);

        mCamera.startCamera();

        final Pokemon mPokemon = (Pokemon) getArguments().getSerializable(
                "pokemon");

        setPokemonGif(mPokemon.getFile_name());

        pokeballImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                new MaterialDialog.Builder(getActivity())
                        .title("CAPTURADO!")
                        .content("Parabéns! Você capturou um " + mPokemon.getName())
                        .positiveText("OK")
                        .show();


                updateCapturedStatus(mPokemon);


                Fragment mainScreenFragment = new MainScreenFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();


                transaction.add(R.id.mainFragment, mainScreenFragment);
                transaction.commit();

            }
        });

        return view;
    }

    void setPokemonGif(String pokemonName) {

        GifDrawable gifFromAssets = null;
        try {
            gifFromAssets = new GifDrawable(getResources().getAssets(), "pokemon_big/" + pokemonName + ".gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPokemonGifImageView.setImageDrawable(gifFromAssets);
    }

    void updateCapturedStatus(Pokemon pokemon) {
        final Configuration configuration = Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();

        Storage storage = SimpleStorage.getInternalStorage(getActivity());

        String content = storage.readTextFile("pokemon_data", "pokemon_data_1.0");

        com.fasterxml.jackson.databind.node.ObjectNode updatedJson = JsonPath.using(configuration).parse(content)
                .set("$.pokemons[?(@.name == '" + pokemon.getName() + "')].captured", true).json();

        storage.deleteFile("pokemon_data", "pokemon_data_1.0");
        storage.createFile("pokemon_data", "pokemon_data_1.0",updatedJson.toString());
    }


}
