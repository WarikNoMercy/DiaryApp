package kirill.app.diary.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kirill.app.diary.models.UserDiary;
import kirill.app.diary.repositories.UserRepository;


@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
	public void registerUser(UserDiary user) {
		
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
	
	public Optional<UserDiary> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDiary user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
	}
}
