

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

package com.hometsolutions.space.Wizard.Model;

import com.hometsolutions.space.Activitys.NewConnectionActivity;
import com.hometsolutions.space.Wizard.UI.*;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.PageList;

public class ConnectionWizardModel extends AbstractWizardModel {

    NewConnectionActivity newConnectionActivity;

    public ConnectionWizardModel(NewConnectionActivity newConnectionActivity) {
        super(newConnectionActivity.getApplicationContext());
        this.newConnectionActivity = newConnectionActivity;
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(

                new BranchPage(this, "Connection Type")

                        .addBranch("Setup New Device",
                                new DisplayNamePage(this, "Display Name").setRequired(true),
                                new PairDevicePage(this, "Pair and Connect").setRequired(true),
                                new AuthenticationPage(this, "Authenticate").setRequired(true),
                                new SetupPortPage(this, "Setup Ports").setRequired(true).setRequired(true)
                        )

                        .addBranch("Add Existing Device",
                                new DisplayNamePage(this, "Display Name").setRequired(true),
                                new PairDevicePage(this, "Pair and Connect").setRequired(true)
                                        .setRequired(true)
                        )

                        .addBranch("Restore Device",
                                new DisplayNamePage(this, "Display Name").setRequired(true)
                        ).setRequired(true)
        );
    }
}
