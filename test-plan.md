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
