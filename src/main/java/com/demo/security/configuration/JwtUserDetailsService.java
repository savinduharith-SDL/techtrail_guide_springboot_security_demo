package com.demo.security.configuration;

import com.demo.security.exception.types.UserNotActiveException;
import com.demo.security.exception.types.UserNotVerifiedException;
import com.demo.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthService authService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, UserNotVerifiedException, UserNotActiveException {
		com.demo.security.entity.User user = authService.findUserByEmail(email);
		if (user != null && !user.getActive()) throw new UserNotActiveException();
		if (user != null && !user.getVerified()) throw new UserNotVerifiedException();
		if (user != null) {
			List<GrantedAuthority> listAuthorities = new ArrayList<>();
			listAuthorities.add(new Role(user.getRole()));
			return new User(email, user.getPassword(), listAuthorities);
		} else {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
	}

}