package com.ssafy.Homezakaya.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentenceDto {
	private int topicId;
	private String content;
	private String strPerson; // 음성인식으로 입력 받은 값
	
}
