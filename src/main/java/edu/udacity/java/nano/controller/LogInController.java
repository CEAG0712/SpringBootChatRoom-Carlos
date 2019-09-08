package edu.udacity.java.nano.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.Session;
import java.security.Principal;

@Controller
public class LogInController {

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String username){


        ModelAndView mav = new ModelAndView();

        mav.addObject("username", username);
        mav.setView( new RedirectView("/index"));
        return mav;
    }
}
