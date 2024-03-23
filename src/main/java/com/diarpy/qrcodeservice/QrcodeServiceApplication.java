package com.diarpy.qrcodeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

/**
 * @author Mack_TB
 * @since 20/03/2024
 * @version 1.0.2
 */

@SpringBootApplication
public class QrcodeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrcodeServiceApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
