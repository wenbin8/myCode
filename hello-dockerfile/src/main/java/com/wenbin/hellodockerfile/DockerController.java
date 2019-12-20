package com.wenbin.hellodockerfile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerController {

    @GetMapping("/dockerfile")
    String dockerfile() {
        return "hello dockerfile";
    }
}
