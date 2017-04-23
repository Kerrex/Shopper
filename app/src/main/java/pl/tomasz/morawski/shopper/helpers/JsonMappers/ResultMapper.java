package pl.tomasz.morawski.shopper.helpers.JsonMappers;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tomek on 23.04.17.
 */

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class ResultMapper {
    @Expose
    private String name;
    @Expose
    private String vicinity;
}
