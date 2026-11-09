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

import oit.is.z3581.kaizi.janken.model.MatchInfo;
import oit.is.z3581.kaizi.janken.model.MatchInfoMapper;

import org.springframework.transaction.annotation.Transactional;
//import oit.is.z3581.kaizi.janken.model.MatchInfo;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import oit.is.z3581.kaizi.janken.service.AsyncKekka;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchInfoMapper;

  @Autowired
  AsyncKekka asyncKekka;

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

    // アクティブな試合情報を取得
    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfo();

    // HTMLに渡す
    model.addAttribute("activeMatches", activeMatches);

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



    // HTMLに表示する情報を渡す
    model.addAttribute("loginUser", user.getName());
    model.addAttribute("opponent", opponent);
    model.addAttribute("userHand", hand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);

    return "match.html";
  }

  @GetMapping("/wait")
  @Transactional
  public String wait(@RequestParam Long id,
      @RequestParam String hand,
      Model model,
      Authentication auth) {

    // ログインユーザを取得
    User user = userMapper.selectByName(auth.getName());

    // 対戦相手を取得
    User opponent = userMapper.selectById(id);

    // 対戦相手が自分を待っているか確認
    MatchInfo waitingMatch = matchInfoMapper.selectWaitingMatch(user.getId(), opponent.getId());

    if (waitingMatch == null) {
      // 待機中の試合がなければ、自分が先手として登録
      MatchInfo matchInfo = new MatchInfo();

      matchInfo.setUser1(user.getId());
      matchInfo.setUser2(opponent.getId());
      matchInfo.setUser1Hand(hand);
      matchInfo.setIsActive(true);

      matchInfoMapper.insertMatchInfo(matchInfo);

    } else {

      // 待機中の試合があれば、試合を成立させる

      // 先手と後手の情報をmatchesに登録
      matchMapper.insertMatch(
          waitingMatch.getUser1(),
          waitingMatch.getUser2(),
          waitingMatch.getUser1Hand(),
          hand);

      // 先手の待機状態を解除
      matchInfoMapper.updateIsActiveFalse(waitingMatch.getId());
    }

    // wait.htmlに表示するログインユーザ名
    model.addAttribute("loginUser", user.getName());

    return "wait.html";
  }

  @GetMapping("/kekka")
  public SseEmitter kekka(Authentication auth) {

    // ログインユーザーを取得
    User user = userMapper.selectByName(auth.getName());

    // SSEの接続を作成
    SseEmitter emitter = new SseEmitter(0L);

    // 非同期で試合結果を確認
    asyncKekka.checkMatch(emitter, user.getId());

    return emitter;
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
