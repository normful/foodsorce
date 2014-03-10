package com.appspot.foodsorce.server.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.appspot.foodsorce.client.admin.VancouverDataService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.server.VendorJDO;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VancouverDataServiceImpl extends RemoteServiceServlet
	implements VancouverDataService {
	
	private static final long serialVersionUID = 979083175319069108L;

	private String filepath = "excel/new_food_vendor_locations.xls";
	private ArrayList<VendorJDO> vendorsToStore;

	public VancouverDataServiceImpl() {
		vendorsToStore = new ArrayList<VendorJDO>();
	}

	@Override
	public void importData() {
		
		// Object toRead = null;
		//
		// try {
		// URL VanData = new
		// URL("ftp://webftp.vancouver.ca/new_food_vendor_locations.xls");
		// toRead = VanData.getContent();
		// } catch (MalformedURLException e) {
		// System.out.print("Problem with URL format");
		// e.printStackTrace();
		// } catch (IOException e) {
		// System.out.print("IOexception reading data file");
		// e.printStackTrace();
		// }

		try {
			InputStream input = new FileInputStream(filepath);
			Workbook wb = WorkbookFactory.create(input);
			Sheet sheet0 = wb.getSheetAt(0);
			sheet0.removeRow(sheet0.getRow(0));
			for (Row row : sheet0) {
				String key = "";
				String name = "";
				String description = "";
				String location = "";
				double latitude = 0;
				double longitude = 0;
				for (int i = 0; i < 8; i++) {
					Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
					if (i == 0 && cell != null)
						key = cell.getRichStringCellValue().getString();
					else if (i == 3 && cell != null)
						name = cell.getRichStringCellValue().getString();
					else if (i == 4 && cell != null)
						location = cell.getRichStringCellValue().getString();
					else if (i == 5 && cell != null)
						description = cell.getRichStringCellValue().getString();
					else if (i == 6 && cell != null)
						latitude = cell.getNumericCellValue();
					else if (i == 7 && cell != null)
						longitude = cell.getNumericCellValue();
				}
				if (row.getCell(2).getRichStringCellValue().getString().equals("open")) {
					VendorJDO vendor = new VendorJDO(name, description, location, latitude, longitude);
					System.out.println("VancouverDataServiceImpl.java: key = " + key);
					System.out.println("VancouverDataServiceImpl.java: creating Vendor = " + vendor.toString());
					
					// TODO: Change this following line to use actual 'key' column from .xls
					Key key = KeyFactory.createKey(VendorJDO.class.getSimpleName(), "key field from .xls");
					
					vendorsToStore.add(vendor);
				}
			}
		} catch (FileNotFoundException exception) {
			System.err.println("FileNotFoundException: new_food_vendor_locations.xls");
			System.exit(0);
		} catch (InvalidFormatException exception) {
			System.err.println("InvalidFormatException: new_food_vendor_locations.xls");
			System.exit(0);
		} catch (IOException exception) {
			System.err.println("IOException: new_food_vendor_locations.xls");
			System.exit(0);
		}
		
		// Batch store in JDO (more efficient than numerous single stores)
		// TODO: Uncomment this once you know that vendorsToStore is correct.
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			pm.makePersistentAll(vendorsToStore);
//		} finally {
//			pm.close();
//		}
		
	}

}
