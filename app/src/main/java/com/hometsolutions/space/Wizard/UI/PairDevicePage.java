
/*
 *
 *                       Copyright (C) 2016 Bishwajyoti Roy
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hometsolutions.space.Wizard.UI;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.hometsolutions.space.Activitys.NewConnectionActivity;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.ImagePage;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;

import java.util.ArrayList;

public class PairDevicePage extends Page {

    boolean isConnected = false;
    public static final String CONNECT_DATA_KEY = "connect_state";
    PairDeviceFragment pairDeviceFragment;

    public PairDevicePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
            pairDeviceFragment = new PairDeviceFragment();
    }

    @Override
    public Fragment createFragment() {
            return pairDeviceFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> arrayList) {
        arrayList.add(new ReviewItem("Pairing", "Successful", getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return isConnected;

    }
}
