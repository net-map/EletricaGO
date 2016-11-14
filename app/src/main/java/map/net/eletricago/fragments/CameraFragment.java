package map.net.eletricago.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.camview.CameraLiveView;
import map.net.eletricago.R;

/**
 * This fragment shows the camera view
 */
public class CameraFragment extends android.app.Fragment {

    @BindView(R.id.camView)
    CameraLiveView mCamera;


    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);

        mCamera.startCamera();

        return view;
    }


}
