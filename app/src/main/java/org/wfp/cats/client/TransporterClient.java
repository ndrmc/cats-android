package org.wfp.cats.client;

import org.wfp.cats.model.Transporter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TransporterClient {
    @GET("/api/transporters")
    Call<List<Transporter>> all();
}
