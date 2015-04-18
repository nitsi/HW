Ray Tracing : 

Package: proj.ex3.render.raytrace:
=============================================

1)Camera.java
2)Hit.java
3)IInitable.java
4)Light.java
5)Material.java
6)OmniLight.java
7)RayTracer.java
8)Scene.java
9)Sphere.java
10)SpotLight.java
11)Surface.java
12)Triangle.java
13)Trimesh.java
=================



The implemented classes:


1)Camera.java
=============
This class implements the scene camera and is in charge of creating
the rays that goes through each pixel in the scence.

2)Hit.java
==========
This class represents an intersection between a surface(sphere or trimesh) and ray,
and holds the point of intersection between them. 


3)IInitable.java
================
This interface is implemented by various classes in this package with the
intent of initalizing them with information from data structers created by the XML parser.

4)Light.java
============
This class is the template for both the omni-light and spot-light types of light and it holds
the characteristics held by both like color and position.
Thus both the OmniLight and SpotLight classes extend this class.


5)Material.java
===============
This class holds the different types of materials in the scene
which needs to be taken into account in the process of ray-tracing.

6)OmniLight.java
================
This class represents an omni-directional light spreading to all directions
from a single source, it extends the Light class to add functionality to it.

7)RayTracer.java
================
This class is the heart of the ray-tracing process.
It renders the scene pixel by pixel from top to bottom
using information processed by the Scene class to construct
the scene.

8)Scene.java
=============
Represents all of the objects found in a given XML scene.
creates the data-structers and classess to account for them and processes
the complete scene which is finnaly shown by the RayTracer class.

9)Sphere.java
=============
This class represents the Sphere type object in the XML scene.

10)SpotLight.java
=================
This class represents a single-source light having a single direction to where it spreads.
it extends the Light class to add functionality to it.

11)Surface.java
===============
The base class for the Sphere and Triangle classes
and for any other objects that can be added to scene in the future.
It contains methods for calculating the distance between a ray and the given surface
and finding the normal in an intersection point with the given surface as well as holding
other properties of objects in the scene.
 
12)Triangle.java
================
This class represents the Triangle type object in the XML scene.

13)Trimesh.java
===============
This class represents a set of triangles.
This main purpose of this class is to afliate a group of triangles
with the geometric shape they create within the scene.

14) Disk.java
This class represents a figure of disc.

Bonuses:
=======
We implemented super sampling ray tracing in class RayTracer.java, method: castRaySupa.
