package sg.edu.nus.iss.server.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.server.services.S3Service;

@Controller
@RequestMapping
public class FileUploadController {

    @Autowired
    private S3Service s3Svc;
    
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> postUpload(
        @RequestPart MultipartFile imageFile, @RequestPart String title, @RequestPart String comment
    ) {
        System.out.printf("Title: %s\n", title);
        System.out.printf("Comment: %s\n", comment);
        System.out.printf("File name: %s\n", imageFile.getOriginalFilename());
        System.out.printf("File type: %s\n", imageFile.getContentType());

        String key = "";

        try {
            key = s3Svc.upload(imageFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JsonObject jo = Json.createObjectBuilder()
                            .add("image key:", key)
                            .build();
        return ResponseEntity.ok(jo.toString());
    }
}
