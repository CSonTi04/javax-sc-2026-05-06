package training.configclientdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/hello")
@RequiredArgsConstructor
@EnableConfigurationProperties(DemoProperties.class)
public class HelloController {

    private final DemoProperties demoProperties;

    @GetMapping
    public String hello() {
        return demoProperties.prefix().concat(String.valueOf(LocalDateTime.now()));
    }

}
