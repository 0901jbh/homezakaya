package com.ssafy.Homezakaya.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.Homezakaya.model.dao.SentenceDao;
import com.ssafy.Homezakaya.model.dto.SentenceDto;

@Service
public class SentenceServiceImpl implements SentenceService {
	
	private SentenceDao sentenceDao;
	
	@Autowired
	public SentenceServiceImpl(SentenceDao sentenceDao) {
		super();
		this.sentenceDao = sentenceDao;
	}
	@Override
	public List<SentenceDto> sentenceList() {
		return sentenceDao.sentenceList();
	}
	
	//정확도 검사
	@Override
	public int calculateAccuracy(String s1, String s2) {
		s1 = s1.replaceAll(" ",  "");
		s2 = s2.replaceAll(" ",  "");
		String longer = s1, shorter = s2;

		if (s1.length() < s2.length()) {
			longer = s2;
			shorter = s1;
		}

		int longerLength = longer.length();
		if (longerLength == 0) return 100;
		double tmp = (longerLength - editDistance(longer, shorter)) / (double) longerLength;
		return (int)Math.floor(tmp*100);
	}
	private static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		int[] costs = new int[s2.length() + 1];

		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0) {
					costs[j] = j;
				} else {
					if (j > 0) {
						int newValue = costs[j - 1];

						if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						}

						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}

			if (i > 0) costs[s2.length()] = lastValue;
		}

		return costs[s2.length()];
	}
}
