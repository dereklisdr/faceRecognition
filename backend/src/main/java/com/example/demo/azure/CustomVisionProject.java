package com.example.demo.azure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CustomVisionProject {

    static RestTemplate restTemplate = new RestTemplate();
    static String projectName = "imageClassifer";
    static String projectID = "78b447c8-b489-4fe0-a965-7cc545a12ec2";
    static String trainingEndpoint = "eastus.api.cognitive.microsoft.com/";
    static String trainingApiKey = "da7dddb178b14c35ba4f0f799a38768b";
    static String predictionResourceId = "/subscriptions/287c850b-98d3-4192-80c9-562d28390e2b/resourceGroups/webCamera/providers/Microsoft.CognitiveServices/accounts/cv_derek";
    static String publishName = "derekImage"; //after publish
    public static String tagName1 = "derek";
    public static String tagName2 = "others";
    public static String tagDerekId = "cbc8694e-14c4-41f9-ba17-e6655781bec2";
    public static String tagOthersId = "686ba0b7-0f75-4f70-8b24-a537fc5ec499";

    public static void main(String[] args) throws JSONException, IOException {

        //CustomVisionProject cv = new CustomVisionProject();
        //cv.createTag(tagName1);
        //cv.createTag(tagName2);
        //uploadImage(tagDerekId, "D:\\docs_sync\\sandbox\\web_camera\\backend\\images\\1c5086c8-fffa-4ba7-b22e-3f6e1ac11b84.png");

        Path path = Paths.get("D:\\docs_sync\\sandbox\\web_camera\\backend\\images\\1c5086c8-fffa-4ba7-b22e-3f6e1ac11b84.png");
        byte[] imageFile = Files.readAllBytes(path);

        validate(imageFile);

    }

    public String createTag(String tagName) throws JSONException {
        String url = "https://{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("name", tagName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        
        JSONObject jsonObject = new JSONObject(response.getBody());
        String id = jsonObject.getString("id");
        System.out.println(id);

        return id;
    }

    public static void uploadImage(String tagID, String fileName) throws JSONException, IOException {
        String url = "https://{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagID);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);

        Path path = Paths.get(fileName);
        byte[] imageFile = Files.readAllBytes(path);

        HttpEntity<byte[]> request = new HttpEntity<>(imageFile, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        System.out.println(response.getBody());
    }

    public static ResponseEntity<String> validate(byte[] data) throws IOException {
    
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", "da7dddb178b14c35ba4f0f799a38768b");
    
        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        String URL = "https://eastus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/78b447c8-b489-4fe0-a965-7cc545a12ec2/classify/iterations/derekImage/image";
        ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);
        System.out.println(result);
        return result;
    }

}
