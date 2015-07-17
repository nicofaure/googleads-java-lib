DoubleClick for Publishers API Java Client Library Examples
===========================================================

For instructions on how to get started, please visit github: https://github.com/googleads/googleads-java-lib

For API and client library updates and news, please follow our [Google+ Ads Developers page](https://plus.google.com/+GoogleAdsDevelopers/posts) and our [Google Ads Developers blog](http://googleadsdeveloper.blogspot.com/) 

For any issues relating to the API (not the library), please see the [DFP API forum](https://groups.google.com/forum/#!forum/google-doubleclick-for-publishers-api)

NOTAS PARA GONZA:
================
Generalmente, hay tres factores que hacen romper la creación de AdUnits.

* Conectividad: Emi utiliza proxy, eso generó quilombos.

* Encoding: el archivo debe tener UTF-8 without BOM. Windows, lo guarda por default en UTF-8 with BOM. Eso hace romper generalmente esta linea de código (https://github.com/nicofaure/googleads-java-lib/blob/change-adunit-generation/examples/dfp_axis/src/main/java/dfp/axis/v201505/inventoryservice/CreateAdUnits.java#L142)

* Mal formato del archivo. Generalmente, la primer línea. Tener en cuenta que es un archivo separado por tabs. Las columnas son:

	* [0]: Category Level
	* [1]: Category / Path (en MyML) 
	* [2]: AdUnitName
	* [3]: ParentCategory. En el caso de la primer linea, debe ser igual que la categoria. 
	* [4]: Sizes. En el caso de tener mas de un size, debe estar delimitado por ';' 
	* [5]: Un campo adicional, porque excel exporta mal los datos.


EJEMPLO 1:

```
1	MLA1367	MLA - Antigüedades	MLA1367	300x250	MLA1367 - MLA - Antigüedades
2	MLA10081	MLA - Antigüedades - Balanzas Antiguas	MLA1367	300x250	MLA10081 - MLA - Antigüedades - Balanzas Antiguas
2	MLA10232	MLA - Antigüedades - Máquinas de Escribir Antiguas	MLA1367	300x250	MLA10232 - MLA - Antigüedades - Máquinas de Escribir Antiguas
```


EJEMPLO MyML:

```
1	MLC_MIML_Mi_cuenta	MLC - Mi cuenta	MLC_MIML_Mi_cuenta	728x90	a
1	MCO_MIML_Mi_cuenta	MCO - Mi cuenta	MCO_MIML_Mi_cuenta	728x90	a
1	MCR_MIML_Mi_cuenta	MCR - Mi cuenta	MCR_MIML_Mi_cuenta	728x90	a
```
