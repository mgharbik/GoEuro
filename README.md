GoEuroTest
==========
1.Once installed the project, create the jar file and run:
> java -jar GoEuroTest.jar MAR

Result in the console:

379620: Marseille, France

460567: Mariupol, Ukraine

378668: Marbella, Spain

376886: Marl, Westfalen, Germany

406125: Maribor, Slovenia

376876: Marburg an der Lahn, Germany

388218: Marsala, Italy

381235: Marousi, Greece


+


test.csv file created with list of cities their details



2.
Errors detected: if the command line running without argument or with more than one argument, in this case the correct uasage is showing. As well as if the segund argument dosn't correspond to any file:

> java -jar GoEuroTest.jar

Usage: java -jar GoEuroTest.jar "STRING"

> java -jar GoEuroTest.jar MAR STR

Usage: java -jar GoEuroTest.jar "STRING"

> java -jar GoEuroTest.jar STRING

The value of the results key is null or the url is incorrect



3. 
dependencies: json-simple-1.1.1.jar
