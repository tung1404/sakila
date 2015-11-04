package vn.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.store.jpa.domain.User;
import vn.store.service.repositories.UserRepository;

/**
 * @author Siva
 *
 */
@Service
@Transactional
public class UserService
{
	
	@Autowired private UserRepository userRepository;

  
    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

	public void updateUser(User user) {
		userRepository.save(user);
		
	}

	
	
}
