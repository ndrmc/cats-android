package org.wfp.cats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.wfp.cats.client.TransporterClient;
import org.wfp.cats.model.Transporter;
import org.wfp.cats.utils.ServiceGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetupActivity extends AppCompatActivity {

    @BindView(R.id.setting_up_text)
    TextView statusText;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);

        BoxStore boxStore = ((CatsApplication) getApplicationContext()).getBoxStore();
        Box<Transporter> transporterBox = boxStore.boxFor(Transporter.class);

        if(transporterBox.count() == 0) {
            TransporterClient client = ServiceGenerator.createService(TransporterClient.class);
            statusText.setText(getString(R.string.fetching_transporters));
            client.all().enqueue(new Callback<List<Transporter>>() {
                @Override
                public void onResponse(Call<List<Transporter>> call, Response<List<Transporter>> response) {
                    if(response.isSuccessful()) {
                        transporterBox.put(response.body());
                        statusText.setText(getString(R.string.fetched_transporters));
                    } else {
                        Toast.makeText(SetupActivity.this, getString(R.string.fetch_transporters_error), Toast.LENGTH_SHORT).show();
                    }

                    startMainActivity();
                }

                @Override
                public void onFailure(Call<List<Transporter>> call, Throwable t) {
                    Toast.makeText(SetupActivity.this, getString(R.string.fetch_transporters_error), Toast.LENGTH_SHORT).show();
                    startMainActivity();
                }
            });
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(SetupActivity.this, MainActivity.class));
    }
}
