package com.chasion.eventbackend.controller;

import com.chasion.eventbackend.entity.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*", // 或列出具体头部（如 "Authorization", "Content-Type"）
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH}
)
public class UploadController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.load.path}")
    private String loadPath;

    // 响应文件上传
    @PostMapping("/api/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 创建上传目录
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + ext;

            // 保存文件
            Path path = Paths.get(uploadPath + "/" + newFileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 返回访问路径
            // http://localhost:8080/newFileName
            String fileUrl = loadPath + newFileName;
            return Result.success("上传成功", fileUrl);
        }catch (Exception e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }

//     响应文件获取
//    http://localhost:8080/newFileName
    @GetMapping("/{newFileName}")
    public Result getPicture(@PathVariable("newFileName") String newFileName, HttpServletResponse response) {
        // 从本地服务器加载图片
        String filePath = uploadPath + "/" + newFileName;
        // 文件的后缀
        String ext = newFileName.substring(newFileName.lastIndexOf(".")+1);
        // 响应图片
//        response.setContentType("image/" + ext);

        // 向页面写入数据
        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(filePath)
        ){
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1){
                os.write(buffer, 0 , len);
            }
            return Result.success(null);
        }catch (Exception e) {
            return Result.error("加载失败");
        }
    }


}
