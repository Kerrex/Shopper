package pl.tomasz.morawski.shopper.helpers.JsonMappers;

import com.google.gson.annotations.Expose;
/**
 * Created by tomek on 23.04.17.
 */

public class ResultMapper {
    @Expose
    private String name;
    @Expose
    private String vicinity;

    public ResultMapper(String name, String vicinity) {
        this.name = name;
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
