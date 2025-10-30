Находясь в корневом каталоге проекта (ImagesToChar)

Сборка:
rm target -rf
mkdir target
javac -target 8 -d target src/java/edu/school21/printer/app/* src/java/edu/school21/printer/logic/*

Запуск:
java -classpath target edu.school21.printer.app.App --img=src/resources/it.bmp --white=. --black=O
or
java -classpath target edu.school21.printer.app.App --img=src/resources/it.bmp

JAR для распространения:
1) Выполнить сборку
2) jar vcfm target/images-to-chars-printer.jar src/manifest.txt -C src resources -C target edu
    cp -r src/resources target/

Запуск JAR:
java -jar target/images-to-chars-printer.jar


