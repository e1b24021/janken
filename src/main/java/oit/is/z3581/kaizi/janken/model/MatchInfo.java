package oit.is.z3581.kaizi.janken.model;

public class MatchInfo {
  private Long id;
  private Long user1;
  private Long user2;
  private String user1Hand;
  private Boolean isActive;

  // アクティブな試合を表示するためのユーザ名
  private String user1Name;
  private String user2Name;

  public MatchInfo() {
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

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getUser1Name() {
    return user1Name;
  }

  public void setUser1Name(String user1Name) {
    this.user1Name = user1Name;
  }

  public String getUser2Name() {
    return user2Name;
  }

  public void setUser2Name(String user2Name) {
    this.user2Name = user2Name;
  }
}
