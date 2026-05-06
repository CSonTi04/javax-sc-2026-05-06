package training.configclientdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;

//demo.prefix néven fogja majd keresni
@ConfigurationProperties(prefix = "demo")
public record DemoProperties(String prefix) {

}