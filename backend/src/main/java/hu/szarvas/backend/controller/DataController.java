package hu.szarvas.backend.controller;

import hu.szarvas.backend.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
@Slf4j
@RequiredArgsConstructor
public class DataController {
    private final DataFetchService dataFetchService;

    @PostMapping("/fetchareas")
    public ResponseEntity<Void> fetchAreas() {
        try {
            dataFetchService.fetchAndSaveAreas();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/fetchcompetitions")
    public ResponseEntity<Void> fetchCompetitions() {
        try {
            dataFetchService.fetchAndSaveCompetitions();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
