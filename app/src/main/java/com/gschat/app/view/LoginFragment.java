package com.gschat.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gschat.R;
import com.gschat.app.App;
import com.gschat.app.viewmodel.LoginViewModel;
import com.gschat.sdk.GSChat;

import org.robobinding.ViewBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginFragment extends Fragment {

    private final static Logger logger = LoggerFactory.getLogger("LoginFragment");

    private GSChat gsChat;

    private LoginViewModel loginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        logger.debug("loginFragment onCreateView");

        App app = ((App) getActivity().getApplication());

        if(loginViewModel == null) {
            loginViewModel = new LoginViewModel(app.getGsChat(),getActivity());
        }

        ViewBinder viewBinder = app.createViewBinder(getActivity());

        return viewBinder.inflateAndBindWithoutAttachingToRoot(R.layout.fragment_login, loginViewModel, container);
    }

    @Override
    public void onResume() {
        super.onResume();

        logger.debug("loginFragment onResume");

        loginViewModel.getPresentationModelChangeSupport().refreshPresentationModel();
    }

    @Override
    public void onPause() {
        super.onPause();

        logger.debug("loginFragment onPause");
    }
}
