package com.atruiz.batch.bo;

import java.util.List;

import lombok.Data;

@Data
public class MalAnimeList {
	
	private String request_hash;
	private String request_cached;
	private List<MalAnime> result;

}
