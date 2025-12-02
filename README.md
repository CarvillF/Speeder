# Proyecto Base de datos - Speeder

Uber-like transport service app for business 

### Descargables del Front-end:

El front-end se realizo en JavaFx a lo cual se requirió las siguientes instalaciones: 

**JavaFx:** https://gluonhq.com/products/javafx/

Dentro de Vscode se instalo las siguientes extensiones: javaFx Support

Dentro del proyecto ya abierto aparecerá una opción en la parte inferior llamada JAVA PROJECTS, al seleccionar esta opción, aparece las referencias (REFERENCED LIBRARIES). AL seleccionar nos aparecerá un boton "+" para agregar las librerías, ahi se tendrá que buscar la carpeta guardada de la descarga de JavaFX, se selecciona la carpeta, y luego la carpeta lib y dentro de esta se selecciona todos los archivos, y se le da a la opción de seleccionar las librerías

Adicionalmente a esto se añadió una configuración dentro del menu de Run en "Add Configuration". Dentro del espacio "Configurations" se agrego:

**"vmArgs":** "module-path (y aqui se pega la ruta donde se encuentra la carpeta lib de javaFX y se remplaza los \ por / si es necesario) -add modules javafx.controls,javafx.xml",

**Para la configuración se siguió este tutorial:** https://www.youtube.com/watch?v=NyK6Wdgw340

**Ademas en el lib se agrego json-2.10.1.jar:** https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/
