package com.example.BTL_IOT.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileService {
	public static void saveFile(String upLoadDir,String fileName,MultipartFile multipartFile) throws IOException {
		 Path path = Paths.get(upLoadDir);
		 if (!Files.exists(path)) {
			 Files.createDirectories(path);
		 }
		 try (InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = path.resolve(fileName);
			Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			// TODO: handle exception
			throw new IOException("khong tim thay file");
		}
	 }
}
