## Сборка проекта
1) установить maven<br>
`https://maven.apache.org/download.cgi`<br><br>

2) Установить (в локальный maven репозиторий) библиотеку с общими классами<br>
`mvn -f src/utils install`<br><br>

3) Установить (в локальный maven репозиторий) библиотеку с логикой расчета движения вражеских юнитов<br>
`mvn -f src/ChaseLogic/ install`<br><br>

4) Собрать игру<br>
`mvn -f src/Game/ package`<br>

## Запуск игры

Желательно запускать не в IDE, а в отдельной консоли*:<br>
```java -jar src/Game/target/Game-1.0-SNAPSHOT.jar --size=20 --enemiesCount=2  --wallsCount=150```<br>

*при запуске через IDE могу быть проблемы с интерпритацией ANSI последовательностей, с помощью которых "очищается" экран, и раскрашиваются символы

Подсказки по запуску игры `java -jar src/Game/target/Game-1.0-SNAPSHOT.jar --help`:
```
The following options are required: [--wallsCount], [--enemiesCount], [--size]

Usage like: java -jar 'game.jar' --size=20 --enemiesCount=2  --wallsCount=150

Usage: <main class> [options]
  Options:
    --help, -h
      application usage description
  * --size
      Size of game field
      Default: 0
  * --enemiesCount
      Count of enemy's
      Default: 0
  * --wallsCount
      Count of obstacles
      Default: 0
    --profile
      File with objects' char/color parameters. E.g [--profile=dev] equals to 
      file [application-dev.properties]
      Default: production


Available colors: BLACK, BLUE, BRIGHT_BLACK, BRIGHT_BLUE, BRIGHT_CYAN, BRIGHT_GREEN, BRIGHT_MAGENTA, BRIGHT_RED, BRIGHT_WHITE, BRIGHT_YELLOW, CYAN, GREEN, MAGENTA, RED, WHITE, YELLOW
```

