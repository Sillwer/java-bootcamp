Находясь в корневом каталоге проекта (ImagesToChar)

Сборка:
rm target -rf
mkdir target
javac -target 8 -d target src/java/edu/school21/printer/app/* src/java/edu/school21/printer/logic/*

Запуск:
java -classpath  target edu.school21.printer.app.App --img=src/resources/it.bmp --white=. --black=O
or
java -classpath  target edu.school21.printer.app.App --img=src/resources/it.bmp