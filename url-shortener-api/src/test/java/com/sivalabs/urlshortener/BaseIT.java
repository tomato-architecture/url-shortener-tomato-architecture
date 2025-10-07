package com.sivalabs.urlshortener;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.urlshortener.api.dtos.JwtToken;
import com.sivalabs.urlshortener.api.utils.JwtTokenHelper;
import com.sivalabs.urlshortener.domain.entities.User;
import com.sivalabs.urlshortener.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
public abstract class BaseIT {
    @Autowired
    protected MockMvcTester mockMvcTester;

    @Autowired
    protected JwtTokenHelper jwtTokenHelper;

    @Autowired
    protected UserRepository userRepository;

    protected String getJwtTokenHeaderValue(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        JwtToken jwtToken = jwtTokenHelper.generateToken(user);
        return "Bearer " + jwtToken.token();
    }
}
