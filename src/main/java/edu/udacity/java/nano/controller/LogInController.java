package edu.udacity.java.nano.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.net.UnknownHostException;
import java.security.Principal;

@Controller
public class LogInController {

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username){

        ModelAndView mav = new ModelAndView(new RedirectView("/index"));
        mav.addObject("username", username);

        return mav;
    }

    /**
     * Login Page
     */
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    /**
     * Chatroom Page
     */
    @GetMapping("/index")
    public ModelAndView index(String username, HttpServletRequest request) throws UnknownHostException {
        //TODO: add code for login to chatroom.

        ModelAndView mav = new ModelAndView("chat");
        mav.addObject("username", username);
        mav.addObject("webSocketUrl","ws://localhost:8080/chat/"+username );
        return mav;
    }
}
