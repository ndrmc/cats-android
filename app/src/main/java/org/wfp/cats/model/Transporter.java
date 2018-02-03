package org.wfp.cats.model;

import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Transporter {

    @Id
    long id;

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }
}
