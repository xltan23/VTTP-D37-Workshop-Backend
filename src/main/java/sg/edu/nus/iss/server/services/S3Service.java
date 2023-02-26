package sg.edu.nus.iss.server.services;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 s3;

    public String upload(MultipartFile mpf) throws IOException {
        Map<String,String> userData = new HashMap<>();
        userData.put("name", "xl");
        userData.put("uploadTime", (new Date()).toString());
        userData.put("originalFilename", mpf.getOriginalFilename());

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(mpf.getSize());
        metaData.setUserMetadata(userData);

        String key = UUID.randomUUID().toString().substring(0,8);
        PutObjectRequest putReq = new PutObjectRequest("xlbucket", key, mpf.getInputStream(), metaData);
        putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(putReq);
        return key;
    }
}
