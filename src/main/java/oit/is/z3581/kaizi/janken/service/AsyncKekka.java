package oit.is.z3581.kaizi.janken.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3581.kaizi.janken.model.Match;
import oit.is.z3581.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {

  private final MatchMapper matchMapper;

  public AsyncKekka(MatchMapper matchMapper) {
    this.matchMapper = matchMapper;
  }

  @Async
  public void checkMatch(SseEmitter emitter, Long userId) {

    AtomicBoolean closed = new AtomicBoolean(false);

    emitter.onCompletion(() -> closed.set(true));
    emitter.onTimeout(() -> closed.set(true));
    emitter.onError(e -> closed.set(true));

    try {
      while (!closed.get()) {

        // アクティブな試合を取得
        Match match = matchMapper.selectActiveMatch(userId);

        if (match != null) {

          // 勝敗を判定
          String result;

          String userHand = match.getUser1Hand();
          String opponentHand = match.getUser2Hand();

          if (userHand.equals(opponentHand)) {
            result = "引き分け";
          } else if ((userHand.equals("グー") && opponentHand.equals("チョキ")) ||
              (userHand.equals("チョキ") && opponentHand.equals("パー")) ||
              (userHand.equals("パー") && opponentHand.equals("グー"))) {
            result = "勝ち";
          } else {
            result = "負け";
          }

          // 画面に送信する文字列
          String message = "あなたの手：" + userHand
              + "、相手の手：" + opponentHand
              + "、結果：" + result;

          // SSEで結果を送信
          emitter.send(SseEmitter.event().data(message));

          // 結果を送信した試合を非アクティブにする
          matchMapper.updateIsActiveFalse(match.getId());

          emitter.complete();
          break;
        }

        // 1秒待ってから再確認
        TimeUnit.SECONDS.sleep(1);
      }

    } catch (IOException e) {
      emitter.completeWithError(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      emitter.complete();
    }
  }
}
