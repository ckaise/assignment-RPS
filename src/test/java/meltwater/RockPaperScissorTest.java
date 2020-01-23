/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package meltwater;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meltwater.domain.GameRequest;
import meltwater.domain.GameResultType;
import meltwater.domain.HandType;
import meltwater.rest.RockPaperScissorController;
import meltwater.service.MatchService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest()
@AutoConfigureMockMvc
public class RockPaperScissorTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@SpyBean
	private MatchService service;

	@Test
	public void haveScore() throws Exception {
		when(service.generateComputerHand()).thenReturn(HandType.SCISSORS);

		GameRequest gameRequest = GameRequest.builder().hand(HandType.SCISSORS.name()).build();
		mockMvc.perform(buildRequest(gameRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.DRAW.name())))
			.andExpect(jsonPath("$.score.won", Matchers.equalTo(0)))
			.andExpect(jsonPath("$.score.draw", Matchers.equalTo(1)))
			.andExpect(jsonPath("$.score.lost", Matchers.equalTo(0)));

		when(service.generateComputerHand()).thenReturn(HandType.PAPER);

		GameRequest gameRequest2 = GameRequest.builder().hand(HandType.SCISSORS.name()).build();
		mockMvc.perform(buildRequest(gameRequest2))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.WON.name())))
			.andExpect(jsonPath("$.score.won", Matchers.equalTo(1)))
			.andExpect(jsonPath("$.score.draw", Matchers.equalTo(1)))
			.andExpect(jsonPath("$.score.lost", Matchers.equalTo(0)));

		when(service.generateComputerHand()).thenReturn(HandType.ROCK);

		GameRequest gameRequest3 = GameRequest.builder().hand(HandType.SCISSORS.name()).build();
		mockMvc.perform(buildRequest(gameRequest3))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.LOST.name())))
			.andExpect(jsonPath("$.score.won", Matchers.equalTo(1)))
			.andExpect(jsonPath("$.score.draw", Matchers.equalTo(1)))
			.andExpect(jsonPath("$.score.lost", Matchers.equalTo(1)));
	}

	@Test
	public void shouldReturnUnknown() throws Exception {
		GameRequest gameRequest = GameRequest.builder().hand("KICK").build();
		mockMvc.perform(buildRequest(gameRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.UNKNOWN.name())))
			.andExpect(jsonPath("$.score").doesNotExist());
	}

	@Test
	public void shouldWinByRules() throws Exception {
		for (HandType userHandType: HandType.values()) {
			for(HandType loser: MatchService.WIN_LOSE_MAP.get(userHandType)) {
				when(service.generateComputerHand()).thenReturn(loser);

				GameRequest gameRequest = GameRequest.builder().hand(userHandType.name()).build();
				mockMvc.perform(buildRequest(gameRequest))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.result", Matchers.is(GameResultType.WON.name())));
			}
		}
	}

	@Test
	public void shouldDraw() throws Exception {
		when(service.generateComputerHand()).thenReturn(HandType.SCISSORS);

		GameRequest gameRequest = GameRequest.builder().hand(HandType.SCISSORS.name()).build();
		mockMvc.perform(buildRequest(gameRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.DRAW.name())));
	}

	@Test
	public void shouldLost() throws Exception {
		when(service.generateComputerHand()).thenReturn(HandType.ROCK);

		GameRequest gameRequest = GameRequest.builder().hand(HandType.SCISSORS.name()).build();
		mockMvc.perform(buildRequest(gameRequest))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result", Matchers.is(GameResultType.LOST.name())));
	}

	private RequestBuilder buildRequest(GameRequest request) throws JsonProcessingException {
		String json = mapper.writeValueAsString(request);
		return MockMvcRequestBuilders
			.post(RockPaperScissorController.GAME_PATH)
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.accept(MediaType.APPLICATION_JSON);
	}

}
