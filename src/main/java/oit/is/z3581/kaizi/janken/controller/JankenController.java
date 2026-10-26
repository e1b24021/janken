package oit.is.z3581.kaizi.janken.controller;

import oit.is.z3581.kaizi.janken.model.Entry;
import oit.is.z3581.kaizi.janken.model.Janken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @GetMapping("/janken")
  public String getJanken(
      @RequestParam(name = "hand", required = false) String hand,
      Model model,
      Authentication authentication) {

    String username = authentication.getName();

    entry.addUser(username);

    model.addAttribute("username", username);
    model.addAttribute("users", entry.getUsers());

    if (hand != null) {
      if (!hand.equals("グー")
          && !hand.equals("チョキ")
          && !hand.equals("パー")) {
        return "janken";
      }

      Janken janken = new Janken(hand);
      model.addAttribute("janken", janken);
    }

    return "janken";
  }

  @PostMapping("/janken")
  public String postJanken(
      Model model,
      Authentication authentication) {

    String username = authentication.getName();

    entry.addUser(username);

    model.addAttribute("username", username);
    model.addAttribute("users", entry.getUsers());

    return "janken";
  }
}
