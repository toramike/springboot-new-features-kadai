package com.example.samuraitravel.form;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {

	@NotNull
	private Integer id;

	private House house;

	private User user;

	@NotNull(message = "星の数を入力してください。")
	private Integer stars;

	@NotBlank(message = "コメントを入力してください。")
	private String comment;

}
