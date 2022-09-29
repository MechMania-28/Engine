# Engine
This is the engine for MechMania 28.
## Before build
Make sure you have Java 11 and gradle installed.
## Build
Run `./gradlew build` .
## Run
Run `java -jar ./build/libs/Engine-1.0-SNAPSHOT.jar` .
Run with -Ddebug=true to display debug messages. It is recommended to pipe the output to a file because it might reach your terminal's line limit.
## Output
After a successful run, a game log with name `game_<timestamp>.json` will show up in the `gamelog` folder.\
You will need Visualizer to see the gameplay out.
