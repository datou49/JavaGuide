package com.javaguide.spring;

import com.javaguide.spring.entity.User;
import com.javaguide.spring.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * javaguide-spring 冒烟测试：验证 IoC/AOP/事务/校验/全局异常。
 */
@SpringBootTest
@AutoConfigureMockMvc
class SpringSmokeTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void hello_works() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, JavaGuide!"));
    }

    @Test
    void aop_works() throws Exception {
        mockMvc.perform(get("/api/hello")).andExpect(status().isOk());
        // AOP 切点匹配 service 包，UserService 会被代理
        org.assertj.core.api.Assertions.assertThat(userService.getClass().getName())
                .contains("CGLIB");
    }

    @Test
    void validation_works() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"age\":-1,\"email\":\"bad\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void transaction_rollback_works() throws Exception {
        long before = userService.count();
        mockMvc.perform(post("/api/order/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fail\":true}"))
                .andExpect(status().isBadRequest());
        org.assertj.core.api.Assertions.assertThat(userService.count()).isEqualTo(before);
    }

    @Test
    void save_valid_user() throws Exception {
        User u = new User();
        u.setName("test");
        u.setAge(20);
        u.setEmail("a@b.com");
        userService.createUser(u);
        org.assertj.core.api.Assertions.assertThat(userService.count()).isGreaterThan(0);
    }
}
