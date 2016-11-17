package map.net.eletricago.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.camview.CameraLiveView;
import map.net.eletricago.R;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.InputSource;

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

        setPokemonGif("a");

        return view;
    }

    void setPokemonGif(String pokemonName){
        mPokemonGifImageView.setImageResource(R.drawable.pikachu);
    }


}
