package com.appspot.foodsorce;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.appspot.foodsorce.shared.Vendor;

public class ImportParserTest {

	private ArrayList<Vendor> vendors;

	@Before
	public void setUp() throws Exception {
		VancouverDataImplTestable ds = new VancouverDataImplTestable();
		vendors = ds.importData();
	}

	@Test
	public void testFirst() {
		Vendor vendor = vendors.get(0);
		assertEquals(vendor.getName(), "Via Tevere Pizzeria");
		assertEquals(vendor.getDescription(), "Pizza");
		assertEquals(vendor.getLocation(),
				("C1 Authorised Parking Meter - West Side of 400 Burrard St"));
		assert (vendor.getLatitude() == 49.2869026429);
		assert (vendor.getLongitude() == -123.117533502);

	}

	@Test
	public void testAnother() {
		Vendor vendor = vendors.get(6);
		assertEquals(vendor.getName(), "Le Tigre");
		assertEquals(vendor.getDescription(), "Chinese Cuisine");
		assertEquals(
				vendor.getLocation(),
				("C17 Authorised Parking Meter - North Side of Alberni St - East of Bute St"));
		assert (vendor.getLatitude() == 49.2865915482);
		assert (vendor.getLongitude() == -123.125618909);
	}

	@Test
	public void testLast() {
		Vendor vendor = vendors.get(vendors.size() - 1);
		assertEquals(vendor.getName(), "Chou Chou Crepes");
		assertEquals(vendor.getDescription(), "French Crepes");
		assertEquals(vendor.getLocation(), ("East Side of 600 Hamilton St"));
		assert (vendor.getLatitude() == 49.2800668156);
		assert (vendor.getLongitude() == -123.113913619);
	}

	@Test
	public void testNoName() {
		Vendor vendor = vendors.get(15);
		assertEquals(vendor.getName(), "Hot Dogs");
		assertEquals(vendor.getDescription(), "Hot Dogs");
		assertEquals(
				vendor.getLocation(),
				("East Side of 600 Beatty St - 6 Metres North of West Georgia St"));
		assert (vendor.getLatitude() == 49.2787463289);
		assert (vendor.getLongitude() == -123.112040051);

	}

	@Test
	public void omitPendingVendors() {
		assert (!vendors.contains(new Vendor("SFV136", "Eat Sexy",
				"Healthy Fusion", "Westside 400 Cambie St", 49.2818452,
				-123.109719)));
	}

}
