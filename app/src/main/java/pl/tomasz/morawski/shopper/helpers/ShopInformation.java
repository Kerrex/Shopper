package pl.tomasz.morawski.shopper.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tomek on 23.04.17.
 */

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class ShopInformation implements Parcelable {
    private String name;
    private String address;
    private Integer id;

    protected ShopInformation(Parcel in) {
        name = in.readString();
        address = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShopInformation> CREATOR = new Parcelable.Creator<ShopInformation>() {
        @Override
        public ShopInformation createFromParcel(Parcel in) {
            return new ShopInformation(in);
        }

        @Override
        public ShopInformation[] newArray(int size) {
            return new ShopInformation[size];
        }
    };
}
