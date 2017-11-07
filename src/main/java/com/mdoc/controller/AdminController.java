package com.mdoc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mdoc.model.User;
import com.mdoc.service.UserService;

@Controller
public class AdminController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin/change-password", method = RequestMethod.GET)
    public ModelAndView changePasswordForm() {
	ModelAndView mav = new ModelAndView();
	
	mav.setViewName("/admin/changePassword");
	return mav;
    }

    @RequestMapping(value = "/admin/change-password", method = RequestMethod.POST)
    public ModelAndView changePasswordSubmit(@RequestParam("currentPassword") String current_password,
	    @RequestParam("newPassword") String new_password) {
	ModelAndView mav = new ModelAndView();
	BCryptPasswordEncoder bCryptPassEncoder = new BCryptPasswordEncoder();
	String username = SecurityContextHolder.getContext().getAuthentication().getName();
	User user = userService.findUserByEmail(username);
	String encodedPassword = user.getPassword();
	if (!bCryptPassEncoder.matches(current_password, encodedPassword)) {
	    mav.addObject("successMessage", "Current Password entered is wrong!!!");
	    mav.setView(new RedirectView("../admin/change-password"));
	    return mav;
	}
	user.setPassword(new_password);
	userService.saveUser(user);
	mav.addObject("successMessage", "Password changed Successfully!!!");
	mav.setView(new RedirectView("../admin/home"));
	return mav;
    }
}