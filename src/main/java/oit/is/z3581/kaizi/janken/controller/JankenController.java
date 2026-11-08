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

import oit.is.z3581.kaizi.janken.model.MatchMapper;
import oit.is.z3581.kaizi.janken.model.Match;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

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

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

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

  @GetMapping("/match")
  public String match(@RequestParam Long id, Model model, Authentication auth) {

    // ログインしているユーザの名前
    String loginUser = auth.getName();

    // クリックされたユーザを取得
    User opponent = userMapper.selectById(id);

    // HTMLに渡すデータ
    model.addAttribute("loginUser", loginUser);
    model.addAttribute("opponent", opponent);

    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam Long id,
      @RequestParam String hand,
      Model model,
      Authentication auth) {

    // ログインユーザを取得
    User user = userMapper.selectByName(auth.getName());

    // 対戦相手を取得
    User opponent = userMapper.selectById(id);

    // CPUの手を固定
    String cpuHand = "グー";

    // 勝敗判定
    String result;

    if (hand.equals(cpuHand)) {
      result = "引き分け";
    } else if ((hand.equals("グー") && cpuHand.equals("チョキ")) ||
        (hand.equals("チョキ") && cpuHand.equals("パー")) ||
        (hand.equals("パー") && cpuHand.equals("グー"))) {
      result = "勝ち";
    } else {
      result = "負け";
    }

    // 試合情報を作成
    Match match = new Match();
    match.setUser1(user.getId());
    match.setUser2(opponent.getId());
    match.setUser1Hand(hand);
    match.setUser2Hand(cpuHand);

    // DBに登録
    matchMapper.insertMatch(match);

    // HTMLに表示する情報を渡す
    model.addAttribute("loginUser", user.getName());
    model.addAttribute("opponent", opponent);
    model.addAttribute("userHand", hand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);

    return "match.html";
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
