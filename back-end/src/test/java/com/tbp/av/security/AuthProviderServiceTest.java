package com.tbp.av.security;
;
import com.tbp.av.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(value = MockitoJUnitRunner.class)
public class AuthProviderServiceTest {

    @InjectMocks
    AuthProviderService authProviderService;
    @Mock
    UserService userService;

    @Test
    public void testSupports() {
        assertFalse(authProviderService.supports(String.class));
        assertTrue(authProviderService.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAuthenticateInvalid() {
        when(userService.isLoginValid(anyString(), anyString())).thenReturn(false);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        when(authentication.getCredentials()).thenReturn("pass");
        try {
            authProviderService.authenticate(authentication);
            fail("This code should not be executed");
        } catch (UsernameNotFoundException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testAuthenticateSuccess() {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        when(authentication.getCredentials()).thenReturn("pass");
        when(userService.isLoginValid("username", "pass")).thenReturn(true);

        try {
            Authentication a = authProviderService.authenticate(authentication);
            assertEquals("username", a.getName());
            assertEquals("pass", a.getCredentials().toString());
            assertEquals("USER", a.getAuthorities().toArray()[0].toString());
        } catch (UsernameNotFoundException e) {
            fail("This code should not be executed");
        }
    }
}