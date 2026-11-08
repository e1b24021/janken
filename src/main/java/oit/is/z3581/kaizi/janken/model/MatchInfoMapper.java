package oit.is.z3581.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

//import oit.is.z3581.kaizi.janken.model.MatchInfo;

@Mapper
public interface MatchInfoMapper {

  // アクティブな試合情報を登録
  @Insert("""
      INSERT INTO matchinfo
      (user1, user2, user1Hand, isActive)
      VALUES
      (#{user1}, #{user2}, #{user1Hand}, #{isActive})
      """)
  void insertMatchInfo(MatchInfo matchInfo);

  // アクティブな試合をすべて取得
  @Select("""
      SELECT
        m.id,
        m.user1,
        m.user2,
        m.user1Hand,
        m.isActive,
        u1.name AS user1Name,
        u2.name AS user2Name
      FROM matchinfo m
      JOIN users u1 ON m.user1 = u1.id
      JOIN users u2 ON m.user2 = u2.id
      WHERE m.isActive = TRUE
      """)
  ArrayList<MatchInfo> selectActiveMatchInfo();

  // 対戦相手が自分を待っているレコードを取得
  @Select("""
      SELECT *
      FROM matchinfo
      WHERE user1 = #{opponentId}
        AND user2 = #{userId}
        AND isActive = TRUE
      """)
  MatchInfo selectWaitingMatch(
      @Param("userId") Long userId,
      @Param("opponentId") Long opponentId);

  // 待機状態を解除
  @Update("""
      UPDATE matchinfo
      SET isActive = FALSE
      WHERE id = #{id}
      """)
  void updateIsActiveFalse(@Param("id") Long id);
}
