package com.gschat.app.robobinding;

import org.robobinding.viewattribute.property.PropertyViewAttribute;

import mehdi.sakout.fancybuttons.FancyButton;

public class FancyButtonTextAttribute  implements PropertyViewAttribute<FancyButton,String> {
    @Override
    public void updateView(FancyButton fancyButton, String s) {
        fancyButton.setText(s);
    }
}
