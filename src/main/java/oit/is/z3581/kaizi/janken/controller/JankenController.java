package oit.is.z3581.kaizi.janken.controller;

import oit.is.z3581.kaizi.janken.model.Entry;
import oit.is.z3581.kaizi.janken.model.Janken;

import org.springframework.beans.factory.annotation.Autowired;
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
      @RequestParam(name = "username", required = false) String username,
      Model model) {

    if (username != null && !username.isBlank()) {
      model.addAttribute("username", username);
    }

    entry.addUser(username);
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
      @RequestParam("username") String username,
      Model model) {

    model.addAttribute("username", username);

    entry.addUser(username);
    model.addAttribute("users", entry.getUsers());

    return "janken";
  }

  @GetMapping("/logout/confirm")
  public String getLogoutConfirm() {
    return "logout-confirm";
  }
}
