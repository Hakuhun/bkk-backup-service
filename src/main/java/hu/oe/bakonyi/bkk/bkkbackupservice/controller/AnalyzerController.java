package hu.oe.bakonyi.bkk.bkkbackupservice.controller;

import hu.oe.bakonyi.bkk.bkkbackupservice.model.ConditionalPossibilityResponse;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.ConditionalQueryingRequest;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.QueryingCase;
import hu.oe.bakonyi.bkk.bkkbackupservice.service.BackupDataAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bkk")
public class AnalyzerController {

    @Autowired
    BackupDataAnalyzer analyzer;

    @PostMapping("/prod/analyze")
    public ResponseEntity<ConditionalPossibilityResponse> calculateConditionalProbability(@RequestBody ConditionalQueryingRequest data) {
        ConditionalPossibilityResponse response = analyzer.conditionalProbability(data);
        return ResponseEntity.ok(analyzer.conditionalProbability(data));
    }

}
