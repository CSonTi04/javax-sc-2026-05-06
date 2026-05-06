package training.configclientdemo;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/hello")
@RequiredArgsConstructor
@EnableConfigurationProperties(DemoProperties.class)
public class HelloController {

    private final DemoProperties demoProperties;

    public HelloController(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @GetMapping
    public String hello() {
        return demoProperties.prefix().concat(String.valueOf(LocalDateTime.now()));
    }

}
