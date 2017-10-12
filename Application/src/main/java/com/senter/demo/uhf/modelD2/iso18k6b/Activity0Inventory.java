package com.senter.demo.uhf.modelD2.iso18k6b;

import android.os.Bundle;
import android.util.Log;

import com.senter.demo.uhf.App;
import com.senter.demo.uhf.util.DataTransfer;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdErrorCode;
import com.senter.support.openapi.StUhf.InterrogatorModelDs.UmdOnIso18k6bInventory;

public class Activity0Inventory extends com.senter.demo.uhf.common.Activity0InventoryCommonAbstract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViews().setModes(InventoryMode.Custom);
    }

    @Override
    protected void uiOnInverntryButton() {
        startInventory();
    }


    private void startInventory() {
//		Log.e("", "Activity0Inventory: startInventory");
//		getViews().enableInventoryButton(false);
//		viewsSetAsStarted();

        App.uhfInterfaceAsModelD2().iso18k6bInventory(new UmdOnIso18k6bInventory() {

            @Override
            public void onTagInventory(Iso18k6bInventoryResult rslt, int antId) {
                Log.e("", "onTagInventory:" + DataTransfer.xGetString(rslt.getUid().getBytes()));
                addNewUiiMassageToListview("Uid:" + DataTransfer.xGetString(rslt.getUid().getBytes()));
            }

            @Override
            public void onFinishedWithError(UmdErrorCode error) {
//				Log.e("", "onFinishedWithError:"+error);
//				getViews().enableInventoryButton(true);
//				viewsSetAsStoped();
            }

            @Override
            public void onFinishedSuccessfully(int antId, int tagFound) {
//				Log.e("", "onFinishedSuccessfully:"+tagFound);
//				getViews().enableInventoryButton(true);
//				viewsSetAsStoped();
            }
        });
    }


}
