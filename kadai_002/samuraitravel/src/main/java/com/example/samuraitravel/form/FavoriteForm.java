package com.example.samuraitravel.form;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

import lombok.Data;

@Data
public class FavoriteForm {

	private House house;

	private User user;

}
