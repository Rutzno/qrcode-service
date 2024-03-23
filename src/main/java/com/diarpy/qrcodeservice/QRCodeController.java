package com.diarpy.qrcodeservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mack_TB
 * @since 20/03/2024
 * @version 1.0.0
 */

@RestController
@RequestMapping("/api")
public class QRCodeController {

    // allows clients to ping the service, verifying its operation and availability.
    @GetMapping("/health")
    public ResponseEntity<Object> getHealth() {
        return ResponseEntity.ok().build();
    }

    // used by clients to retrieve QR code images
    @GetMapping("/qrcode")
    @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
    public void getQrcode() {

    }
}
