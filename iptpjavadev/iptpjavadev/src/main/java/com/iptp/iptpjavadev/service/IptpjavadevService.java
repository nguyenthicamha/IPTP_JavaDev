package com.iptp.iptpjavadev.service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iptp.iptpjavadev.model.Interval;

@Service
public class IptpjavadevService {

	private Date[] dateFromTo = new Date[2];

	@Autowired
	public IptpjavadevService() {
	}

	public List<String[]> getEmpIntervals(MultipartFile file) {
		List<String[]> result = new ArrayList<String[]>();
		String[] msg = new String[2];
		try {
			
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			if (file.isEmpty()) {
				msg[0] = "Empty file " + filename;
				msg[1] = "";
				result.add(msg);
			}
			List<Interval> intervals = getIntervals(file);
			if (intervals != null && intervals.size() > 0) {
				sortList(intervals);
				List<String[]> empInterval = getEmptyIntervals(intervals);
				if (empInterval != null && empInterval.size() > 0) {
					String[] rlt = new String[empInterval.size()];
					msg[0] = dateFromTo[0].toString();
					msg[1] = dateFromTo[1].toString();
					String[] tmp;
					for (int i= 0; i< empInterval.size(); i++) {
						tmp = empInterval.get(i);
						rlt[i] = "From " + tmp[0] + " To " + tmp[1];
					}
					result.add(msg);
					result.add(rlt);
				} else {
					msg[0] = "No Empty Interval";
					msg[1] = "";
					result.add(msg);
				}

			} else {
				msg[0] = "No Empty Interval";
				msg[1] = "";
				result.add(msg);
			}
		} catch (Exception e) {
			msg[0] = "No Empty Interval";
			msg[1] = "";
			result.add(msg);
		}

		return result;
	}

	public List<String[]> getEmptyIntervals(List<Interval> list) {
		try {
			List<String[]> result = new ArrayList<String[]>();
			Date[] dateTime1 = list.get(0).getDates();
			String[] sDateTime = new String[2];
			Date[] dateTime2;
			sDateTime[0] = dateTime1[1].toString();
			for (int i = 1; i < list.size(); i++) {
				dateTime2 = list.get(i).getDates();
				sDateTime[1] = dateTime2[0].toString();
				result.add(sDateTime);
				sDateTime = new String[2];
				sDateTime[0] = dateTime2[1].toString();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void sortList(List<Interval> list) {
		Collections.sort(list);
		Interval firstIntv = list.get(0);
		Interval intv;
		int i = 1;
		while(i<list.size()){
			intv = list.get(i);
			if (firstIntv.getStart().equals(intv.getStart()) || firstIntv.getEnd().equals(intv.getEnd()) || intv.getStart().before(firstIntv.getEnd()) || intv.getStart().equals(firstIntv.getEnd())) {
				if(firstIntv.getEnd().before(intv.getEnd())) {
				firstIntv.setEnd(intv.getEnd());
				list.remove(i-1);
				list.add(i-1,firstIntv);
				list.remove(i);
				}
				else {
					list.remove(i);
				}
			}
			else {
				i++;
				firstIntv = intv;
			}
		}
	}

	public List<Interval> getIntervals(MultipartFile file) {
		try {
			InputStream inputStream = file.getInputStream();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);
			if (doc.getDocumentElement().getNodeName().equals("response")) {
				NodeList nList = doc.getElementsByTagName("bar");
				Node nNode;
				Element eElement;
				List<Interval> result = new ArrayList<Interval>();
				Date[] dateTime;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (int i = 0; i < nList.getLength(); i++) {
					nNode = nList.item(i);
					eElement = (Element) nNode;
					dateTime = new Date[2];
					dateTime[0] = sdf.parse(eElement.getAttribute("startdate"));
					dateTime[1] = sdf.parse(eElement.getAttribute("enddate"));
					if(!dateTime[0].after(dateTime[1])) {
						result.add(new Interval(dateTime[0], dateTime[1]));
						if (i == 0) {
							dateFromTo[0] = dateTime[0];
							dateFromTo[1] = dateTime[1];
						} else {
							if (dateFromTo[0].after(dateTime[0])) {
								dateFromTo[0] = dateTime[0];
							}
							if (dateFromTo[1].before(dateTime[1])) {
								dateFromTo[1] = dateTime[1];
							}
						}
					}
				}
				return result;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}