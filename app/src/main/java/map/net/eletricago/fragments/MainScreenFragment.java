package map.net.eletricago.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import map.net.eletricago.R;
import map.net.eletricago.classes.Pokemon.Pokemon;
import map.net.eletricago.classes.ResultResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.buttonPokedex)
    Button pokedexButton;

    WifiManager wManager;
    String currentZone = "";

    Pokemon pokemon;
    AcquireCurrentZoneFromServer acquireCurrentZoneFromServer;
    CountDownTimer timer;


    public MainScreenFragment() {
        // Required empty public constructor
    }

    public CountDownTimer setTimer(final float totalTime, final OkHttpClient client) {

        return new CountDownTimer((long) (totalTime * 1000), 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (totalTime * 1000 - millisUntilFinished));
                if(currentZone.equals("")){
                    captureButton.setVisibility(View.GONE);
                    pokemonNameTextView.setVisibility(View.GONE);
                    capturedTextView.setVisibility(View.GONE);
                    zoneNameTextView.setText("AGUARDANDO SERVIDOR");

                }
                else{
                    captureButton.setVisibility(View.VISIBLE);
                    pokemonNameTextView.setVisibility(View.VISIBLE);
                    capturedTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {

                if (wManager.startScan()) {

                    try {
                        acquireCurrentZoneFromServer.run(client, wManager.getScanResults());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                this.cancel();

            }


        }.start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        ButterKnife.bind(this, view);



        final OkHttpClient client = new OkHttpClient();
        wManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        acquireCurrentZoneFromServer = new AcquireCurrentZoneFromServer();

        try {
            acquireCurrentZoneFromServer.run(client, wManager.getScanResults());
        } catch (Exception e) {
            e.printStackTrace();
        }

        progressBar.setMax((int) (2.0f * 1000));



        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }

                Fragment cameraFragment = CameraFragment.newInstance(pokemon);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.add(R.id.mainFragment, cameraFragment);
                transaction.commit();

            }
        });

        pokedexButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }


                Fragment listFragment = new ListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.add(R.id.mainFragment, listFragment);
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
            JSONObject pokemonJSON = new JSONObject(pokemonString.substring(1, pokemonString.length() - 1));
            pokemon.setName(pokemonJSON.getString("name"));
            pokemon.setFile_name(pokemonJSON.getString("file_name"));
            pokemon.setZone(pokemonJSON.getString("zone"));
            pokemon.setCaptured(pokemonJSON.getBoolean("captured"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pokemon;
    }

    private void setMiniatureNameStatus(Pokemon pokemon) {
        try {
            Drawable drawable = Drawable.createFromStream(getActivity().getAssets().open("pokemon_miniature/" + pokemon.getFile_name() + ".png"), null);
            pokemonMiniatureImageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pokemonNameTextView.setText(pokemon.getName());

        String captureStatus = "";
        if (pokemon.getCaptured()) {
            captureStatus = "Já capturado!";
        } else {
            captureStatus = "Não capturado!";
        }
        capturedTextView.setText(captureStatus);

    }

    private class AcquireCurrentZoneFromServer {

        void run(final OkHttpClient client, List<ScanResult> accessPoints) throws Exception {



            JSONObject requestBodyJSON = new JSONObject();
            JSONObject apJSON;
            JSONArray acquisitionsJSONArray = new JSONArray();

            requestBodyJSON.put("facility_id", "58516e3cbde5c66ca85ab58f");
            for (ScanResult ap : accessPoints) {
                apJSON = new JSONObject();
                apJSON.put("BSSID", ap.BSSID);
                apJSON.put("RSSI", ap.level);
                acquisitionsJSONArray.put(apJSON);
            }

            requestBodyJSON.put("access_points", acquisitionsJSONArray);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(requestBodyJSON));

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.predict_zone_url))
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getActivity(),
                                    "Something went wrong, try again later", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getActivity(),
                                        "Something went wrong, try again later", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Unexpected code " + response);
                    }

                    currentZone = response.body().string();
                    currentZone = currentZone.substring(2, currentZone.length() - 2);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ResultResponse resultResponse = new ResultResponse();
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(currentZone.replace("\\", ""));
                                resultResponse.setZonaName(jsonObject.getString("ZonaName"));
                                resultResponse.setConfidence(String.valueOf(jsonObject.getDouble("Confidence")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            zoneNameTextView.setText(resultResponse.getZonaName());
                            pokemon = loadPokemonDataJSON(resultResponse.getZonaName());
                            setMiniatureNameStatus(pokemon);
                            if(timer != null){
                                timer.cancel();
                            }
                            timer = setTimer(2.0f, client);

                        }
                    });
                    response.close();
                }
            });
        }
    }

}
