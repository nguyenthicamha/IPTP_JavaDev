package com.iptp.iptpjavadev.model;

import java.util.Date;

public class Interval implements Comparable<Interval> {
	private Date start;
	private Date end;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Interval(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public Date[] getDates() {
		Date[] dates = new Date[2];
		dates[0] = this.start;
		dates[1] = this.end;
		return dates;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public int compareTo(Interval interval) {
		if (getStart() == null || interval.getStart() == null) {
			return 0;
		}
		return getStart().compareTo(interval.getStart());
	}
}
