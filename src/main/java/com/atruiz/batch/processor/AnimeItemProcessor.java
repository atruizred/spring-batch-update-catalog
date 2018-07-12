package com.atruiz.batch.processor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.atruiz.batch.bo.AnimeCsv;
import com.atruiz.batch.bo.MalAnime;
import com.atruiz.batch.bo.MalAnimeList;
import com.google.gson.Gson;

/**
 * Using JIKAN MAL Unofficial API - https://jikan.docs.apiary.io/#
 * 
 * 
 * @author aruiz
 *
 */
public class AnimeItemProcessor implements ItemProcessor<AnimeCsv, MalAnime> {
	
	private static final Logger log = LoggerFactory.getLogger(AnimeItemProcessor.class);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@Override
	public MalAnime process(AnimeCsv item) throws Exception {
		
		log.info("Searching (" + item + ")");
		
		/*
		 * 
		 */
		
		String uri = "https://api.jikan.moe/search/anime/"+ item.getName() +"/1";
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
		
		String json = IOUtils.toString(br);;

		br.close();
		conn.disconnect();
		
		MalAnimeList data = new Gson().fromJson(json, MalAnimeList.class);
		
		for (MalAnime result : data.getResult()) {
			if (result.getTitle().equalsIgnoreCase(item.getName())) {
				log.info("Anime found in MAL (" + result.getTitle() + ")");
				result.setFansub(item.getFansub());
				return result;
			}
		}
		
		//TODO new recursive call for other pages
		
		
		log.info("Anime not found in MAL");
		return null;
	}

}
