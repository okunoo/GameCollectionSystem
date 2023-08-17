package com.example.demo.web.game;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditGameForm {
	@Id
	private long id;
	
	@NotBlank
	@Size(max=256)
	private String summary;
	
	@NotBlank
	@Size(max=256)
	private String genre;
	
	private long score;
	
	@Size(max=1000)
	private String comment;
}
