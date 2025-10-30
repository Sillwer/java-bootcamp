# Java basics - Day 04
### JAR

*Takeaways: Today you will learn to create library archives and use external libraries*

# Contents
1. [Exercise 00 — Packages](#exercise-00--packages)
2. [Exercise 01 — First JAR](#exercise-01--first-jar)
3. [Exercise 02 — JCommander & JCDP](#exercise-02--jcommander--jcdp)

### General Rules
- There is only one Java version for you, 1.8. Make sure you have the compiler and interpreter for this version installed on your machine.
- The code is more often read than written. Maintain code formatting based on Oracle standards.
- Use System.out for output.

### Exercise 00 — Packages

Exercise 00: Packages ||
---|---
Turn-in directory	| ex00
Files to turn-in	| ImagesToChar-folder (exclude target)

Code can be organized at several levels. Packages are one way of organizing code, where classes are located in individual folders. 

Now your task is to implement functionality that prints a two-color image to the console. 

An example of a black and white BMP image (this format is mandatory for the solution). The image size is 16*16 pixels.

![it](misc/images/it_black.png)

You can download this image [here](https://yadi.sk/i/nt-C_kZKWrlyNQ ).

Your application must accept input parameters corresponding to characters to be displayed in place of white and black pixels. Another main function startup parameter is the full path to the image on your hard disk.

If the character "." is used for white and "0" for black, the image in the console might look like this:

![it_console](misc/images/it_console.png)

Application logic must be distributed between different packages and have the following structure:

- ImagesToChar — project folder.
  - src — source files.
    -	java — files of Java source code.
        - edu.school21.printer — a series of main packages.
          -	app — a package that contains classes for startup.
          -	logic — a package that contains the logic for converting an image into an array of characters.
  -	target — compiled .class files.
    -	edu.school21.printer ...
  -	README.txt
  
The README.txt file must contain instructions for compiling and running your source from the console (non-IDE). The instructions are written for the state where the console is opened in the project's root folder.

### Exercise 01 — First JAR

Exercise 01: First JAR ||
---|---
Turn-in directory	| ex01
Files to turn-in	| ImagesToChar-folder (exclude target)

Now you need to create a distribution package of the application — a JAR archive. It is important that the image is included in this archive (a command-line parameter for the full path to the file is not required for this task).

The following project structure shall be adhered to:

- ImagesToChar — project folder.
  - src — source files.
    - java — files of Java source code.
      - ...
    -	resources — a folder with resource files.
         - image.bmp — the displayed image.
    - manifest.txt — a file containing the description of the initial point for archive startup.
  - target — compiled .class files and archive.
    - edu.school21.printer ...
    - resources
    - images-to-chars-printer.jar
  - README.txt

The archive and all compiled files must be placed in the target folder during assembly (without manual file transfer; you can use the cp command on the resource folder).

The README.txt file should also contain information about assembling and starting the archive.

### Exercise 02 — JCommander & JCDP
Exercise 02: JCommander & JCDP ||
---|---
Turn-in directory	| ex02
Files to turn-in |	ImagesToChar (exclude lib and target)

Now you should be using external libraries:
- JCommander for the command line. 
- JCDP or JColor for colored output.

Download the archives containing these libraries and add them to the project of the previous task. 

Now the startup parameters of the application should be edited with JCommander tools. The image should be displayed using the "colored" output option of the JCDP library.

Required project structure:
- ImagesToChar — project folder.
  -	lib — external library folder.
    -	jcommander-*.**.jar
    -	JCDP-*.*.*.jar/JCOLOR-*.*.*.jar
  -	src — source files.
  -	target — compiled .class files and archive.
    -	edu.school21.printer
    -	com/beust ... — .class files of JCommander library.
    -	com/diogonunes ... — .class files of JCDP library.
    -	resources
    -	images-to-chars-printer.jar
  -	README.txt

The README.txt file must also contain the information about including external libraries in the final assembly.

Example of program operation:

`$ java -jar images-to-chars-printer.jar --white=RED --black=GREEN`

![it_red](misc/images/it_red.png)
