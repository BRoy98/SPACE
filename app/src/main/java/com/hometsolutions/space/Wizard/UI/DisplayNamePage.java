package com.hometsolutions.space.Wizard.UI;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;

public class DisplayNamePage extends Page {

    public static final String DPNAME_DATA_KEY = "dp_name";

    public DisplayNamePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return DisplayNameFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> arrayList) {
        arrayList.add(new ReviewItem("Display name", mData.getString(DPNAME_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(DPNAME_DATA_KEY));
    }
}
