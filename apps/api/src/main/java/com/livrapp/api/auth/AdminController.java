package com.livrapp.api.auth;

import com.livrapp.api.auth.dto.CreateCoAdminRequest;
import com.livrapp.api.auth.dto.UserSummary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/co-admins")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserSummary> createCoAdmin(@Valid @RequestBody CreateCoAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createCoAdmin(request));
    }
}
