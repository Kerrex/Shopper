package pl.tomasz.morawski.shopper.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Spinner;

import pl.tomasz.morawski.shopper.DoShopping;
import pl.tomasz.morawski.shopper.FindingLocation;
import pl.tomasz.morawski.shopper.R;

/**
 * Created by tomek on 04.06.17.
 */

public class DialogUtil {
    public static void spawnChooseShopDialog(final Activity ctx) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        final View view = ctx.getLayoutInflater().inflate(R.layout.choose_shop_dialog, null);
        builder.setTitle(ctx.getResources().getString(R.string.choose_shop_dialog_title));
        builder.setView(view);
        builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Spinner spinner = (Spinner) view.findViewById(R.id.shop_spinner);
                Intent intent = new Intent(ctx.getBaseContext(), DoShopping.class);
                String selectedItem = String.valueOf(spinner.getSelectedItem());
                intent.putExtra("ShopInformation",
                        new ShopInformation(selectedItem, "", Constants.valueOf(selectedItem).getIndex()));
                ctx.startActivity(intent);
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ctx.findShop();
            }
        });
        builder.show();
    }
}
