package com.appspot.foodsorce.client.map;

import java.util.ArrayList;
import java.util.List;

import com.appspot.foodsorce.shared.Vendor;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	
	private TextBox addressField = new TextBox();
	private Button setAddressButton = new Button("Set Location");
	
	private HorizontalPanel distancePanel = new HorizontalPanel();
	private Label radioLabel = new Label("Distance:");
	private RadioButton optionAll = new RadioButton("radioGroup", "all");
	private RadioButton option1 = new RadioButton("radioGroup", "1km");
	private RadioButton option2 = new RadioButton("radioGroup", "2km");
	private RadioButton option5 = new RadioButton("radioGroup", "5km");
	private RadioButton option10 = new RadioButton("radioGroup", "10km");
	private List<RadioButton> buttons = new ArrayList<RadioButton>();
	private String radioButtonSelected = "all";
	
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
	
	//Added GPS field to save from the method convertGPStoAddress.
	@SuppressWarnings("unused")
	private String coordinateConversionResults;
	
	private MapSearchPanel() {
		System.out.println("MapSearchPanel.java: MapSearchPanel() constructor");
		
		allVendors = new ArrayList<Vendor>();
		matchingVendors = new ArrayList<Vendor>();
		vendorMarkers = new ArrayList<Marker>();
		
		//GPS test
//		LatLng test = LatLng.create(49.2328357, -123.05465859999998);
//		convertGPStoAddress(test);
		
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
		this.matchingVendors.addAll(allVendors);
		this.plotMatchingVendorMarkers();
	}
	
	private void createAddressTextBox() {
		addressField.setText("Address");
		addressField.setWidth("350px");
		addressField.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					String addressInput = addressField.getText();
					setLocationFromInput(addressInput);
					addressField.setFocus(true);
				}
			}});
		
		addressField.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addressField.selectAll();
			}});
		
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
		buttons.add(option1);
		buttons.add(option2);
		buttons.add(option5);
		buttons.add(option10);

		for (final RadioButton button : buttons) {
			button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					radioButtonSelected = button.getText();
					updateAndPlotMatchingVendors();
				}
			});
		}
		
		optionAll.setValue(true);
		
		distancePanel.add(radioLabel);
		distancePanel.add(optionAll);
		distancePanel.add(option1);
		distancePanel.add(option2);
		distancePanel.add(option5);
		distancePanel.add(option10);
		
		this.add(distancePanel);
	}
	
	private void updateAndPlotMatchingVendors() {
		updateMatchingVendors(radioButtonSelected);
		try {
			System.out.println("MapSearchPanel.java: ValueChangeHandler onValueChange");
			vendorListPanel = VendorListPanel.getInstance();
			vendorListPanel.setAndDisplayMatchingVendors(matchingVendors);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		plotMatchingVendorMarkers();
	
	}
	
	private void createMap() {
		mapPanel = new SimplePanel();
		mapPanel.setSize("500px", "600px");
		
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
				updateAndPlotMatchingVendors();
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
//		if (location != null)
//			System.out.println("MapSearchPanel.java: plotUser(location=" + location.toString() + ")");
		
		userLocation = location;
		userMarker.setPosition(location);
		map.setCenter(location);
	}
	
	
	private void setLocationFromInput(String address) {
		
		// For debugging
//		if (address != null && !address.isEmpty())
//			System.out.println("MapSearchPanel.java: setLocationFromInput(address=" + address + ")");
		
		Geocoder geocoder = Geocoder.create();
		GeocoderRequest georequest = GeocoderRequest.create();
		georequest.setAddress(address);
		
		geocoder.geocode(georequest, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				if (b == GeocoderStatus.OK) {
					
					GeocoderResult result = a.shift();
					
					// For debugging
					System.out.println("MapSearchPanel.java: setLocationFromInput result.getFormattedAddres()=" + result.getFormattedAddress());
					System.out.println("MapSearchPanel.java: setLocationFromInput result.getGeometry().getLocation()=" + result.getGeometry().getLocation());
					
					if (isInVancouver(result.getGeometry().getLocation()) == true) {
						plotUser(result.getGeometry().getLocation());
						updateAndPlotMatchingVendors();
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
		System.out.println("MapSearchPanel.java: isInVancouver latitude:" + lat);
		double lng = location.lng();
		System.out.println("MapSearchPanel.java: isInVancouver longitude:" + lng);
		return (49.200589 < lat && lat < 49.309591 && -123.259243 < lng && lng < -123.017235);
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
		if (buttonText.equals("1km"))
			travelDistance = 1000.0;
		else if (buttonText.equals("2km"))
			travelDistance = 2000.0;
		else if (buttonText.equals("5km"))
			travelDistance = 5000.0;
		else if (buttonText.equals("10km"))
			travelDistance = 10000.0;
		else //  buttonText.equals("all")
			travelDistance = 999999.0;
		
		matchingVendors.clear();
		
		System.out.println("MapSearchPanel.java: travelDistance=" + travelDistance);
		
		// Radio button "all" is selected
		if (travelDistance == 999999.0) {
			matchingVendors.addAll(allVendors);
			return;
		}
		
		// Radio button "1km", "2km", "5km", or "10km" is selected
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
	private void convertGPStoAddress(LatLng location) {
		Geocoder geocoder = Geocoder.create();
		GeocoderRequest georequest = GeocoderRequest.create();
		georequest.setLocation(location);
		geocoder.geocode(georequest, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				if (b == GeocoderStatus.OK) {
					
					GeocoderResult result = a.shift();
					
					// For debugging
					System.out.println("MapSearchPanel.java: convertGPStoAddress GeocoderResult=" + result.getFormattedAddress());
					System.out.println("MapSearchPanel.java: convertGPStoAddress GPS GeocoderResult=" + result.getGeometry().getLocation());
					
					coordinateConversionResults = result.getFormattedAddress();

				} else {
					Window.alert("Google Maps could not return address from coordinates.");
					coordinateConversionResults = "N/A";
				}
			}
		});
	}

}