

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

package com.hometsolutions.space.Wizerd.Model;

import android.content.Context;

import com.hometsolutions.space.Wizerd.UI.DisplayNamePage;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.MultipleFixedChoicePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

public class ConnectionWizardModel extends AbstractWizardModel {
    public ConnectionWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(

                new BranchPage(this, "Connection Type")

                        .addBranch("Setup New Device",
                                //new DisplayNamePage(this, "Display Name").setRequired(true),

                                new BranchPage(this, "Device Type")

                                        .addBranch("Six ports",
                                                new DisplayNamePage(this, "Display Name").setRequired(true),
                                                new SingleFixedChoicePage(this, "Toast time")
                                                        .setChoices("30 seconds", "1 minute",
                                                                "2 minutes").setRequired(true)
                                        )
                                        .addBranch("Eight ports",
                                                new DisplayNamePage(this, "Display Name").setRequired(true),
                                                new SingleFixedChoicePage(this, "Toast time")
                                                        .setChoices("30 seconds", "1 minute",
                                                                "2 minutes").setRequired(true)
                                        )
                                        .addBranch("Ten ports",
                                        new DisplayNamePage(this, "Display Name").setRequired(true),
                                        new SingleFixedChoicePage(this, "Toast time")
                                                .setChoices("30 seconds", "1 minute",
                                                        "2 minutes").setRequired(true)
                                        ).setRequired(true)
                        )

                        .addBranch("Add Existing Device",

                                new DisplayNamePage(this, "Display Name").setRequired(true),

                                new SingleFixedChoicePage(this, "Salad type").setChoices(
                                        "Greek", "Caesar").setRequired(true),

                                new SingleFixedChoicePage(this, "Dressing").setChoices(
                                        "No dressing", "Balsamic", "Oil & vinegar",
                                        "Thousand Island", "Italian").setValue("No dressing"),
                                new NumberPage(this, "How Many Salads?").setRequired(true)
                        )

                        .addBranch("Restore Device",

                                new DisplayNamePage(this, "Display Name").setRequired(true),

                                new SingleFixedChoicePage(this, "Salad type").setChoices(
                                        "Greek", "Caesar").setRequired(true),

                                new SingleFixedChoicePage(this, "Dressing").setChoices(
                                        "No dressing", "Balsamic", "Oil & vinegar",
                                        "Thousand Island", "Italian").setValue("No dressing"),
                                new NumberPage(this, "How Many Salads?").setRequired(true)
                        ).setRequired(true)
        );
    }
}