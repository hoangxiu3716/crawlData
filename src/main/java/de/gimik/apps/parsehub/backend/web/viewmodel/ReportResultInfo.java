package de.gimik.apps.parsehub.backend.web.viewmodel;

import java.io.File;

public class ReportResultInfo {
	private String reportId;
	private String message;
	private File file;

	public ReportResultInfo() {
		super();
	}
	public ReportResultInfo(String reportId, String message, File file) {
		super();
		this.reportId = reportId;
		this.message = message;
		this.file = file;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

}
