package pl.tomasz.morawski.shopper.helpers.JsonMappers;

import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tomek on 23.04.17.
 */

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class RootMapper {
    @Expose
    private List<String> html_attributions;
    @Expose
    private List<ResultMapper> results;
    @Expose
    private String status;
}
