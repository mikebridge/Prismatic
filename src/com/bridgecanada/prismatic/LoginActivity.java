package com.bridgecanada.prismatic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.feed.IAuthService;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 22/04/13
 */
//public class LoginActivity extends Activity {
public class LoginActivity extends RoboActivity {

    private final String TAG = getClass().getSimpleName();

    @Inject private IAuthService _authService;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        Button btnOk = (Button)findViewById(R.id.btnOk);

        btnOk.setOnClickListener(getCLickListener());   // Set OnClick Listener on SignUp button


    }

//    public PersistentCookieStore getPersistentStore() {
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        return new PersistentCookieStore(sharedPreferences);
//    }

    private View.OnClickListener getCLickListener() {
        return new View.OnClickListener() {

            public void onClick(View v) {
                //Log.i(TAG, "Clicked...") ;

                EditText userNameText = (EditText)findViewById(R.id.username);
                EditText passwordText = (EditText)findViewById(R.id.password);

                String login = userNameText.getText().toString();
                String password = passwordText.getText().toString();

                _authService.Login(
                        login,
                        password,
                        createLoginSuccessCallback(),
                        createLoginFailureCallback()
                );
            }

        };
    }

    private IHttpResultCallback<String> createLoginSuccessCallback() {
        return new IHttpResultCallback<String>() {

            @Override
            public void onComplete(String loginResult, String rawResponse, int statusCode) {

                // The successful outcome is that the cookies are stored
                // in the persistent cookie store.

                Log.w(TAG, "onSuccess! " + loginResult);

                processSuccessfulLogin(loginResult);

                //ShowToast("Successfully Logged In!");

            }
        };
    }

    private void processSuccessfulLogin(String loginResult) {

        //if (loginResult == null) {
        //    throw new RuntimeException("This shouldn't be null!");
        //}
        Intent intent=new Intent();
        intent.putExtra("url", loginResult); // not sure this is useful.
        setResult(RESULT_OK, intent);
        finish(); // close the window

    }

    private IHttpResultCallback<LoginFailureResult> createLoginFailureCallback() {
        return new IHttpResultCallback<LoginFailureResult>() {

            @Override
            public void onComplete(LoginFailureResult errorResult,String rawResponse, int statusCode) {
                showToast(concatErrors(errorResult.getMessages()));
            }

        };
    }


    private void showToast(String message) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, message, duration);
        toast.show();

    }

    private String concatErrors(List<String> messages) {
        StringBuilder sb = new StringBuilder();
        for(String s: messages) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(s);
        }
        return sb.toString();
    }

}