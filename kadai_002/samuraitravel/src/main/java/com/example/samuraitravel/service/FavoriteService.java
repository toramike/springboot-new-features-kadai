package com.example.samuraitravel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteForm;
import com.example.samuraitravel.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;

	}

	//お気に入り登録する
	@Transactional
	public void addFavorite(FavoriteForm favoriteForm, House house, User user) {
		Favorite favorite = new Favorite();

		favorite.setHouse(house);
		favorite.setUser(user);

		favoriteRepository.save(favorite);
	}

	//ログインユーザーがお気に入り済みか判定する

	public boolean isFavorited(User user, House house) {
		if (user == null) {
			return false;
		}
		List<Favorite> favorites = favoriteRepository.findAllByHouseAndUser(house, user);
		if (CollectionUtils.isEmpty(favorites)) {
			return false;
		}
		return true;
	}

}
