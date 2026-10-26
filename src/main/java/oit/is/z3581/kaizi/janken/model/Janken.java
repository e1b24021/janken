package oit.is.z3581.kaizi.janken.model;

public class Janken {

  private String playerHand;
  private String cpuHand;
  private String result;

  public Janken(String playerHand) {
    this.playerHand = playerHand;
    this.cpuHand = "Gu";
    judge();
  }

  private void judge() {
    if (playerHand.equals("Gu")) {
      result = "Draw!";
    } else if (playerHand.equals("Ch")) {
      result = "You Lose!";
    } else if (playerHand.equals("Pa")) {
      result = "You Win!";
    } else {
      result = "Invalid hand";
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
