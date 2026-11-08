package oit.is.z3581.kaizi.janken.model;

public class Match {
  private Long id;
  private Long user1;
  private Long user2;
  private String user1Hand;
  private String user2Hand;

  public Match() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUser1() {
    return user1;
  }

  public void setUser1(Long user1) {
    this.user1 = user1;
  }

  public Long getUser2() {
    return user2;
  }

  public void setUser2(Long user2) {
    this.user2 = user2;
  }

  public String getUser1Hand() {
    return user1Hand;
  }

  public void setUser1Hand(String user1Hand) {
    this.user1Hand = user1Hand;
  }

  public String getUser2Hand() {
    return user2Hand;
  }

  public void setUser2Hand(String user2Hand) {
    this.user2Hand = user2Hand;
  }
}
