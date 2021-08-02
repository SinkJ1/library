package sinkj1.library.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sinkj1.library.domain.Authority;
import sinkj1.library.repository.AuthorityRepository;
import sinkj1.library.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthorityResource {


    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthorityResource(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/authority")
    public ResponseEntity<List<Authority>> getAllAuthority(){
        return ResponseEntity.ok(authorityRepository.findAll());
    }

    @GetMapping("/authority/{login}")
    public ResponseEntity<List<Authority>> getAllAuthorityByLogin(@PathVariable("login") String login){
        List<Authority> authorityList = new ArrayList<>();
        Optional<List<Authority>> optionalList = userRepository.getUserAuthoritiesByUserLogin(login);
        if(optionalList.isPresent()){
            authorityList = optionalList.get();
        }
        return ResponseEntity.ok(authorityList);
    }


}
