package com.appspot.foodsorce;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.appspot.foodsorce.server.admin.VancouverDataServiceImpl;

public class ImportParserTest {

	@Before
	public void setUp() throws Exception {
		VancouverDataServiceImpl ds = new VancouverDataServiceImpl();
		ds.importData();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
