package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

import jakarta.transaction.Transactional;

@Controller
public class FavoriteController {
	private final FavoriteService favoriteService;
	private final HouseRepository houseRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteController(FavoriteService favoriteService, FavoriteRepository favoriteRepository,
			HouseRepository houseRepository) {
		this.favoriteService = favoriteService;
		this.houseRepository = houseRepository;
		this.favoriteRepository = favoriteRepository;
	}

	//お気に入り一覧
	@GetMapping("/favorites")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {

		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUser(user, pageable);

		model.addAttribute("favoritePage", favoritePage);

		return "favorites/index";
	}

	//お気に入り登録
	@PostMapping("/houses/{id}/addFavorite")
	public String addFavorite(@PathVariable(name = "id") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated FavoriteForm favoriteForm, BindingResult bindingResult,
			Model model) {

		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		favoriteService.addFavorite(favoriteForm, house, user);

		return "redirect:/houses/{id}";
	}

	//お気に入り解除
	@PostMapping("/houses/{id}/delete")
	@Transactional
	public String delete(@PathVariable(name = "id") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		favoriteRepository.deleteByHouseAndUser(house, user);

		return "redirect:/houses/{id}";
	}

}
