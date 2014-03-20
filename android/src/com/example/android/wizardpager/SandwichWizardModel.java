/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.wizardpager;

import com.example.android.wizardpager.wizard.model.AbstractWizardModel;
import com.example.android.wizardpager.wizard.model.BranchPage;
import com.example.android.wizardpager.wizard.model.CustomerInfoPage;
import com.example.android.wizardpager.wizard.model.MultipleFixedChoicePage;
import com.example.android.wizardpager.wizard.model.PageList;
import com.example.android.wizardpager.wizard.model.SingleFixedChoicePage;

import android.content.Context;

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        
    	
    	return new PageList(new BranchPage(this, "Create Or Join Room").
        		addBranch("Join A Room",
        						new SingleFixedChoicePage(this, "Selected Room")
                                        .setChoices("School Trip #1", 
                                        		"School Trip #2",
                                        		"School Trip #3",
                                        		"School Trip #4",
                                        		"School Trip #5")
                                        .setRequired(true))

                        .addBranch("Create A Room",
                                new SingleFixedChoicePage(this, "Salad type")
                                        .setChoices("Greek", "Caesar")
                                        .setRequired(true),

                                new SingleFixedChoicePage(this, "Dressing")
                                        .setChoices("No dressing", "Balsamic", "Oil & vinegar",
                                                "Thousand Island", "Italian")
                                        .setValue("No dressing")
                        )

                        .setRequired(true),

                new CustomerInfoPage(this, "Your info")
                        .setRequired(true)
        );
    }
}
