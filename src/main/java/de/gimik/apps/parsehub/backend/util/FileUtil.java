package de.gimik.apps.parsehub.backend.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

//import net.sf.jasperreports.crosstabs.JRCrosstab;
//import net.sf.jasperreports.engine.JRBreak;
//import net.sf.jasperreports.engine.JRChart;
//import net.sf.jasperreports.engine.JRComponentElement;
//import net.sf.jasperreports.engine.JRElementGroup;
//import net.sf.jasperreports.engine.JREllipse;
//import net.sf.jasperreports.engine.JREmptyDataSource;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRFrame;
//import net.sf.jasperreports.engine.JRGenericElement;
//import net.sf.jasperreports.engine.JRImage;
//import net.sf.jasperreports.engine.JRLine;
//import net.sf.jasperreports.engine.JRRectangle;
//import net.sf.jasperreports.engine.JRStaticText;
//import net.sf.jasperreports.engine.JRSubreport;
//import net.sf.jasperreports.engine.JRTextField;
//import net.sf.jasperreports.engine.JRVisitor;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.design.JasperDesign;
//import net.sf.jasperreports.engine.util.JRElementsVisitor;
//import net.sf.jasperreports.engine.util.JRLoader;
//import net.sf.jasperreports.engine.util.JRSaver;
//import net.sf.jasperreports.engine.xml.JRXmlLoader;
import ru.yandex.qatools.ashot.Screenshot;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.xalan.xsltc.compiler.sym;
//import org.castor.core.util.Base64Encoder;
//import org.olap4j.impl.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.sun.jersey.core.header.FormDataContentDisposition;

import de.gimik.apps.parsehub.backend.util.Constants;
import de.gimik.apps.parsehub.backend.util.FileUtil;
import de.gimik.apps.parsehub.backend.BackendException;
import de.gimik.apps.parsehub.backend.web.viewmodel.FileUploadInfo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class FileUtil {
//    public static JasperPrint getJasperPrint(String jasperFile, List<?> list,
//                                             Map<String, Object> parameters) {
//        JasperPrint jasperPrint = null;
//        try {
//            File file = new File(jasperFile);
//            String reportPath = file.getParent();
//            String fileName = FilenameUtils.getBaseName(file.getAbsolutePath());
//            File compiledJasperFile = new File(reportPath, fileName + ".jasper");
//            JasperReport jasperReport = null;
//            if (compiledJasperFile.exists()) {
//                jasperReport = (JasperReport) JRLoader
//                        .loadObject(compiledJasperFile);
//            } else {
//                jasperReport = compileReport(reportPath, fileName);
//            }
//
//            if (list == null || list.size() == 0) {
//                jasperPrint = JasperFillManager.fillReport(jasperReport,
//                        parameters, new JREmptyDataSource());
//            } else {
//                JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(
//                        list);
//                jasperPrint = JasperFillManager.fillReport(jasperReport,
//                        parameters, beanDataSource);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (JRException e) {
//            e.printStackTrace();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return jasperPrint;
//    }
//
//    public static JasperReport compileReport(final String reportsPath,
//                                             String reportName) throws Throwable {
//        JasperDesign jasperDesign = JRXmlLoader.load(new File(reportsPath,
//                reportName + ".jrxml"));
//        JasperReport jasperReport = JasperCompileManager
//                .compileReport(jasperDesign);
//        JRSaver.saveObject(jasperReport, new File(reportsPath, reportName
//                + ".jasper"));
//        // toLog("Saving compiled report to: " + reportsPath + reportName +
//        // ".jasper");
//        // Compile sub reports
//        JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
//            @Override
//            public void visitBreak(JRBreak breakElement) {
//            }
//
//            @Override
//            public void visitChart(JRChart chart) {
//            }
//
//            @Override
//            public void visitCrosstab(JRCrosstab crosstab) {
//            }
//
//            @Override
//            public void visitElementGroup(JRElementGroup elementGroup) {
//            }
//
//            @Override
//            public void visitEllipse(JREllipse ellipse) {
//            }
//
//            @Override
//            public void visitFrame(JRFrame frame) {
//            }
//
//            @Override
//            public void visitImage(JRImage image) {
//            }
//
//            @Override
//            public void visitLine(JRLine line) {
//            }
//
//            @Override
//            public void visitRectangle(JRRectangle rectangle) {
//            }
//
//            @Override
//            public void visitStaticText(JRStaticText staticText) {
//            }
//
//            @Override
//            public void visitSubreport(JRSubreport subreport) {
//                try {
//                    String expression = subreport.getExpression().getText()
//                            .replace(".jasper", "");
//                    StringTokenizer st = new StringTokenizer(expression, "\"/");
//                    String subReportName = null;
//                    while (st.hasMoreTokens())
//                        subReportName = st.nextToken();
//                    // Sometimes the same subreport can be used multiple times,
//                    // but
//                    // there is no need to compile multiple times
//                    // if (completedSubReports.contains(subReportName))
//                    // return;
//                    // completedSubReports.add(subReportName);
//                    compileReport(reportsPath, subReportName);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void visitTextField(JRTextField textField) {
//            }
//
//            @Override
//            public void visitComponentElement(
//                    JRComponentElement componentElement) {
//            }
//
//            @Override
//            public void visitGenericElement(JRGenericElement element) {
//            }
//        });
//        return jasperReport;
//    }

    public static final String appendFilename(String originalName, String preceeding) {
        return FilenameUtils.getBaseName(originalName) + "_" + preceeding + "." + FilenameUtils.getExtension(originalName);
    }

    public static final String setFilename(String originalName, String newName) {
    	if(originalName == null)
    		return newName + ".png";
    	String extension = FilenameUtils.getExtension(originalName) == null ? "png" :  FilenameUtils.getExtension(originalName);
        return newName + "." + extension;
    }

    public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation, long maxBytes) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int read = 0;
        byte[] bytes = new byte[1024];
        int byteCount = 0;

        while ((read = uploadedInputStream.read(bytes)) != -1) {
            byteCount += read;
            if (byteCount > maxBytes)
                break;
            out.write(bytes, 0, read);
        }
        out.flush();

        if (byteCount <= maxBytes) {
            File uploadedFile = new File(uploadedFileLocation);
            OutputStream fileOut = new FileOutputStream(uploadedFile);
            out.writeTo(fileOut);

            fileOut.flush();
            fileOut.close();
        }
        out.close();

        if (byteCount > maxBytes) {

            throw new MaxUploadSizeExceededException(maxBytes);
        }

    }
    public static FileUploadInfo uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String directoryFileUpload, String folderName) {
    	return uploadFile(uploadedInputStream, fileDetail, directoryFileUpload, folderName,"");
    }
    public static FileUploadInfo uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String directoryFileUpload, String folderName,String oldFileName) {
        FileUploadInfo fileUploadInfo = new FileUploadInfo();
        String fileName = UUID.randomUUID().toString();
        if(!StringUtils.isEmpty(oldFileName))
        	fileName = oldFileName;
        String extension = FilenameUtils.getExtension(fileName);
        String uploadFileName = "";
        if(!StringUtils.isEmpty(extension))
        	uploadFileName = oldFileName;
        else
        	uploadFileName = setFilename(fileDetail.getFileName(), fileName);
        try {
            File uploadFolder = new File(directoryFileUpload, folderName);
            File uploadFile = new File(uploadFolder, uploadFileName);
            FileUtils.forceMkdir(uploadFolder);
            FileUtil.writeToFile(uploadedInputStream, uploadFile.getAbsolutePath(), Constants.IMAGE_UPLOAD_FILE_MAX_SIZE);
        } catch (Exception e) {
            throw new BackendException(Constants.ErrorCode.IMAGE_UPLOAD_ERROR);
        }
        fileUploadInfo.setFileUrl("/" + folderName + "/" + uploadFileName);

        return fileUploadInfo;
    }

    public static void deleteFile(String fileDirecttody) {
        try {
            File file = new File(fileDirecttody);
            FileUtils.deleteQuietly(file);
        } catch (Exception e) {

        }
    }

    public static InputStream resizeImage(InputStream file,String fileType){
    	Integer max_size  = 1000;
    	try {
    		BufferedImage originalImage = ImageIO.read(file);
    		int width = originalImage.getWidth();
    		int height = originalImage.getHeight();
    		if (width > height) {
                if (width > max_size) {
                    height = (int) (height * (new Double(max_size) / new Double(width)));
                    width = max_size;
                }
            } else {
                if (height > max_size) {
                    width = (int) (width *(new Double(max_size) / new Double(height)));
                    height = max_size;
                }
            }
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizedImage = new BufferedImage(width, height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, width, height, null);
			g.dispose();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, fileType, os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			return is;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		return null;
    }
    
    public static FileUploadInfo uploadFileCaptureShot(Screenshot screenshot, String directoryFileUpload, String folderName) {
		return uploadFileScreenShot(screenshot, directoryFileUpload, folderName);
	}

	public static FileUploadInfo uploadFileScreenShot(Screenshot screenshot, String directoryFileUpload, String folderName) {
		
		FileUploadInfo fileUploadInfo = new FileUploadInfo();
		String fileName = UUID.randomUUID().toString();
		BufferedImage image =screenshot.getImage();
		String uploadFileName = setNewFilename(fileName);
		String extension = FilenameUtils.getExtension(uploadFileName);
		InputStream resizeImage = resizeCapture(image, extension);
		try {
			File uploadFolder = new File(directoryFileUpload, folderName);
			File uploadFile = new File(uploadFolder, uploadFileName);
			FileUtils.forceMkdir(uploadFolder);
			FileUtil.writeToFile(resizeImage, uploadFile.getAbsolutePath(),
					Constants.IMAGE_UPLOAD_FILE_MAX_SIZE);
		} catch (Exception e) {
			throw new BackendException(Constants.ErrorCode.IMAGE_UPLOAD_ERROR);
		}
		fileUploadInfo.setFileUrl("/" + folderName + "/" + uploadFileName);

		return fileUploadInfo;
	}
	public static final String setNewFilename(String newName) {
    		return newName + ".png";
    	
    }
	 public static InputStream resizeCapture(BufferedImage originalImage,String fileType){
	    	try {
	    		int width = originalImage.getWidth();
	    		int height = originalImage.getHeight();
	    		
				int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
				BufferedImage resizedImage = new BufferedImage(width, height, type);
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(originalImage, 0, 0, width, height, null);
				g.dispose();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(resizedImage, fileType, os);
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				return is;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
			return null;
	    }
    public static void main(String[] args) throws IOException {
//    	MappingLanguageInfo languageInfo = new MappingLanguageInfo();
//    	languageInfo.setBuilding("Building");
//    	languageInfo.setFloor("Floor");
//    	languageInfo.setOriginalState("Original State");
//    	languageInfo.setProjectNumber("Project Number");
//    	languageInfo.setProtectiveClass("Protective Class");
//    	languageInfo.setRoomInformation("Room Information");
//    	languageInfo.setRoomNumber("Room Number");
//    	languageInfo.setSpatialUse("Spatial Use");
//    	Room room= new Room();
//    	room.setId(1);
//    	room.setBuilding("Building");
//    	room.setFloor("Floor");
//    	room.setOriginalState(true);
//    	Project project = new Project();
//    	project.setProjectNumber("Project Number");
//    	room.setProject(project);
//    	room.setProtectiveClass("Protective Class");
//    	room.setRoomInformation("Room Information");
//    	room.setRoomNumber("Room Number");
//    	room.setSpatialUse("Spatial Use");
//    	FileUploadInfo fileUploadInfo = FileUtil.createQrcodeForRoom(languageInfo, room);
    	
    	String fullPrice = "52,56 € UVP: 72,78 €";
		Double avp= null;
		if(fullPrice.indexOf("VP:") > 0) {
			fullPrice = fullPrice.substring(fullPrice.indexOf("VP:")+3,fullPrice.length());
			if(fullPrice.indexOf("€") > 0) 
				fullPrice = fullPrice.substring(0,fullPrice.indexOf("€")-1);
			fullPrice = fullPrice.replace(",", ".").trim();
			try {
				avp = Double.parseDouble(fullPrice.trim());
			} catch (Exception e) {
				e.printStackTrace();
				avp = null;
			}
			
		}
		System.out.println(avp);
		System.out.println(fullPrice);
	}

}
