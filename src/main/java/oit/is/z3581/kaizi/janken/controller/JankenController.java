package oit.is.z3581.kaizi.janken.controller;

import oit.is.z3581.kaizi.janken.model.Entry;
import oit.is.z3581.kaizi.janken.model.Janken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import oit.is.z3581.kaizi.janken.model.UserMapper;
import oit.is.z3581.kaizi.janken.model.User;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @GetMapping("/janken")
  public String getJanken(
      @RequestParam(name = "hand", required = false) String hand,
      Model model,
      Authentication authentication) {

    String username = authentication.getName();

    entry.addUser(username);

    // DBから全ユーザ情報を取得
    ArrayList<User> users = userMapper.selectAllUsers();

    model.addAttribute("username", username);
    model.addAttribute("users", entry.getUsers());
    // HTMLにユーザ一覧を渡す
    model.addAttribute("users", users);

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
