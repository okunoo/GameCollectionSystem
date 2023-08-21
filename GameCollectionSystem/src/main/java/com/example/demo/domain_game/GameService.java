package com.example.demo.domain_game;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
	private final GameRepository gameRepository;
	
	public Page<GameEntity> findAll(Pageable pageable){
		return gameRepository.findAll(pageable);
	}
	
	public void addGame(GameEntity game) {
		Long maxId = gameRepository.findMaxId();
		
		Long newId = (maxId == null) ? 1L : maxId + 1L;
	    game.setId(newId);
        gameRepository.save(game);
    }
	
	public GameEntity updateGame(GameEntity updatedGame) {
		GameEntity existingGame = gameRepository.findById(updatedGame.getId()).orElse(null);
		if(existingGame != null) {
			existingGame.setSummary(updatedGame.getSummary());
			existingGame.setGenre(updatedGame.getGenre());
			existingGame.setScore(updatedGame.getScore());
			existingGame.setComment(updatedGame.getComment());
			
			return gameRepository.save(existingGame);
		}
		return null;
	}
	
	public void deleteGame(Long id) {
		gameRepository.deleteById(id);
	}
	
	public GameEntity findById(Long id) {
		return gameRepository.findById(id).orElse(null);
	}
	
	public List<GameEntity> searchGames(String searchSummary,List<String> genres){
		return gameRepository.findBySummaryContainingIgnoreCaseAndGenreIn(searchSummary, genres);
	}
	
	public List<GameEntity> searchGamesBySummary(String searchSummary){
		return gameRepository.findBySummaryContainingIgnoreCase(searchSummary);
	}
	
	public List<GameEntity> searchGamesByGenre(List<String> genres){
		return gameRepository.findByGenreIn(genres);
	}
}
