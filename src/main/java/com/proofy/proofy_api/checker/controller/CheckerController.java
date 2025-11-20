package com.proofy.proofy_api.checker.controller;

import com.proofy.proofy_api.checker.dto.ProofCheckRequest;
import com.proofy.proofy_api.checker.dto.ProofErrorResponse;
import com.proofy.proofy_api.checker.dto.ProofResponse;
import com.proofy.proofy_api.checker.service.CheckerService;
import com.proofy.proofy_api.global.ApiResponse;
import com.proofy.proofy_api.global.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checker")
public class CheckerController {
    private final CheckerService checkerService;

    @PostMapping("/check")
    public ResponseEntity<ProofResponse> checkProof(@RequestBody ProofCheckRequest request) {

        List<ProofErrorResponse> errors = checkerService.checkProof(request);

        if (errors.isEmpty()) {
            return ResponseEntity.ok(ProofResponse.ok());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(ProofResponse.fail(errors));
        }
    }
}
