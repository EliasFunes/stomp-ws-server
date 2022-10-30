package com.qrSignInServer.controllers;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qrSignInServer.config.security.JwtTokenUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping(value = "/qr")
public class QRController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/genQR")
    public void genQR(HttpServletResponse response) {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

//        String contents = "https://simplesolution.dev";
        String QRID = "IDQR_" + UUID.randomUUID().toString();
        String contents = jwtTokenUtil.generateQRIDToken(QRID);
        int width = 100;
        int height = 100;

        try {
            // generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(contents, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
            IOUtils.copy(inStream, response.getOutputStream());

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping(value = "/scanQR")
    public @ResponseBody String scanQR(@RequestParam("qrCodeFile") MultipartFile qrCodeFile) throws IOException, NotFoundException {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrCodeFile.getBytes());
        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
        BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Result result = multiFormatReader.decode(binaryBitmap);
        String stringResult = result.getText();
        return stringResult;
    }


}
