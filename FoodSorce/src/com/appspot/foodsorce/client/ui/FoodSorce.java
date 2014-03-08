package com.appspot.foodsorce.client.ui;

import java.util.ArrayList;
import java.util.List;





import com.appspot.foodsorce.client.vendor.VendorInfo;
import com.appspot.foodsorce.client.vendor.VendorService;
import com.appspot.foodsorce.client.vendor.VendorServiceAsync;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.TravelMode;
import com.google.maps.gwt.client.UnitSystem;
import com.google.maps.gwt.client.geometry.Spherical;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodSorce implements EntryPoint {

	
	private MarkerOptions moption;
	private Marker currentLocationMarker;
	private List<Marker> vendorMarkers;
	
	private GoogleMap map;
	
	private Button setAddressButton = new Button("Set");
	private TextBox addressField = new TextBox();
	
	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	private LayoutPanel mapPanel = new LayoutPanel();
	private HorizontalPanel addressPanel = new HorizontalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel vendorPanel = new VerticalPanel();
	private FlowPanel radioPanel = new FlowPanel();
	private List<RadioButton> listOfRadioButtons;
	 
	private final VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private ArrayList<VendorInfo> vendors = new ArrayList<VendorInfo>();
	private ArrayList<VendorInfo> filteredVendors = vendors;
	
	private LatLng currentLocation;
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Create textbox and button for address input
		createAddressInput();
		// Create options for vendor distance
		createRadioPanel();
		// Create Map
		createMap();
		
		// Resize
		mainPanel.setSize("50%", "50%");
		RootLayoutPanel.get().add(mainPanel);
		
		// Checks Browser for location
		checkLocationfromBrowser();
		
		
	}
	
	private void createMap() {
		MapOptions options  = MapOptions.create() ;
	    options.setCenter(LatLng.create( 49.279641,-123.125625 ));   
	    options.setZoom( 14 ) ;
	    options.setMapTypeId( MapTypeId.ROADMAP );
	    options.setDraggable(true);
	    options.setMapTypeControl(true);
	    options.setScaleControl(true) ;
	    options.setScrollwheel(true) ;


	    map = GoogleMap.create( mapPanel.getElement(), options );
	    
	    moption = MarkerOptions.create();
	    moption.setPosition(LatLng.create( 49.279641,-123.125625 ));
	    moption.setMap(map); 
//	    mapPanel.setSize("10%", "10%");
	    
	    mainPanel.add(mapPanel);
	    
	    
	    currentLocationMarker = Marker.create(moption);
	}
	
	// If possible attempts to grab location from browser
	private void checkLocationfromBrowser() {
		Geolocation geolocation = Geolocation.getIfSupported();
		if (geolocation != null)
			setLocationFromBrowser(geolocation);	
	}

	// Creates Radio Panel and sets all Radio Buttons with handlers
	private void createRadioPanel() {
		RadioButton optionAll = new RadioButton("distanceOption", "all");
	    RadioButton option2 = new RadioButton("distanceOption", "2km");
	    RadioButton option5 = new RadioButton("distanceOption", "5km");
	    RadioButton option10 = new RadioButton("distanceOption", "10km");
	    Label radioLabel = new Label("Distance to show");
	    
	    listOfRadioButtons = new ArrayList<RadioButton>();
	    
	    listOfRadioButtons.add(optionAll);
	    listOfRadioButtons.add(option2);
	    listOfRadioButtons.add(option5);
	    listOfRadioButtons.add(option10);
	    
	    for (final RadioButton button : listOfRadioButtons) {
	    	button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

	    		@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					changeDistanceVendor(button.getName());
					
				}

	    	});
	    }
	    
	    
	    optionAll.setValue(true);
	    
	    radioPanel.add(optionAll);
	    radioPanel.add(option2);
	    radioPanel.add(option5);
	    radioPanel.add(option10);
	    
	    rightPanel.add(radioLabel);
	    rightPanel.add(radioPanel);
		
	}

	private void changeDistanceVendor(String name) {
		filteredVendors.clear();
		double dist = 0;
		if (name == "2km") dist = 2;
		if (name == "5km") dist = 5;
		if (name == "10km") dist = 10;
		filterVendor(dist);
		setFilteredMarkers();
//		setVendorDisplay();
			
		
	}


	private void updateVendorDisplay() {
		// TODO Auto-generated method stub
		
	}


	private void setFilteredMarkers() {
		GoogleMap x = null;
		for (Marker marker: vendorMarkers) {
			marker.setMap(x);
		}
		
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
			double dist2 = Spherical.computeDistanceBetween(currentLocation, latlong);
			if (dist1 <= dist2) filteredVendors.add(vendor);
			
		}
		
	}




	private void createAddressInput() {
		// Set initial text to be address
		addressField.setText("Address");

		// Set sendButton
		setAddressButton.addStyleName("sendButton");
		
		// Add handler for setButton
		setAddressButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String addressinput = addressField.getText();
				System.out.println(addressinput);
				setLocationFromInput(addressinput);
				addressField.setFocus(true);
			}
		});
		

		addressPanel.add(addressField);
		addressPanel.add(setAddressButton);
		rightPanel.add(addressPanel);
		mainPanel.addEast(rightPanel, 300);
//		mainPanel.add(rightPanel);
	}
	


	
	//TODO: Need to fix this
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
	
	private void setCurrentLocation(LatLng latlong) {
		
		currentLocation = latlong;
		currentLocationMarker.setPosition(latlong);
	    map.setCenter(latlong);
		
	}
	
	private void setLocationFromBrowser(Geolocation geo) {

		  geo.getCurrentPosition(new Callback<Position, PositionError>()
		  {
		    
		   @Override
		   public void onSuccess(Position result)
		   {
			Coordinates coordinates = result.getCoordinates();
			LatLng latlong = LatLng.create(coordinates.getLatitude(), coordinates.getLongitude());
		    setCurrentLocation(latlong);
		   }
		    
		   @Override
		   public void onFailure(PositionError reason)
		   {
		    Window.alert("Could not acquire data from browser");
		   }
		  });
	}
	
	private boolean checkAddressVancouver(LatLng latlong) {
		double lat = latlong.lat();
		double lng = latlong.lng();	
		return (49.200589 < lat && lat < 49.309591 && -123.259243 < lng && lng < -123.064235);
	}
	
	private void convertGPStoAddress() {
		//do this
	}

	public void loadViewProfilePanel() {
		// TODO Auto-generated method stub
		
	}

	public void loadVendorListPanel() {
		// TODO Auto-generated method stub
		
	}
	
	
}
