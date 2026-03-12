package com.murillo.bufonio.controller;

import com.murillo.bufonio.dto.ParchmentDTO;
import com.murillo.bufonio.dto.summary.ParchmentSummary;
import com.murillo.bufonio.model.Parchment;
import com.murillo.bufonio.service.ParchmentService;
import com.murillo.bufonio.util.LocationUtil;
import com.murillo.bufonio.util.mapper.ParchmentMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/channels/{tokenChannel}/parchments")
@Validated
public class ParchmentController {

    private final ParchmentService parchmentService;
    private final ParchmentMapper parchmentMapper;

    public ParchmentController(ParchmentService parchmentService, ParchmentMapper parchmentMapper) {
        this.parchmentService = parchmentService;
        this.parchmentMapper = parchmentMapper;
    }

    @PostMapping
    public ResponseEntity<ParchmentSummary> createParchment(
            @NotBlank @PathVariable String tokenChannel) {
        Parchment parchment = parchmentService.createParchment(tokenChannel);

        ParchmentSummary summary = new ParchmentSummary() {
            @Override
            public Long getIdParchment() { return parchment.getIdParchment(); }
            @Override
            public String getBufonioMessage() { return parchment.getBufonioMessage(); }
            @Override
            public LocalDateTime getCreatedAt() { return parchment.getCreatedAt(); }
            @Override
            public int getUrgencyLevel() { return parchment.getUrgencyLevel(); }
        };

        URI location = LocationUtil.getLocation("/api/parchments", parchment.getIdParchment());
        return ResponseEntity.created(location).body(summary);
    }

    @GetMapping
    public ResponseEntity<List<ParchmentSummary>> getParchmentSummaries(
            @NotBlank @PathVariable String tokenChannel) {
        List<ParchmentSummary> summaries = parchmentService.getParchmentSummariesByTokenChannel(tokenChannel);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{idParchment}")
    public ResponseEntity<ParchmentDTO> getParchmentByTokenChannelAndIdParchment(
            @PathVariable String tokenChannel,
            @PathVariable @NotNull @Positive Long idParchment) {

        Parchment parchment = parchmentService.getParchmentByTokenChannelAndIdParchment(tokenChannel, idParchment);
        return ResponseEntity.ok(parchmentMapper.toDTO(parchment));
    }
}