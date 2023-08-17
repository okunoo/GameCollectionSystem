package com.example.demo.domain_game;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Mapper
public interface GameRepository extends JpaRepository<GameEntity, Long> {
	 List<GameEntity> findAll();
	 
	 void deleteById(Long id);
	 
	 @Query("SELECT max(g.id) FROM GameEntity g")
	 Long findMaxId();
	 
	List<GameEntity> findBySummaryContainingIgnoreCaseAndGenreIn(String summary, List<String> genres);
	 
	// Summaryの部分一致検索用メソッドを追加
	List<GameEntity> findBySummaryContainingIgnoreCase(String summary);
	    
	// Genreのリストによる検索用メソッドを追加
	List<GameEntity> findByGenreIn(List<String> genres);
}
