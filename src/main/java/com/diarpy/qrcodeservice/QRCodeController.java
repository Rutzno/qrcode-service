package com.diarpy.qrcodeservice;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Mack_TB
 * @since 20/03/2024
 * @version 1.0.5
 */

@RestController
@RequestMapping("/api")
public class QRCodeController {
    private final Set<Character> SUPPORTED_CORRECTION_LEVELS = Set.of('L', 'M', 'Q', 'H');
    private final Map<Character, ErrorCorrectionLevel> CORRECTION_LEVELS = Map.of('L', ErrorCorrectionLevel.L,
            'M', ErrorCorrectionLevel.M,
            'Q', ErrorCorrectionLevel.Q,
            'H', ErrorCorrectionLevel.H);
    private final Set<String> SUPPORTED_TYPES = Set.of("png", "jpeg", "gif");
    private final Map<String, MediaType> MEDIATYPES = Map.of("png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF);

    // allows clients to ping the service, verifying its operation and availability.
    @GetMapping("/health")
    public ResponseEntity<Object> getHealth() {
        return ResponseEntity.ok().build();
    }

    // used by clients to retrieve QR code images
    @GetMapping("/qrcode")
    public ResponseEntity<Object> getImage(@RequestParam String contents,
                                           @RequestParam(defaultValue = "250") int size,
                                           @RequestParam(defaultValue = "L") char correction,
                                           @RequestParam(defaultValue = "png") String type) throws IOException {
        if (contents.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Contents cannot be null or blank"));
        }
        if (size < 150 || size > 350) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Image size must be between 150 and 350 pixels"));
        } if (!SUPPORTED_CORRECTION_LEVELS.contains(correction)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Permitted error correction levels are L, M, Q, H"));
        }
        if (!SUPPORTED_TYPES.contains(type)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
        }
               /* BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, size, size);*/

        QRCodeWriter writer = new QRCodeWriter();
        try (var baos = new ByteArrayOutputStream()) {
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, CORRECTION_LEVELS.get(correction));
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ImageIO.write(bufferedImage, type, baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity
                    .ok()
                    .contentType(MEDIATYPES.get(type))
                    .body(bytes);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}