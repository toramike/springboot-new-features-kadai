package com.example.samuraitravel.controller;

import java.text.SimpleDateFormat;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller

public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;

	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository,
			ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;

	}

	@GetMapping("/houses/{id}/reviews")
	public String index(@PathVariable(name = "id") Integer id,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,

			Model model) {

		House house = houseRepository.getReferenceById(id);

		Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");

		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("house", house);
		model.addAttribute("dateFormat", dateFormat.toPattern());

		return "reviews/index";
	}

	@GetMapping("/houses/{id}/review/register")
	public String register(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		model.addAttribute("house", house);
		return "reviews/register";
	}

	@PostMapping("/houses/{id}/review/register/create")
	public String create(@PathVariable(name = "id") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			redirectAttributes.addFlashAttribute("errorMessage", "正しい入力をしてください");
			return "reviews/register";
		}

		reviewService.create(reviewRegisterForm, house, user);

		return "redirect:/houses/{id}";
	}

	@GetMapping("/houses/{id}/review/{reviewid}/edit")
	public String edit(@PathVariable(name = "id") Integer houseId, @PathVariable(name = "reviewid") Integer reviewId,
			Model model) {
		Review review = reviewRepository.getReferenceById(reviewId);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getHouse(), review.getUser(),
				review.getStars(), review.getComment());
		House house = houseRepository.getReferenceById(houseId);

		model.addAttribute("house", house);

		model.addAttribute("reviewEditForm", reviewEditForm);

		return "reviews/edit";
	}

	@PostMapping("/houses/{id}/review/{reviewid}/update")
	public String update(
			@ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		House house = reviewEditForm.getHouse();
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);

			redirectAttributes.addFlashAttribute("errorMessage", "正しい入力をしてください");
			return "reviews/edit";
		}
		reviewService.update(reviewEditForm);
		return "redirect:/houses/{id}";
	}

	@PostMapping("/houses/{id}/review/{reviewid}/delete")
	public String delete(@PathVariable(name = "reviewid") Integer reviewId) {
		reviewRepository.deleteById(reviewId);

		return "redirect:/houses/{id}";
	}

}