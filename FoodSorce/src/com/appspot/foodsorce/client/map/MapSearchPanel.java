package com.appspot.foodsorce.client.map;

import java.util.ArrayList;
import java.util.List;

import com.appspot.foodsorce.shared.Vendor;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.geometry.Spherical;

public class MapSearchPanel extends FlowPanel {

	private static final MapSearchPanel INSTANCE = new MapSearchPanel();
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();
	
	private Button setAddressButton = new Button("Set Location");
	private TextBox addressField = new TextBox();
	
	private Label radioLabel = new Label("Distance:");
	private RadioButton optionAll = new RadioButton("radioGroup", "all");
	private RadioButton option2 = new RadioButton("radioGroup", "2km");
	private RadioButton option5 = new RadioButton("radioGroup", "5km");
	private RadioButton option10 = new RadioButton("radioGroup", "10km");
	private List<RadioButton> buttons = new ArrayList<RadioButton>();
	
	private SimplePanel mapPanel;
	private GoogleMap map;
	private MapOptions mapOptions;
	
	private Marker userMarker;
	private MarkerOptions userOptions;
	private MarkerImage userMarkerImage = MarkerImage.create("https://maps.gstatic.com/mapfiles/ridefinder-images/mm_20_red.png");
	
	private List<Marker> vendorMarkers;
	private MarkerImage vendorMarkerImage = MarkerImage.create("https://maps.gstatic.com/mapfiles/ridefinder-images/mm_20_blue.png");
	
	private Geolocation geolocation;
	private LatLng userLocation = LatLng.create(49.279641,-123.125625);
	
	private List<Vendor> allVendors;
	private List<Vendor> matchingVendors;
	
	private MapSearchPanel() {
		System.out.println("MapSearchPanel.java: MapSearchPanel() constructor");
		allVendors = new ArrayList<Vendor>();
		matchingVendors = new ArrayList<Vendor>();
		vendorMarkers = new ArrayList<Marker>();
		// Notes to Brandon:
		//
		// Please leave the "For debugging" System.out.println statements in, they're helpful for understanding control flow
		//
		// Assume that "vendors" will contain a List of all vendors. 
		// I (Norman) will implement soon a way to keep 1 single consistent 
		// client-side List of vendors throughout the whole app (probably using 
		// the Singleton and Observer patterns).
		//
		// But until I add that, maybe you'll find it helpful to use the 
		// following dummy list of vendors
		// 
		// (these locations were generated with this site,
		// which you might find useful):
		// http://universimmedia.pagesperso-orange.fr/geo/loc.htm
//		Vendor dummyVendor1 = new Vendor("dummyKey", "Burger Truck", "West End", "American", 49.28525, -123.13530);
//		Vendor dummyVendor2 = new Vendor("dummyKey", "Pizza Stand", "BC Place", "Italian", 49.27657, -123.11041);
//		Vendor dummyVendor3 = new Vendor("dummyKey", "Sushi Shop", "Granville Island", "Japanese", 49.27069, -123.13384);
//		Vendor dummyVendor4 = new Vendor("dummyKey", "Tim Hortons", "YVR", "Coffee", 49.19594, -123.17757);
//		Vendor dummyVendor5 = new Vendor("dummyKey", "Taco Shop", "Cambie St. & 41st Ave.", "Mexican", 49.23407, -123.11560);
//		allVendors.add(dummyVendor1);
//		allVendors.add(dummyVendor2);
//		allVendors.add(dummyVendor3);
//		allVendors.add(dummyVendor4);
//		allVendors.add(dummyVendor5);
		
		createAddressTextBox();
		createRadioButtons();
		createMap();
		
		geolocation = Geolocation.getIfSupported();
		if (geolocation != null)
			setLocationFromBrowser(geolocation);
		
		plotMatchingVendorMarkers();
	}
	
	public static MapSearchPanel getInstance() {
		System.out.println("MapSearchPanel.java: getInstance");
		return INSTANCE;
	}
	
	public void setAllVendors(List<Vendor> allVendors) {
		System.out.println("MapSearchPanel.java: setAllVendors");
		this.allVendors.clear();
		this.allVendors.addAll(allVendors);
	}
	
	private void createAddressTextBox() {
		addressField.setText("Address");
		addressField.setWidth("350px");
		
		setAddressButton.addStyleName("setAddressButton");
		setAddressButton.setWidth("140px");
		setAddressButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String addressInput = addressField.getText();
				setLocationFromInput(addressInput);
				addressField.setFocus(true);
			}
		});
		
		addressField.setFocus(true);
		this.add(addressField);
		this.add(setAddressButton);
	}
		
	private void createRadioButtons() {
		buttons.add(optionAll);
		buttons.add(option2);
		buttons.add(option5);
		buttons.add(option10);

		for (final RadioButton button : buttons) {
			button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					updateMatchingVendors(button.getText());
					try {
						System.out.println("MapSearchPanel.java: ValueChangeHandler onValueChange");
						vendorListPanel = VendorListPanel.getInstance();
						vendorListPanel.setAndDisplayMatchingVendors(matchingVendors);
					} catch (Throwable e) {
						e.printStackTrace();
					}
					plotMatchingVendorMarkers();
				}
			});
		}
		
		optionAll.setValue(true);
		
		this.add(radioLabel);
		this.add(optionAll);
		this.add(option2);
		this.add(option5);
		this.add(option10);
	}
	
	private void createMap() {
		mapPanel = new SimplePanel();
		mapPanel.setSize("500px", "500px");
		
		mapOptions = MapOptions.create();
		mapOptions.setCenter(LatLng.create(49.279641,-123.125625));
		mapOptions.setZoom(14);
		mapOptions.setMapTypeId(MapTypeId.ROADMAP);
		mapOptions.setDraggable(true);
		mapOptions.setMapTypeControl(true);
		mapOptions.setScaleControl(true);
		mapOptions.setScrollwheel(true);
		map = GoogleMap.create(mapPanel.getElement(), mapOptions);

		userOptions = MarkerOptions.create();
		userOptions.setPosition(userLocation);
		userOptions.setMap(map);
		
		userMarker = Marker.create(userOptions);
		userMarker.setIcon(userMarkerImage);
		
		this.add(mapPanel);
	}
	
	private void setLocationFromBrowser(Geolocation geolocation) {
		
		// For debugging
		if (geolocation != null)
			System.out.println("MapSearchPanel.java: setLocationFromBrowser(geolocation=" + geolocation.toString() + ")");
		
		geolocation.getCurrentPosition(new Callback<Position, PositionError>() {
			
			@Override
			public void onSuccess(Position result) {
				Coordinates coordinates = result.getCoordinates();
				LatLng latlong = LatLng.create(coordinates.getLatitude(), coordinates.getLongitude());
				plotUser(latlong);
			}
			
			@Override
			public void onFailure(PositionError reason) {
				// For debugging
				System.out.println("MapSearchPanel.java: setLocationFromBrowser geo.getCurrentPosition Callback onFailure");
			}
		});
	}
	
	private void plotUser(LatLng location) {
		// For debugging
		if (location != null)
			System.out.println("MapSearchPanel.java: plotUser(location=" + location.toString() + ")");
		
		userLocation = location;
		userMarker.setPosition(location);
		map.setCenter(location);
	}
	
	// TODO: Brandon needs to fix this
	private void setLocationFromInput(String address) {
		
		// For debugging
		if (address != null)
			System.out.println("MapSearchPanel.java: setLocationFromInput(address=" + address + ")");
		
		Geocoder geocoder = Geocoder.create();
		GeocoderRequest georequest = GeocoderRequest.create();
		georequest.setAddress(address);
		
		geocoder.geocode(georequest, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				if (b == GeocoderStatus.OK) {
					
					GeocoderResult result = a.shift();
					
					// For debugging
					System.out.println("MapSearchPanel.java: setLocationFromInput GeocoderResult=" + result.toString());
					
					if (isInVancouver(result.getGeometry().getLocation()) == true) {
						plotUser(result.getGeometry().getLocation());
					} else {
						Window.alert("Please re-enter an address within Vancouver.");
					}
				} else {
					Window.alert("Google Maps could not return address coordinates.");
				}
			}
		});
		
	}
	
	private boolean isInVancouver(LatLng location) {
		double lat = location.lat();
		double lng = location.lng();
		return (49.200589 < lat && lat < 49.309591 && -123.259243 < lng && lng < -123.064235);
	}

	private void plotMatchingVendorMarkers() {
		// For debugging
		System.out.println("MapSearchPanel.java: plotVendorMarkers()");
		
		// Clear all existing vendor markers from map
		GoogleMap nullMap = null;
		for (Marker vendorMarker: vendorMarkers)
			vendorMarker.setMap(nullMap);
		
		// Plot matching vendors onto map
		for (Vendor vendor : matchingVendors) {
			MarkerOptions options = MarkerOptions.create();
			options.setPosition(LatLng.create(vendor.getLatitude(),vendor.getLongitude()));
			Marker vendorMarker = Marker.create(options);
			vendorMarker.setIcon(vendorMarkerImage);
			vendorMarkers.add(vendorMarker);
			vendorMarker.setMap(map);
		}
	}

	private void updateMatchingVendors(String buttonText) {
		// For debugging
		System.out.println("MapSearchPanel.java: filterVendors(buttonText=" + buttonText + ")");
		
		double travelDistance;
		
		if (buttonText.equals("2km"))
			travelDistance = 2000.0;
		else if (buttonText.equals("5km"))
			travelDistance = 5000.0;
		else if (buttonText.equals("10km"))
			travelDistance = 10000.0;
		else
			travelDistance = 999999.0;
		
		matchingVendors.clear();
		
		System.out.println("MapSearchPanel.java: travelDistance=" + travelDistance);
		
		// Radio button "all" is selected
		if (travelDistance == 999999.0) {
			matchingVendors.addAll(allVendors);
			return;
		}
		
		// Radio button "2km", "5km", or "10km" is selected
		for (Vendor vendor: allVendors) {
			System.out.println("MapSearchPanel.java: matchingVendors.add(" + vendor.toString() +")");
			LatLng vendorLocation = LatLng.create(vendor.getLatitude(), vendor.getLongitude());
			double vendorDistance = Spherical.computeDistanceBetween(userLocation, vendorLocation);
			System.out.println("MapSearchPanel.java: vendorDistance=" + String.valueOf(vendorDistance));
			if (vendorDistance <= travelDistance)
				matchingVendors.add(vendor);
		}
	}
	
	@SuppressWarnings("unused")
	private void convertGPStoAddress() {
		// TODO: Brandon needs to do this
	}

}