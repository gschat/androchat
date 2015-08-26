package com.gschat.app.viewmodel;

import android.content.Context;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.gschat.R;
import com.gschat.sdk.GSChat;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSUserListener;
import com.gschat.sdk.GSUserState;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PresentationModel
public class LoginViewModel implements HasPresentationModelChangeSupport {

    /**
     * logger .
     */
    private final static Logger logger = LoggerFactory.getLogger("loginViewModel");

    /**
     * gschat service
     */
    private final GSChat gsChat;

    /**
     * android application context
     */
    private final Context context;

    /**
     * current userName information
     */
    private String userName;

    /**
     * userName state information
     */
    private GSUserState state = GSUserState.Logoff;

    /**
     * received last error code
     */
    private GSError error = GSError.SUCCESS;

    /**
     * support dynamic model changed notify
     */
    private PresentationModelChangeSupport modelChangeSupport = new PresentationModelChangeSupport(this);


    /**
     * create new login view model
     * @param gsChat  gschat service
     * @param context android app context
     */
    public LoginViewModel(GSChat gsChat,Context context) {

        this.gsChat = gsChat;
        this.context = context;

        gsChat.addUserListener(new GSUserListener() {
            @Override
            public void onStateChanged(String userName, GSUserState state, GSError errorCode) {
                LoginViewModel.this.userName = userName;
                LoginViewModel.this.state = state;
                LoginViewModel.this.error = errorCode;

                LoginViewModel.this.modelChangeSupport.refreshPresentationModel();

                if(GSError.SUCCESS != errorCode) {
                    Toast.makeText(
                            LoginViewModel.this.context,
                            String.format(LoginViewModel.this.context.getString(R.string.user_state_error_notify),error),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * get userName name
     * @return userName name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * username setter
     * @param userName userName name
     */
    public void setUserName(String userName) {
        logger.debug("set username {}",userName);
        this.userName = userName;
    }

    public boolean getTextEditEnable() {

        switch (this.state){

            case Login:
            case LoginProcessing:
            case LogoffProcessing:
                return false;
            case Logoff:
                return true;
            default:
                return true;
        }
    }

    public boolean getButtonEnable() {

        logger.debug("getButtonEnable state({})",state);

        switch (this.state){
            case LoginProcessing:
            case LogoffProcessing:
                return false;
            case Logoff:
            case Login:
                return true;
            default:
                return true;
        }
    }

    public int getLoginProcess() {

        logger.debug("getLoginProcess state({})",state);


        if(!GSError.SUCCESS.equals(error)) {

            logger.debug("getLoginProcess 1");

            return -1;
        }

        if (state == GSUserState.Login) {

            logger.debug("getLoginProcess 2");

            return 100;
        }

        if(state == GSUserState.LoginProcessing) {

            logger.debug("getLoginProcess 3");

            return 1;
        }

        if(state == GSUserState.Logoff) {

            logger.debug("getLoginProcess 4");

            return 0;
        }

        return 0;
    }

    public void onClick() {

        logger.debug("onClick ....");

        if(state == GSUserState.Login) {

            try {
                logger.debug("call gschat logoff {}", userName);

                gsChat.logoff();

                logger.debug("call gschat logoff {} -- success", userName);

            } catch (Exception e) {
                Toast.makeText(
                        context,
                        String.format(context.getString(R.string.logoff_error),e.getMessage()),
                        Toast.LENGTH_SHORT).show();
            }
        }

        if(state == GSUserState.Logoff) {


            if("".equals(userName)) {
                Toast.makeText(
                        context,
                        context.getString(R.string.user_name_null_warning),
                        Toast.LENGTH_SHORT).show();

                return;
            }

            try {

                logger.debug("call gschat login {}", userName);

                gsChat.login(userName);

                if(!GSError.SUCCESS.equals(error)) {
                    Toast.makeText(
                            context,
                            String.format(context.getString(R.string.login_error_message),error),
                            Toast.LENGTH_SHORT).show();
                }

                logger.debug("call gschat login {} -- success", userName);

            } catch (Exception e) {


                Toast.makeText(
                        context,
                        String.format(context.getString(R.string.login_error_message),e.getMessage()),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ActionProcessButton.Mode getProcessMode() {
        return ActionProcessButton.Mode.ENDLESS;
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return modelChangeSupport;
    }
}
