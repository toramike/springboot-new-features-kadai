package com.example.samuraitravel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;

	public ReviewService(ReviewRepository reviewRepository) {

		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public void create(ReviewRegisterForm reviewRegisterForm, House house, User user) {
		Review review = new Review();

		review.setHouse(house);
		review.setUser(user);
		review.setStars(reviewRegisterForm.getStars());
		review.setComment(reviewRegisterForm.getComment());

		reviewRepository.save(review);
	}

	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

		review.setHouse(reviewEditForm.getHouse());
		review.setUser(reviewEditForm.getUser());
		review.setStars(reviewEditForm.getStars());
		review.setComment(reviewEditForm.getComment());

		reviewRepository.save(review);
	}

	//ログインユーザーがレビュー済みか判定する
	public boolean isReviewed(User user, House house) {
		if (user == null) {
			return false;
		}

		List<Review> reviews = reviewRepository.findAllByHouseAndUser(house, user);

		if (CollectionUtils.isEmpty(reviews)) {
			return false;
		}

		return true;
	}

}
