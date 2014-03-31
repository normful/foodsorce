package com.appspot.foodsorce.server.admin;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.appspot.foodsorce.client.admin.VancouverDataService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VancouverDataServiceImpl extends RemoteServiceServlet implements
	VancouverDataService {

	private static final long serialVersionUID = -2926288307530133636L;

	private URL dataUrl;
	private InputStream inputStream;
	private Workbook workbook;
	private Sheet sheet;
	private ArrayList<Vendor> vendorsToStore;

	public VancouverDataServiceImpl() {
		vendorsToStore = new ArrayList<Vendor>();
		try {
			dataUrl = new URL("http://www.ugrad.cs.ubc.ca/~h0e9/new_food_vendor_locations.xls");
			inputStream = dataUrl.openStream();
			workbook = WorkbookFactory.create(inputStream);
			sheet = workbook.getSheetAt(0);
			sheet.removeRow(sheet.getRow(0));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void importData() {
		for (Row row : sheet) {
			String excelKey = "";
			String name = "";
			String description = "";
			String location = "";
			double latitude = 0;
			double longitude = 0;
			for (int i = 0; i < 8; i++) {
				Cell cell = row.getCell(i, Row.RETURN_BLANK_AS_NULL);
				if (i == 0 && cell != null)
					excelKey = cell.getRichStringCellValue().getString();
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
			if (row.getCell(2).getRichStringCellValue().getString()
					.equals("open")) {
				Vendor vendor = new Vendor(excelKey, name, description,
						location, latitude, longitude);
				vendorsToStore.add(vendor);
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistentAll(vendorsToStore);
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				pm.close();
			}
		}
	}
}