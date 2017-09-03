# Welcome 

This application provides different modes for the navigation on predefined routes. It is focused on Münster in this state. Three modes are
available:

 - Map view
 - Map + Picture view
 - Map + AR view


 In these modes a navigator gets different support for the navigation towards a defined location. 

 ## Modify the app

 Clone this repository and import the project into AndroidStudio. 

 ### Set new routes

 To get new routes into the application, replace the ``json`` files in ``MasterThesis\app\src\main\assets`` with ``geojson`` caontaining a LineString for a predefined route and Features for markers that represent decision points on the route.

### Adjust icons

To replace the icons representing POIs in the map add png files into the folder ``MasterThesis\app\src\main\res\drawable-nodpi`` and adjust the code in the class ``Layerinteraction`` as well as the ``arrays.xml`` file containing the names of the layers.

### ToDo

Add augmentations for the geofences that are triggered. The augmentations need to be added according to the right location. The class ``JsonParser`` already provides a method that returns the list of all augmentations on the current route.

The app needs to be tested in the wild. The geofences are tested with a fake gps provider but a whole run of a route from the beginning to the end needs to be done.