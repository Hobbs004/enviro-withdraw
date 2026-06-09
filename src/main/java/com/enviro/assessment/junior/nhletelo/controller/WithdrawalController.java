package com.enviro.assessment.junior.nhletelo.controller;

import com.enviro.assessment.junior.nhletelo.dto.*;
import com.enviro.assessment.junior.nhletelo.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/*
 * REST endpoints for withdrawal notices and CSV export.
 * POST /api/withdrawals                          - submit a withdrawal
 * GET  /api/withdrawals/investor/{id}            - history for an investor
 * GET  /api/withdrawals/investor/{id}/export     - CSV download with optional date filter */
@RestController
@RequestMapping("/api/withdrawals")

public class WithdrawalController {

    private final WithdrawalService withdrawalService;
    private final CsvExportService csvExportService;


    public WithdrawalController(WithdrawalService withdrawalService, CsvExportService csvExportService) {
    this.withdrawalService = withdrawalService;
    this.csvExportService = csvExportService;
}

    @PostMapping
    public ResponseEntity<ApiResponseDTO<WithdrawalNoticeDTO>> createWithdrawal(
            @Valid @RequestBody WithdrawalRequestDTO request) {
        WithdrawalNoticeDTO result = withdrawalService.createWithdrawal(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Withdrawal approved successfully", result));
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<ApiResponseDTO<List<WithdrawalNoticeDTO>>> getWithdrawalHistory(
            @PathVariable Long investorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<WithdrawalNoticeDTO> notices;
        if (from != null && to != null) {
            notices = withdrawalService.getWithdrawalsByInvestorIdAndDateRange(investorId, from, to);
        } else {
            notices = withdrawalService.getWithdrawalsByInvestorId(investorId);
        }
        return ResponseEntity.ok(ApiResponseDTO.ok("Withdrawal history retrieved", notices));
    }

    @GetMapping("/investor/{investorId}/export")
    public void exportCsv(
            @PathVariable Long investorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            HttpServletResponse response) throws IOException {

        List<WithdrawalNoticeDTO> notices;
        if (from != null && to != null) {
            notices = withdrawalService.getWithdrawalsByInvestorIdAndDateRange(investorId, from, to);
        } else {
            notices = withdrawalService.getWithdrawalsByInvestorId(investorId);
        }

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"withdrawal-statement-investor-" + investorId + ".csv\"");
        csvExportService.writeWithdrawalsCsv(notices, response.getOutputStream());
    }
}
