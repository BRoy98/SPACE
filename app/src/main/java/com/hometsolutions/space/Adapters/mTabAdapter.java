/*****************************************************************************************
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
 *
 *
 ****************************************************************************************/

package com.hometsolutions.space.Adapters;

/**
 * Created by Bishwajyoti Roy on 23-10-2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hometsolutions.space.Activitys.EditComponentsActivity;
import com.hometsolutions.space.Fragments.*;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class mTabAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    EditComponentsActivity editComponentsActivity;

    //Constructor to the class
    public mTabAdapter(FragmentManager fm, int tabCount, EditComponentsActivity editComponentsActivity) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.editComponentsActivity = editComponentsActivity;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                 addLightFragment tab1 = new addLightFragment(editComponentsActivity);
                return tab1;
            case 1:
                addFanFragment tab2 = new addFanFragment(editComponentsActivity);
                return tab2;
            case 2:
                addWindowFragment tab3 = new addWindowFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Light";
            case 1:
                return "Fan";
            case 2:
                return "Window";
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}