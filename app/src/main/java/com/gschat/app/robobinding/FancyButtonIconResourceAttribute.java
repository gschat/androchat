package com.gschat.app.robobinding;

import org.robobinding.viewattribute.property.PropertyViewAttribute;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by liyang on 15/7/16.
 */
public final class FancyButtonIconResourceAttribute implements PropertyViewAttribute<FancyButton,String> {

    @Override
    public void updateView(FancyButton fancyButton, String s) {
        fancyButton.setIconResource(s);
    }
}
