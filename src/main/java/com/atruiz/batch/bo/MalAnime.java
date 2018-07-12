package com.atruiz.batch.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MalAnime {
	
	private String mal_id;
	private String url;
	private String image_url;
	private String title;
	private String fansub;
	private String description;
	private String type;
	private String score;
	private String episodes;
	private String members;

}
