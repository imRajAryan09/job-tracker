package com.tracker.controller;

import com.tracker.constants.ApiEndPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by Raj Aryan,
 * created on 24/09/2024
 */
@RestController
public class TestController {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${git.commit.id.full}")
    private String commitId;

    @Value("${git.branch}")
    private String branch;


    @GetMapping(value = ApiEndPoint.PING)
    public ResponseEntity<String> homePage() {
        var message = String.format(
                "<div style=\"display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; text-align: center;\">"
                        + "<div>"
                        + "<h1 style=\"font-size: 2.5em;\">Hello World!</h1>"
                        + "<pre style=\"font-size: 1.5em; word-wrap: break-word; white-space: pre-wrap;\">"
                        + "This is %s. <br>Branch: %s <br>CommitId: %s"
                        + "</pre>"
                        + "</div>"
                        + "</div>",
                appName, branch, commitId);
        return ResponseEntity.ok(message);
    }
}
