package oit.is.z3581.kaizi.janken.model;

public class Janken {

  private String playerHand;
  private String cpuHand;
  private String result;

  public Janken(String playerHand) {
    this.playerHand = playerHand;
    this.cpuHand = "グー";
    judge();
  }

  private void judge() {
    if (playerHand.equals(cpuHand)) {
      result = "あいこ";
    } else if ((playerHand.equals("グー") && cpuHand.equals("チョキ")) ||
        (playerHand.equals("チョキ") && cpuHand.equals("パー")) ||
        (playerHand.equals("パー") && cpuHand.equals("グー"))) {
      result = "勝ち";
    } else {
      result = "負け";
    }
  }

  public String getPlayerHand() {
    return playerHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public String getResult() {
    return result;
  }
}
