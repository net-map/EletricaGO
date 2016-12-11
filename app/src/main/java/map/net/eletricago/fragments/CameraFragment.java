package map.net.eletricago.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.camview.CameraLiveView;
import map.net.eletricago.R;
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


    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);

        mCamera.startCamera();

        setPokemonGif("pikachu");

        return view;
    }

    void setPokemonGif(String pokemonName){

        GifDrawable gifFromAssets = null;
        try {
            gifFromAssets = new GifDrawable( getResources().getAssets(), "pokemon_big/"+pokemonName+".gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPokemonGifImageView.setImageDrawable(gifFromAssets);
    }


}
