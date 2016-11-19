package com.hometsolutions.space.Wizerd.UI;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;

/**
 * Created by Bishwajyoti Roy on 11/18/2016.
 */

public class PairDevicePage extends Page {

    protected PairDevicePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return null;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> arrayList) {

    }
}
