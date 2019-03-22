CD to the following location and run the java compiler as follows:
D:\workspace\java\GUS_JAVA> javac @modules/compile.txt 

Then in file/navigator view see the /outDir folder for the .class files
To run the MainApp use the following
D:\workspace\java\GUS_JAVA> java --module-path outDir -m app/com.gus.app.MainApp

You should see the message from the 'hello' module printed to the console.