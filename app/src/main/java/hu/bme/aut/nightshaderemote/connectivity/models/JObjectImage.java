package hu.bme.aut.nightshaderemote.connectivity.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Ákos Pap
 */
public class JObjectImage {

    @JsonProperty(value = "objectId")
    private String objectId;

    @JsonProperty(value = "objectName")
    private String name;

    @JsonProperty("data")
    private String data;


    public Bitmap getImage() {
        if (TextUtils.isEmpty(data)) { return null; }

        byte[] bytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
