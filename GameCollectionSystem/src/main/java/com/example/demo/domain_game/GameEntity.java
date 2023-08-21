package com.example.demo.domain_game;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor // 追加
@Data
public class GameEntity {
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String summary;
	private String genre;
	private Long score;
	private String comment;
}
