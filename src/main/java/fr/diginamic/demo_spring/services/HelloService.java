package fr.diginamic.demo_spring.services;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String salutations() {
        return "Je suis la classe de service et je vous dis bonjour";
    }
}
