package com.enviro.assessment.junior.nhletelo.service;

import com.enviro.assessment.junior.nhletelo.dto.WithdrawalNoticeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generates CSV exports of withdrawal history for a given investor.
 * Writes directly to an OutputStream to avoid large in-memory string buffers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CsvExportService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String[] CSV_HEADER = {
            "Notice ID", "Product ID", "Product Name",
            "Withdrawal Amount (R)", "Balance Before (R)", "Balance After (R)",
            "Date & Time", "Status"
    };

    public void writeWithdrawalsCsv(List<WithdrawalNoticeDTO> notices, OutputStream outputStream)
            throws IOException {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {
            // Header row
            writer.println(String.join(",", CSV_HEADER));

            // Data rows
            for (WithdrawalNoticeDTO n : notices) {
                writer.printf("%d,%d,\"%s\",%.2f,%.2f,%.2f,\"%s\",%s%n",
                        n.getId(),
                        n.getProductId(),
                        escapeCsv(n.getProductName()),
                        n.getWithdrawalAmount(),
                        n.getBalanceBefore(),
                        n.getBalanceAfter(),
                        n.getCreatedAt().format(FORMATTER),
                        n.getStatus()
                );
            }
            writer.flush();
        }
        log.info("CSV export generated: {} records", notices.size());
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}
