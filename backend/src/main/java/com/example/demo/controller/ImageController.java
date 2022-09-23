package com.example.demo.controller;  
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

//import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.example.demo.azure.CustomVisionProject; 

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:http://localhost:8000}")
public class ImageController   
{  
    @RequestMapping("/hello")
    public String hello()
    {  
        return "Hello World";  
    }

    //@RequestMapping(value="/greeting",method=GET)
    //public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
/*
    @RequestMapping(value="/greeting",method=RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    
*/
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseEntity<String> posting(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        

        String imageName = UUID.randomUUID() + ".png";
        saveImageToFile(decode, imageName);
        //saveImageToAzure(imageName);
        CustomVisionProject.uploadImage(CustomVisionProject.tagDerekId, "./images/"+imageName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add("Training-Key", "5a585b5fabb24dddbe89f6406afbf71a");

        //HttpEntity<byte[]> entity = new HttpEntity<>(decode, headers);

        ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

        return result;

    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);

        String imageName = UUID.randomUUID() + ".png";
        saveImageToFile(decode, imageName);
        //saveImageToAzure(imageName);

        return CustomVisionProject.validate(decode);

    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File pathName = new File("./images/");
        if (!pathName.exists()) {
            pathName.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    private void saveImageToAzure(String fileName){
        // Create a BlobServiceClient object which will be used to create a container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString("DefaultEndpointsProtocol=https;AccountName=cs21003200201c7c4af;AccountKey=MDMjmHfzENRzNFItVVKAGgNGdDgTTOMWZMyYuFFRImkcz0BZkdYg387mNaV82DLEPKS2a4RSQDFO+AStdNFecg==;EndpointSuffix=core.windows.net").buildClient();

        //Create a unique name for the container
        String containerName = "images"; //change to the containerName as the same path name images
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        if( !containerClient.exists()){
            containerClient.create();
        }

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

        // Upload the blob
        blobClient.uploadFromFile("./images/" + fileName);

        
    }
}

