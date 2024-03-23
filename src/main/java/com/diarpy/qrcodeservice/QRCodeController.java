package com.diarpy.qrcodeservice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Mack_TB
 * @since 20/03/2024
 * @version 1.0.4
 */

@RestController
@RequestMapping("/api")
public class QRCodeController {
    final Set<String> SUPPORTED_TYPES = Set.of("png", "jpeg", "gif");
    final Map<String, MediaType> MEDIATYPES = Map.of("png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF);

    // allows clients to ping the service, verifying its operation and availability.
    @GetMapping("/health")
    public ResponseEntity<Object> getHealth() {
        return ResponseEntity.ok().build();
    }

    // used by clients to retrieve QR code images
    @GetMapping("/qrcode")
    public ResponseEntity<Object> getImage(@RequestParam int size, @RequestParam String type) throws IOException {
        if (size < 150 || size > 350) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Image size must be between 150 and 350 pixels"));
        } else {
            if (!SUPPORTED_TYPES.contains(type)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
            } else {
                BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, size, size);

                try (var baos = new ByteArrayOutputStream()) {
                    ImageIO.write(bufferedImage, type, baos);
                    byte[] bytes = baos.toByteArray();
                    return ResponseEntity
                            .ok()
                            .contentType(MEDIATYPES.get(type))
                            .body(bytes);
                } /*catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
