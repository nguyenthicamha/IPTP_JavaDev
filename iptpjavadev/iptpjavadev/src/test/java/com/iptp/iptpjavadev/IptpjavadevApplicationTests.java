package com.iptp.iptpjavadev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.iptp.iptpjavadev.service.IptpjavadevService;


@SpringBootTest
class IptpjavadevApplicationTests {
	
@Autowired
public IptpjavadevService iptpjavadevService;

	@Test
	public void testEmployee() throws Exception {
	      try {
	    	  Path path = Paths.get("D:\\HA\\IPTPNetwork\\data_java-test.xml");
	    	  String name = "data_java-test.xml";
	    	  String originalFileName = "data_java-test.xml";
	    	  String contentType = "text/plain";
	    	  byte[] content = null;
	    	  try {
	    	      content = Files.readAllBytes(path);
	    	  } catch (final IOException e) {
	    	  }
	    	  MultipartFile file = new MockMultipartFile(name,originalFileName, contentType, content);
	    	  List<String[]> result = iptpjavadevService.getEmpIntervals(file);
	    	 	if(result != null && result.size() > 1) {
					System.out.println("From " + result.get(0)[0] + " To " + result.get(0)[1] + " has the empty intervals : ");
					for(String s : result.get(1)) {
						System.out.println(s);
					}
				}
				else {
					System.out.println(result.get(0)[0]);
				}
	          
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	}
}
