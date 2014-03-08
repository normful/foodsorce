package com.appspot.foodsorce.client.map;

import java.util.ArrayList;
import java.util.List;

import com.appspot.foodsorce.client.vendor.VendorInfo;
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
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.geometry.Spherical;

public class MapSearchPanel extends FlowPanel {

	private Button setAddressButton;
	private TextBox addressField;
	
	private Label radioLabel;
	private RadioButton optionAll;
	private RadioButton option2;
	private RadioButton option5;
	private RadioButton option10;
	private List<RadioButton> buttons;
	
	private SimplePanel mapPanel;
	private GoogleMap map;
	private MapOptions mapOptions;
	
	private Marker user;
	private MarkerOptions userOptions;
	
	private Geolocation geolocation;
	private LatLng userLocation;
	
	private List<VendorInfo> vendors;
	private List<VendorInfo> filteredVendors;
	private List<Marker> vendorMarkers;
	
	public MapSearchPanel() {
		createAddressTextBox();
		createRadioButtons();
		createMap();
		geolocation = Geolocation.getIfSupported();
		if (geolocation != null)
			setLocationFromBrowser(geolocation);
	}
	
	private void createAddressTextBox() {
		addressField = new TextBox();
		addressField.setText("Address");
		
		setAddressButton = new Button("Set Location");
		setAddressButton.addStyleName("setAddressButton");
		setAddressButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String addressinput = addressField.getText();
				System.out.println(addressinput);
				setLocationFromInput(addressinput);
				addressField.setFocus(true);
			}
		});
		
		this.add(addressField);
		this.add(setAddressButton);
	}
		
	private void createRadioButtons() {
		radioLabel = new Label("Distance:");
		
		optionAll = new RadioButton("distanceOption", "all");
		option2 = new RadioButton("distanceOption", "2km");
		option5 = new RadioButton("distanceOption", "5km");
		option10 = new RadioButton("distanceOption", "10km");
		
		buttons = new ArrayList<RadioButton>();
		buttons.add(optionAll);
		buttons.add(option2);
		buttons.add(option5);
		buttons.add(option10);

		for (final RadioButton button : buttons) {
			button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					changeDistanceVendor(button.getName());
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
		userOptions.setPosition(LatLng.create(49.279641,-123.125625));
		userOptions.setMap(map);
		user = Marker.create(userOptions);
		
		this.add(mapPanel);
	}
	
	private void setLocationFromBrowser(Geolocation geo) {
		geo.getCurrentPosition(new Callback<Position, PositionError>() {
			@Override
			public void onSuccess(Position result) {
				Coordinates coordinates = result.getCoordinates();
				LatLng latlong = LatLng.create(coordinates.getLatitude(), coordinates.getLongitude());
				setCurrentLocation(latlong);
			}
			@Override
			public void onFailure(PositionError reason) {
				System.out.println("Could not acquire data from browser");
			}
		});
	}
	
	private void setCurrentLocation(LatLng location) {
		userLocation = location;
		user.setPosition(location);
		map.setCenter(location);
	}
	
	// TODO: Need to fix this
	private void setLocationFromInput(String address) {
		GeocoderRequest georequest = GeocoderRequest.create();
		georequest.setAddress(address);
		Geocoder geocoder = Geocoder.create();
		
		geocoder.geocode(georequest, new Geocoder.Callback() {
			@Override
			public void handle(JsArray<GeocoderResult> a, GeocoderStatus b) {
				if (b == GeocoderStatus.OK) {
					GeocoderResult result = a.shift();
					if (checkAddressVancouver(result.getGeometry().getLocation()) == true) {
						setCurrentLocation(result.getGeometry().getLocation());
					} else {
						Window.alert("Address not within Vancouver. Please enter an address from Vancouver");
					}
				} else {
					Window.alert("Google Maps could not return address coordinates");
				}
			}
		});
		
	}
	
	private boolean checkAddressVancouver(LatLng location) {
		double lat = location.lat();
		double lng = location.lng();
		return (49.200589 < lat && lat < 49.309591 && -123.259243 < lng && lng < -123.064235);
	}
	
	private void changeDistanceVendor(String selection) {
		System.out.println("changeDistanceVendor called with selection = " + selection);
		filteredVendors.clear();
		double distance = 0;
		if (selection == "2km")
			distance = 2;
		if (selection == "5km")
			distance = 5;
		if (selection == "10km")
			distance = 10;
		filterVendor(distance);
		setFilteredMarkers();
//		setVendorDisplay();
	}

	private void updateVendorDisplay() {
		// TODO Auto-generated method stub

	}

	private void setFilteredMarkers() {
		GoogleMap x = null;
		for (Marker marker: vendorMarkers)
			marker.setMap(x);

		for (VendorInfo vendor: filteredVendors) {
			MarkerOptions options = MarkerOptions.create();
			options.setPosition(LatLng.create( vendor.getLatitude(),vendor.getLongitude()));
			Marker marker = Marker.create(options);
			vendorMarkers.add(marker);
			marker.setMap(map);
		}

	}


	private void filterVendor(double dist1) {
		if (dist1==0) {
			filteredVendors = vendors;
			return;
		}

		for (VendorInfo vendor: vendors) {
			LatLng latlong = LatLng.create(vendor.getLatitude(), vendor.getLongitude());
			double dist2 = Spherical.computeDistanceBetween(userLocation, latlong);
			if (dist1 <= dist2)
				filteredVendors.add(vendor);
		}

	}
	
	private void convertGPStoAddress() {
		// TODO: do this
	}
	
}