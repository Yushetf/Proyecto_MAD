# Bar Finders!
## Workspace

  · Github: https://github.com/Yushetf/Proyecto_MAD

  · Releases: https://github.com/Yushetf/Proyecto_MAD/releases

  · Workspace: https://upm365.sharepoint.com/sites/Tracking12/SitePages/Tracking.aspx

## Description

With the Bar Finders! application, you can find the nearest bars or restaurants in a matter of seconds based on our current location, and you can also indicate the maximum distance at which you want these bars or restaurants to appear. Additionally, we can automatically store in a list the bars that were available when we last used the application, as they will be saved in a database.

Thanks to Bar Finders!, we will save time by not having to filter the type of place we want to search for, as it focuses exclusively on bars or restaurants.

## Screenshots and navigation

![Main screen capture.](https://github.com/Yushetf/Proyecto_MAD/blob/master/Home.png)
Main Screen

![Map part 1.](https://github.com/Yushetf/Proyecto_MAD/blob/master/MAPA.png)
Main Map Screen without a set maximum distance

![Map part 2.](https://github.com/Yushetf/Proyecto_MAD/blob/master/map2.png)
Setting a new maximum distance

![Map part 3.](https://github.com/Yushetf/Proyecto_MAD/blob/master/mapa3.png)
Map Screen with a specified maximum distance

![List.](https://github.com/Yushetf/Proyecto_MAD/blob/master/lista.png)
List of restaurants

## Demo Video
[Demo](https://upm365-my.sharepoint.com/personal/yushetf_lopez_jimenez_alumnos_upm_es/_layouts/15/stream.aspx?id=%2Fpersonal%2Fyushetf%5Flopez%5Fjimenez%5Falumnos%5Fupm%5Fes%2FDocuments%2Fgrabacion%20bar%20finders%201%2Ewebm&referrer=StreamWebApp%2EWeb&referrerScenario=AddressBarCopied%2Eview "Demo video")

## Features

**Functional features:**

**------------------------------------------------------------------------------------------**

  · Search for bars or restaurants.

  · Bars or restaurants available are placed on the map.

  · Your current location is marked on the map.

  · A list is created with the available bars or restaurants so that we can see the names of those available establishments.

  · You can specify the maximum distance at which you want bars or restaurants to appear.

**Technical features:**

**------------------------------------------------------------------------------------------**

  · Retrofit Integration: Utilizes Retrofit library for making HTTP requests to the Overpass API.

  · Data Classes: Utilizes Kotlin data classes (OverpassResponse, Osm3s, Element) to represent JSON responses from the Overpass API.

  · Room Database: Implements a local SQLite database using Room library for storing location data (LocationEntity) and bar data (BarEntity).

  · DAO (Data Access Object) Interfaces: Defines DAO interfaces (ILocationDao, IBarDao) for accessing data from Room database.

  · Location Services: Utilizes Android's Location Manager to access the device's location (MainActivity and OpenStreetMapActivity).

  · UI Components: Implements various UI components such as Buttons, EditText, TextView, ListView, etc., for user interaction and display.

  · Asynchronous Operations: Uses Kotlin coroutines for handling asynchronous operations, such as database operations (MainActivity, SecondActivity, OpenStreetMapActivity).

  · Permission Handling: Requests and checks permissions for accessing device location (MainActivity, OpenStreetMapActivity).

  · Navigation Components: Utilizes BottomNavigationView for navigation between different activities (MainActivity, OpenStreetMapActivity, SecondActivity, SettingsActivity).

  · Alert Dialogs: Displays AlertDialogs for user input and notifications (MainActivity, OpenStreetMapActivity, SecondActivity).

  · Shared Preferences: Saves and retrieves user settings using SharedPreferences (MainActivity, SecondActivity, SettingsActivity).

  · Map Integration: Integrates OpenStreetMap using OSMDroid library, displays map tiles and markers (OpenStreetMapActivity).

  · Network Operations: Performs network requests using Retrofit for fetching nearby bars/restaurants from the Overpass API (OpenStreetMapActivity).

## How to Use
  · First step: we enter the application and wait until the coordinates appear on the main screen (otherwise, the map will not load correctly).

  · Second step: Once our coordinates have loaded, we can access the map and see the restaurants or bars within a 5km distance from us (default distance).

  · Third step: We can modify that distance. To do this, we need to press the "Set Max Distance" button (located at the top left) and enter the desired distance. Press OK. 
    Now, the bars within the maximum distance we just entered will appear.

  · Fourth step: We can view a list of available bars or restaurants if AFTER using the map, we go to the collection button. There, the names of the bars or restaurants   
    will appear. We can also clear the list by clicking on the button located at the top right (DELETE ALL), which will delete the entire database.

## Participants

List of Proyecto_MAD developers:

  · Yushetf López Jiménez (yushetf.lopez.jimenez@alumnos.upm.es)

  · Jaime Pastor Ocariz (jaime.pastor.ocariz@alumnos.upm.es)

