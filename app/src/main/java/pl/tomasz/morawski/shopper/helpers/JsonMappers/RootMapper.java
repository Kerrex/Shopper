package pl.tomasz.morawski.shopper.helpers.JsonMappers;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by tomek on 23.04.17.
 */

public class RootMapper {
    @Expose
    private List<String> html_attributions;
    @Expose
    private List<ResultMapper> results;
    @Expose
    private String status;

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public List<ResultMapper> getResults() {
        return results;
    }

    public void setResults(List<ResultMapper> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RootMapper(List<String> html_attributions, List<ResultMapper> results, String status) {
        this.html_attributions = html_attributions;
        this.results = results;
        this.status = status;
    }
}
