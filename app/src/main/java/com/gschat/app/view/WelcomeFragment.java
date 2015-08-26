package com.gschat.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gschat.R;
import com.gschat.app.App;
import com.gschat.sdk.GSChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeFragment extends Fragment {

    private final static Logger logger = LoggerFactory.getLogger("WelcomeFragment");

    private GSChat gsChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {

            ((App) getActivity().getApplication()).getGsChat().connectService();

        } catch (Exception e) {

            Toast.makeText(
                    getActivity(),
                    String.format(getString(R.string.start_error), e.getMessage()),
                    Toast.LENGTH_SHORT).show();
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }
}
