package com.example.demo.web.game;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.domain_game.GameEntity;
import com.example.demo.domain_game.GameService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

	private final GameService gameService;

	//ゲームジャンル。検索やゲー厶追加の際に使用。
	private final List<String> genreOptions = Arrays.asList(
			"アクション", "対戦型格闘ゲーム", "RPG", "シューティング(FPS,TPS)", "アドベンチャー",
			"MMO", "レース", "パズル", "スポーツ", "ストラテジー", "恋愛シミュレーション",
			"タワーディフェンス", "シミュレーション（育成、箱庭、操作）", "サンドボックス",
			"音楽ゲーム", "テーブルゲーム", "パーティゲーム", "トレーディングカードゲーム");

	@GetMapping
	public String showList(Model model, @RequestParam(defaultValue = "0") int page) {
	    int pageSize = 10; // 1ページあたりのデータ数
	    Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
	    Page<GameEntity> gamePage = gameService.findAll(pageable);

	    List<GameEntity> gameList = gamePage.getContent();
	    int totalPages = gamePage.getTotalPages();
	    long totalElements = gamePage.getTotalElements(); // 追加: 要素の総数を取得

	    model.addAttribute("gameList", gameList);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("totalElements", totalElements); // 追加: 要素の総数をモデルに追加

	    return "games/list";
	}


	@GetMapping("creationForm")
	public String showCreationForm(Model model) {

		GameForm gameForm = new GameForm();

		model.addAttribute("gameForm", gameForm);
		model.addAttribute("genreOptions", genreOptions);
		return "games/creationForm";
	}

	@GetMapping("/edit")
	public String showEditForm(@RequestParam Long id, Model model) {
		GameEntity game = gameService.findById(id);

		if (game == null) {
			return "redirect:/error";
		}

		model.addAttribute("editForm", game);
		model.addAttribute("genreOptions", genreOptions);
		return "games/edit";
	}

	@PostMapping("")
	public String addGame(@Validated GameForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return showCreationForm(model);
		}

		GameEntity game = new GameEntity();
		game.setSummary(form.getSummary());
		game.setGenre(form.getGenre());
		game.setScore(form.getScore());
		game.setComment(form.getComment());

		gameService.addGame(game);
		return "redirect:/games";
	}

	@PostMapping("/update")
	public String updateGame(@ModelAttribute @Validated EditGameForm editGameForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "games/edit";
		}
		GameEntity updatedGame = new GameEntity(editGameForm.getId(), editGameForm.getSummary(),
				editGameForm.getGenre(),
				editGameForm.getScore(), editGameForm.getComment());

		gameService.updateGame(updatedGame);
		return "redirect:/games";
	}

	@PostMapping("/delete")
	public String deleteGame(@RequestParam("id") Long id) {
		gameService.deleteGame(id);
		return "redirect:/games";
	}

	@GetMapping("/search")
	public String searchGames(
			Model model,
			@RequestParam(defaultValue = "") String searchSummary,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "") String selectedGenre) {
		int pageSize = 10;
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

		List<GameEntity> gameList;
		Page<GameEntity> gamePage;

		if (!searchSummary.isEmpty() && !selectedGenre.isEmpty()) {
			//summaryとgenreの両方が指定されている場合
			//両方の条件を満たすゲームを検索
			List<String> selectedGenres = List.of(selectedGenre);
			gameList = gameService.searchGames(searchSummary, selectedGenres);
			gamePage = new PageImpl<>(gameList);
		} else if (!searchSummary.isEmpty() && selectedGenre.isEmpty()) {
			//summaryだけが指定されている場合
			//summaryの部分一致検索を実行
			gameList = gameService.searchGamesBySummary(searchSummary);
			gamePage = new PageImpl<>(gameList);
		} else if (searchSummary.isEmpty() && !selectedGenre.isEmpty()) {
			//genreだけが指定されている場合
			//ganreで検索
			List<String> selectedGenres = List.of(selectedGenre);
			gameList = gameService.searchGamesByGenre(selectedGenres);
			gamePage = new PageImpl<>(gameList);
		} else {
			//どの条件も指定されていない場合
			//ページングを適用して全てのゲームデータを取得
			gamePage = gameService.findAll(pageable);
			gameList = gamePage.getContent();
		}

		int totalPages = gamePage.getTotalPages();

		model.addAttribute("gameList", gameList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("searchSummary", searchSummary);
		model.addAttribute("selectedGenre", selectedGenre);
		model.addAttribute("searchForm", new SearchForm());
		model.addAttribute("genreOptions", genreOptions);

		return "games/search";
	}

}
