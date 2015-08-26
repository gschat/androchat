package com.gschat.app.robobinding;

import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;

import org.robobinding.attribute.Command;
import org.robobinding.viewattribute.event.EventViewAttribute;

import mehdi.sakout.fancybuttons.FancyButton;


public class FancyButtonOnClickEvent implements EventViewAttribute<FancyButton> {


    @Override
    public void bind(final FancyButton fancyButton, final Command command) {

        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                command.invoke(fancyButton);
            }
        });
    }

    @Override
    public Class<?> getEventType() {
        return null;
    }
}
