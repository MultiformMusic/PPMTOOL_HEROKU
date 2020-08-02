package net.multiformmusic.ppmtool.services;

import net.multiformmusic.ppmtool.domain.User;
import net.multiformmusic.ppmtool.exceptions.UsernameAlreadyExistException;
import net.multiformmusic.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser) throws UsernameAlreadyExistException {


            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            User findUser = userRepository.findByUsername(newUser.getUsername());
            if (findUser != null) {
                throw new UsernameAlreadyExistException("Username '" + newUser.getUsername() + "' already exists.");
            }

            newUser.setConfirmPassword("");
            return userRepository.save(newUser);


    }
}
