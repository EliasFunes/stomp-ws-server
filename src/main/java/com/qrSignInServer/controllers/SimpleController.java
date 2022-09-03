package com.qrSignInServer.controllers;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.models.Student;
import com.qrSignInServer.services.SimpleService;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Controller
@RequestMapping(value = "/test")
public class SimpleController {

    @Autowired
    SimpleService simpleService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping(value = "/student/{studentId}")
    public @ResponseBody
    Student getTestData(@PathVariable Integer studentId) {
        Student student = new Student();
        student.setName("Peter");
        student.setId(studentId);
        return student;
    }

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
    public @ResponseBody String scanQR(@RequestParam("qrCodeFile")MultipartFile qrCodeFile) throws IOException, NotFoundException {

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

    @GetMapping(value = "/jdbc_test")
    public @ResponseBody String jdbcTest() {
        return simpleService.getData();
    }

    @PostMapping(value = "/sendToUser")
    public void userRegister(@RequestHeader HttpHeaders headers, @RequestBody @Valid HashMap<String, String> data) throws ExecutionException, InterruptedException {
 //TODO: ver si se puede refactorizar algo en un utils.

        String tokenQR = data.get("tokenQr");
        String qrId = jwtTokenUtil.getQRIDFromToken(tokenQR);
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        List<Transport> transports =
                Collections.<Transport>singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transports));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandlerAdapter handler =
                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        logger.info("Connected to {}", session);
                        session.send("/app/hello_user", qrId);
                        session.disconnect();
                    }
                    @Override
                    public void handleException(
                            StompSession session,
                            StompCommand command,
                            StompHeaders headers,
                            byte[] payload,
                            Throwable exception) {
                        throw new RuntimeException(exception);
                    }
                };
        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("X-Authorization", token);
        connectHeaders.add("username", "username");
        stompClient.connect("ws://127.0.0.1:8080/wsc", handshakeHeaders, connectHeaders, handler, new Object[0]);
        logger.info("hello done");

    }

}
