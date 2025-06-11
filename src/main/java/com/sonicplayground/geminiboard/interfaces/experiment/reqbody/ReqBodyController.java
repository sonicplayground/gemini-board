package com.sonicplayground.geminiboard.interfaces.experiment.reqbody;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/experiments/reqbody")
public class ReqBodyController {

    @GetMapping("/exampleClass")
    public ResponseEntity<Map<String, String>> getExampleClass(ImmutableClassCase example) {
        System.out.println("getExampleClass example = " + example);
        return ResponseEntity.ok(Map.of("message", "Request body example"));
    }

    @GetMapping("/exampleRecord")
    public ResponseEntity<Map<String, String>> getExampleRecord(RecordCase example) {
        System.out.println("getExampleRecord example = " + example);
        return ResponseEntity.ok(Map.of("message", "Request body example"));
    }

    @PostMapping("/exampleClass")
    public ResponseEntity<Map<String, String>> postExampleClass(@RequestBody ImmutableClassCase example) {
        System.out.println("postExampleClass example = " + example);
        return ResponseEntity.ok(Map.of("message", "Request body example"));
    }

    @PostMapping("/exampleRecord")
    public ResponseEntity<Map<String, String>> postExampleRecord(@RequestBody RecordCase example) {
        System.out.println("postExampleRecord example = " + example);
        return ResponseEntity.ok(Map.of("message", "Request body example"));
    }
}
