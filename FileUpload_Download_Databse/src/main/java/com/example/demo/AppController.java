package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {

	@Autowired
	private DocumentRepository repo;

	@GetMapping("/")
	public String viewHomePage(Model model) {
		List<Document> listDocs = repo.findAll();
		model.addAttribute("listDocs", listDocs);
		return "home";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("document") MultipartFile multipartFile, RedirectAttributes re)
			throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		Document document = new Document();
		document.setName(fileName);
		document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		document.setUploadTime(new Date());

		repo.save(document);
		re.addFlashAttribute("message", "The file has been upload successfully!!");
		return "redirect:/";

	}

	@GetMapping("/download")
	public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
		Optional<Document> result = repo.findById(id);
		if (!result.isPresent()) {
			throw new Exception("Could not find Document with ID:" + id);
		}
		Document document = result.get();
		//tells the browser the content is binary file ,should be saved to disk
		response.setContentType("application/octet-stream");
		
		
		
		//Http Response -> inline -> webpage or part of the webpage
		
		// attachement  -> downloaded and saved locally
		String headerKey = "Content-Disposition";
		//tells the browser downlad the file given name
		String headerValue = "attachment; filename=" + document.getName();
		response.setHeader(headerKey, headerValue);
		ServletOutputStream outputStream=  response.getOutputStream();
		outputStream.write(document.getContent());
		outputStream.close();

	}

}
