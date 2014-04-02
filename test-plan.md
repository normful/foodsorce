# Test Plan for TeamTau FoodSorce

## Mapping UI

- On startup, a map should appear with default location set to downtown Vancouver as a marker.
- On startup, vendor markers should be presesnt on the map.
- On startup, if application is allowed to obatin user location, the user location marker and focus will be set to that location.
- On startup, the distance option will be set at "all".
- User should be able to interact with the map by zooming and moving around.
- User can input own address or location through text box, and send request the result by pressing enter or hitting the set location button.
- When user sets current location, the user location marker and focus will be set to that location.
- When user sets current location to be either an invalid location or a location outside of vancouver, an error message will appear requesting another address within Vancouver.
- When new location is set, distance option does not change.
- When new distance option is set, distance does not change
- When new distance is set, markers appear/dissapear wih accordance to the distance option selected.
- Selecting an already selected distance option does not do anything.
- Default field for address field is "address" and text is highlighted when selected
- Selecting Main Page, Profile, Admin or Sign Out does not effect map.
- Selecting a vendor Marker reveals a popup, which contains the vendor information in a nice format.
- Selecting the same vendor marker a second time closes the vendor marker popup.
- Selecting another vendor marker when another vendor marker popup appears should close the popup and open a new one corresponding to the current vendor
- When a vendor marker is clicked, the VendorInfoPanel appears.

## General UI

- On startup, the Title, "FoodSorce" and a menu on the left should be present.
- On startup, left of the map a log in page should be shown with a link to login with your Google Account.
- On startup, the menu on the left should contain Main Page, Profile, Admin and Sign Out links.
- Clicking Main Page should link to the vendor list. 
- Clicking Main more than once does not do anything.
- Clicking Profile should link to the not yet completed profile page. 
- Clicking Profile more than once does not do anything.
- Clicking Admin should link to the Administrator Console page. 
- Clicking Admin more than once does not do anything.
- Clicking Sign Out should log the user out, if user is signed in, and refresh the page to the login page.
- Clicking Sign Out more than once or when not signed in does not do anything except refresh the page
- Administrator Console Page should display a clickable button labelled "Import Data".
- Clicking import data should display a pop-up displaying whether import was successful or failed.
- Clicking import data mutliple times does not crash program.

## Vendor Display UI

- Vendor display displays all venues on startup.
- Vendor display changes corresponding to the correct distance option selected.
- Vendor Name, Type of Food, Quality, Cost and Location are all correctly displayed.
- User is able to scroll through the vendor list.
- VendorInfo Panel is shown when a vendor name is clicked.
- Vendor Search correctly displays vendors when search button is pressed.
- Vendor Search displays the correct filtered venfors when the search button is pressed.
- Vendor Search only displays the filtered vendors of the distance option selected.
- Vendor quality and cost are updated after submitting a review.
- Vendor Search is saved when logging out and loggin back in.

## VendorInfo Display UI
- VendorInfo Panel displays name, location, description, average quality, average cost, and a list of all reviews.
- VendorInfo Panel also displays a add To favourites and a add Reviews button.
- Add reviews button dissapears after adding a review.
- AddToFavourites Button should add the vendor to the users list of favourites.

## Social Media Integration UI
- Twitter button is present on the top right corner displaying the current number of followers.
- When cliking on Follow @FoodSorce, a popup is displayed allowing the user to follow FoodSorce.
- Facebook button is present on the top right corner dispalying the current number of likes.
- When cliking on Like, a popup is displayed allowing the user to like FoodSorce on Facebook.
- Both Facebook and twitter will prompt the user to login in the popup if user is not logged in.
- Profile Page image displays a default image if user has not pressed Faceook Photo button.
- When the user logs out and logs back in, that the photo URL is stored and their photo is displayed.
- Import Facebook photo button correctly imports the facebook image.

## Admin Page
- Admin Panel link only shows when a user is logged in as an admin.
- Import Data button is visible on admin page.
- When Import Data button is pressed, a popup will occur showing whether the import failed or passed.
- A list of user Profiles is present allowing the user to delete users by pressing the Delete User button.
- The listofusers Panel is updated when the DeleteUser button is pressed deleting the selected user.
- When logging out and logging back in, the list of users should be the same.

## Profile Page
- Profile Page link should be visible only when the user is logged in.
- A profile page should contain Email, Favourite foods, WebsiteGender, Headline, Search Text Search, Distance and Hometown fields.
- An import Facebook button sise present
- A profile photo is present.
- The Profile Photo is set when clicking import Facebook button.
- The Profile Photo and all other fields is saved after loggin out and loggin back in.
- Profile Page should contain An Edit Profile link at the button.
- When clicking the Edit Profile Link, user can edit profile fields.
- Pressing submit after editting profile fields, saves the profile fields.
- A Table of favourite Vendors is present on the right side.
- favouriteVendors should display favourite vendors that the user has favourited.





