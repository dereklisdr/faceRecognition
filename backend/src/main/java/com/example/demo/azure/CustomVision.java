package com.example.demo.azure;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CustomVision {
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseEntity<String> posting(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);

        //String imageName = UUID.randomUUID() + ".png";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add("Training-Key", "5a585b5fabb24dddbe89f6406afbf71a");

        HttpEntity<byte[]> entity = new HttpEntity<>(decode, headers);
        //String URL = "https://westus2.api.cognitive.microsoft.com/customvision/v3.3/training/projects/cd621fcb-704f-48ab-bdb0-ad1e4e72e2b2/images?tagIds=b3ce2a88-cbad-4fcb-916c-8a5513768359";
        //ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);

        ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

        return result;

    }

}
