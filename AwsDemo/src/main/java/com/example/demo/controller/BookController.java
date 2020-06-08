package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Book;
import com.example.demo.service.AmazonS3ClientService;

@Controller
@RequestMapping("/book")
public class BookController {
	 @Autowired
	    private AmazonS3ClientService amazonS3ClientService;
	 
	
	 
	 @RequestMapping(value = "/add", method = RequestMethod.GET)
		public String addBook(Model model) {
			Book book = new Book();
			model.addAttribute("book", book);
			return "index";
		}

	
	 
	 @PostMapping(value = "/add")
		public String addBookPost(@ModelAttribute("book") Book book, HttpServletRequest request) {
		 
		 MultipartFile file= book.getImage();
		 
		 System.out.println(file.getOriginalFilename());
		 
		 String name=book.getId()+".png";
		 
		 this.amazonS3ClientService.uploadFileToS3Bucket(file, false,name);
		 
		 
		
		 return "index";
	 }
	 
	 ///Delete an image file
	 @RequestMapping(value="/delete",method=RequestMethod.POST)
	    public String deleteFile(@ModelAttribute("book") Book book)
	    
	    {
		 String fileName=book.getId()+".png";
		 System.out.println(fileName);
	        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
	       

//	        Map<String, String> response = new HashMap<>();
//	        response.put("message", "file [" + fileName + "] removing request submitted successfully.");

	        return "index";
	    }
	 
	 
	 ///get an image 
	 @GetMapping("/fet")
	  public ResponseEntity<byte[]> downloadFile() {
		    String keyname = "5.png";
		    ByteArrayOutputStream downloadInputStream = amazonS3ClientService.downloadFile(keyname);
		  
		    return ResponseEntity.ok()
		          .contentType(MediaType.IMAGE_PNG)
		          .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
		          .body(downloadInputStream.toByteArray());  
		  }
	 
	 
	 
//
//	    @PostMapping
//	    
//	    public Map<String, String> uploadFile(@RequestPart(value = "file") MultipartFile file)
//	    {
//	        this.amazonS3ClientService.uploadFileToS3Bucket(file, true);
//	        
//
//	        Map<String, String> response = new HashMap<>();
//	        response.put("message", "file [" + file.getOriginalFilename() + "] uploading request submitted successfully.");
//
//	        return response;
//	    }

//	    @DeleteMapping
//	    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
//	    {
//	        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
//
//	        Map<String, String> response = new HashMap<>();
//	        response.put("message", "file [" + fileName + "] removing request submitted successfully.");
//
//	        return response;
//	    }
}
