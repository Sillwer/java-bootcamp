Находясь в корневом каталоге проекта (ImagesToChar)

Сборка:
rm target -rf && mkdir target
cp -r src/resources target
javac -target 8 \
    -d target \
    src/java/edu/school21/printer/app/* src/java/edu/school21/printer/logic/* \
    -cp "lib/*"


Запуск:
java -classpath target:lib/* edu.school21.printer.app.App --white=GREEN --black=BLUE --img=src/resources/it.bmp
or
java -classpath target:lib/* edu.school21.printer.app.App --img=src/resources/it.bmp


JAR для распространения (предварительно выполните сборку):
cp -r src/resources target
cd target && jar -xf ../lib/jcommander-1.82.jar com && jar -xf ../lib/JCDP-5.5.1.jar com && cd ..
jar vcfm target/images-to-chars-printer.jar src/manifest.txt -C target .


Запуск JAR:
java -jar target/images-to-chars-printer.jar
java -jar target/images-to-chars-printer.jar --white=GREEN --black=BLUE