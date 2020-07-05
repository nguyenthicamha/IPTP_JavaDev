package com.iptp.iptpjavadev.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.iptp.iptpjavadev.service.IptpjavadevService;

@Controller
public class IptpjavadevController {

	@Autowired
	private IptpjavadevService iptpjavadevService;

	@Autowired
	public IptpjavadevController(IptpjavadevService iptpjavadevService) {
		this.iptpjavadevService = iptpjavadevService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		return "uploadForm";
	}

	@PostMapping("/back")
	public String backUpload(Model model) throws IOException {
		return "uploadForm";
	}

	@PostMapping("/upload-file")
	public String uploadFile(Model model, @RequestParam("file") MultipartFile file) {
		String name = file.getOriginalFilename();
		List<String[]> result;
		if (name.toLowerCase().endsWith(".xml")) {
			result = iptpjavadevService.getEmpIntervals(file);
			if(result != null && result.size() > 1) {
				model.addAttribute("msg", "From " + result.get(0)[0] + " To " + result.get(0)[1] + " has the empty intervals : ");
				model.addAttribute("results", result.get(1));
			}
			else {
				model.addAttribute("msg", result.get(0)[0]);
			}
		} else {
			model.addAttribute("msg", "Invalid file.");
		}
		return "result";

	}
}