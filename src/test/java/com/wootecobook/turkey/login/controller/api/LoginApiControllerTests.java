package com.wootecobook.turkey.login.controller.api;

import com.wootecobook.turkey.login.service.dto.LoginRequest;
import com.wootecobook.turkey.user.controller.BaseControllerTests;
import com.wootecobook.turkey.user.controller.api.ErrorMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.wootecobook.turkey.commons.exception.NotLoginException.NOT_LOGIN_MESSAGE;
import static com.wootecobook.turkey.login.service.exception.LoginFailException.LOGIN_FAIL_MESSAGE;
import static com.wootecobook.turkey.user.domain.User.INVALID_PASSWORD_MESSAGE;
import static com.wootecobook.turkey.user.service.exception.NotFoundUserException.NOT_FOUND_USER_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginApiControllerTests extends BaseControllerTests {

    private static final String VALID_EMAIL = "login@test.test";
    private static final String VALID_NAME = "name";
    private static final String VALID_PASSWORD = "passWORD1!";

    @Autowired
    private WebTestClient webTestClient;

    Long userId;

    @BeforeEach
    void setUp() {
        userId = addUser(VALID_NAME, VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    void 로그인() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .build();

        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 없는_email로_로그인() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("invalid@invalid.invalid")
                .password(VALID_PASSWORD)
                .build();

        ErrorMessage errorMessage = webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorMessage.getErrorMessage()).contains(LOGIN_FAIL_MESSAGE, NOT_FOUND_USER_MESSAGE);
    }

    @Test
    void 잘못된_비밀번호로_로그인() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(VALID_EMAIL)
                .password("invalid")
                .build();

        ErrorMessage errorMessage = webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(loginRequest), LoginRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorMessage.getErrorMessage()).contains(LOGIN_FAIL_MESSAGE, INVALID_PASSWORD_MESSAGE);
    }

    @Test
    void 로그아웃() {
        webTestClient.post()
                .uri("/logout")
                .cookie("JSESSIONID", logIn(VALID_EMAIL, VALID_PASSWORD))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 로그인_안된_상태에서_로그아웃() {
        ErrorMessage errorMessage = webTestClient.post()
                .uri("/logout")
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ErrorMessage.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorMessage.getErrorMessage()).isEqualTo(NOT_LOGIN_MESSAGE);
    }

    @AfterEach
    void tearDown() {
        deleteUser(userId);
    }

}