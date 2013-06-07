package com.bridgecanada.prismatic;

import android.content.Context;
import android.widget.Button;
import com.bridgecanada.net.CookieSerializer;
import com.bridgecanada.net.IHttpResultCallback;
import com.bridgecanada.prismatic.di.PrismaticTestModule;
import com.bridgecanada.prismatic.feed.*;
import com.bridgecanada.testhelper.InjectedTestRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import roboguice.RoboGuice;
import roboguice.activity.RoboActivity;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 05/05/13
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(RobolectricTestRunner.class)
//@RunWith(InjectedTestRunner.class)
public class LoginActivityTest {

    IAuthService _authService;
    //@Inject LoginActivity loginActivity;

    @Before
    public void SetUp() {
        _authService = mock(IAuthService.class);
        //MockitoAnnotations.initMocks(this); // mock everything marked with @Mock
        PrismaticTestModule module = new PrismaticTestModule();
        module.addBinding(IAuthService.class, _authService);
        PrismaticTestModule.setUp(this, module);

    }

    @Test
    @SuppressWarnings("unchecked") // In test
    public void click_Sends_Login_Request() {

        // Arrange
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.onCreate(null);
        Button btnOk = (Button) loginActivity.findViewById(R.id.btnOk);

        // Act
        btnOk.performClick();

        // Assert

        verify(_authService).Login(anyString(), anyString(), any(IHttpResultCallback.class), any(IHttpResultCallback.class));
    }

    @Test
    public void success_Sends_Response_Intent() {

        // Arrange
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.onCreate(null);

        // Act

        // Assert
        fail("Need to test this");
    }

    @Test
    public void failure_Shows_Message() {

        // Arrange
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.onCreate(null);

        // Act

        // Assert
        fail("Need to test this");
    }

    /*
    @Test
    public void successful_Login_Should_Set_Cookie() {

        // Arrange

        LoginActivity loginActivity = new LoginActivity(_authService, "http://localhost:8080");
        loginActivity.onCreate(null);
        Button btnOk = (Button) loginActivity.findViewById(R.id.btnOk);

        // Act
        btnOk.performClick();

        // Assert
        loginActivity.getPersistentStore().getCookies()
    }
      */
}
