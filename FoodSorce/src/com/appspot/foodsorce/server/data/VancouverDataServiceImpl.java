package com.appspot.foodsorce.server.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//import javax.jdo.JDOHelper;
//import javax.jdo.PersistenceManager;
//import javax.jdo.PersistenceManagerFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class VancouverDataServiceImpl {
	private static VancouverDataServiceImpl uniqueInstance = null;
	private ArrayList<Vendor> Vendors;
//	private PersistenceManagerFactory Factory;
	
	private VancouverDataServiceImpl(){
		Vendors= new ArrayList<Vendor>();
//		Object toRead = null;
//		
//		try {
//			URL VanData = new URL("ftp://webftp.vancouver.ca/new_food_vendor_locations.xls");
//			 toRead = VanData.getContent();
//		} catch (MalformedURLException e) {
//			System.out.print("Problem with URL format");
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.out.print("IOexception reading data file");
//			e.printStackTrace();
//		}
		
		try{
		InputStream inp = new FileInputStream("new_food_vendor_locations.xls");
		Workbook wb = WorkbookFactory.create(inp);
		
		Sheet sheet0 = wb.getSheetAt(0);
		sheet0.removeRow(sheet0.getRow(0));
		for(Row row : sheet0){
			String name="";
			String description="";
			String location="";
			double latitude=0;
			double longitude=0;
			for(int i=0;i<8;i++){
				Cell cell;
				if(i==3){
					cell=row.getCell(i,Row.RETURN_BLANK_AS_NULL);
					if(!(cell==null))
						name=cell.getRichStringCellValue().getString();
				}
				if(i==4){
					cell=row.getCell(i,Row.RETURN_BLANK_AS_NULL);
					if(!(cell==null))
						location=cell.getRichStringCellValue().getString();
				}
				if(i==5){
					cell=row.getCell(i,Row.RETURN_BLANK_AS_NULL);
					if(!(cell==null))
						description=cell.getRichStringCellValue().getString();
				}
				if(i==6){
					cell=row.getCell(i,Row.RETURN_BLANK_AS_NULL);
					if(!(cell==null))
						latitude=cell.getNumericCellValue();
				}
				if(i==7){
					cell=row.getCell(i,Row.RETURN_BLANK_AS_NULL);
					if(!(cell==null))
						longitude=cell.getNumericCellValue();
				}
			}
			if(row.getCell(2).getRichStringCellValue().getString().equals("open"))
				Vendors.add(new Vendor(name,description,location,latitude,longitude));
		}

		
		}
		
		catch (FileNotFoundException exception){
			System.err.println("FileNotFoundException: new_food_vendor_locations.xls");
			System.exit(0);
		}
		catch (InvalidFormatException exception){
			System.err.println("InvalidFormatException: new_food_vendor_locations.xls");
			System.exit(0);
		}
		catch (IOException exception){
			System.err.println("IOException: new_food_vendor_locations.xls");
			System.exit(0);
		}
//		Factory = JDOHelper.getPersistenceManagerFactory("transactions-optional");
//		PersistenceManager pm = Factory.getPersistenceManager();
//		pm.makePersistent(Vendors);
//		pm.close();

	}
	public static VancouverDataServiceImpl getInstance(){
		if(uniqueInstance==null)
			uniqueInstance=new VancouverDataServiceImpl();
		return uniqueInstance;
	}
	
	public Vendor getVendor(int index){
		return Vendors.get(index);
	}
}
