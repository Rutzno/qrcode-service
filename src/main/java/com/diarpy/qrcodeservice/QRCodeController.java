package com.diarpy.qrcodeservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    // used by clients to retrieve QR code images
    @GetMapping("/qrcode")
    public ResponseEntity<byte[]> getImage() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 250, 250);

        try(var baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(bytes);
        } /*catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
