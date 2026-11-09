package oit.is.z3581.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

//import oit.is.z3581.kaizi.janken.model.Match;

@Mapper
public interface MatchMapper {

  @Select("SELECT * FROM matches")
  ArrayList<Match> selectAllMatches();

  @Insert("""
      INSERT INTO matches
      (user1, user2, user1Hand, user2Hand, isActive)
      VALUES
      (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, TRUE)
      """)
  void insertMatch(@Param("user1") Long user1,
      @Param("user2") Long user2,
      @Param("user1Hand") String user1Hand,
      @Param("user2Hand") String user2Hand);

      // ログインユーザーのアクティブな試合を取得
  @Select("""
      SELECT *
      FROM matches
      WHERE user1 = #{userId}
        AND isActive = TRUE
        ORDER BY id DESC
    LIMIT 1
      """)
  Match selectActiveMatch(@Param("userId") Long userId);

  // 試合結果を表示済みにする
  @Update("""
      UPDATE matches
      SET isActive = FALSE
      WHERE id = #{id}
      """)
  void updateIsActiveFalse(@Param("id") Long id);
}
