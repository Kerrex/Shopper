package pl.tomasz.morawski.shopper.helpers;

/**
 * Created by tomek on 22.04.17.
 */

public enum Constants {
    Biedronka(0),
    Lidl(1),
    Carrefour(2),
    Tesco(3);

    public static final String GOOGLE_API_KEY = "AIzaSyB-abondJExHeMWV13veBqC-T9lMaohlx0";
    private int index;

    public int getIndex() {
        return index;
    }

    Constants(int index) {
        this.index = index;
    }
}
