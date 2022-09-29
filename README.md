# Engine
This repository contains source code of the engine for MechMania 28.  You should be able to download the executable `jar` file and run the game as follows with your own bots. 
## Before running
Download the jar exeutable from [release](https://github.com/MechMania-28/Engine/releases).
Make sure you have Java installed on your computer. The engine is compiled with Java 11 so make sure you have version >= 11 with `java -v`.
## Run
Run `java -jar path/to/engine.jar` to start the engine.\
Then, run any desired copies of [Java](https://github.com/MechMania-28/Java-Starterpack) or [Python](https://github.com/MechMania-28/Python-Starterpack) bots following instructions over there.

## Output
After a successful run, a file output with name `game_<timestamp>.json` will show up in the `gamelog` folder.\
You will need [Visualizer](https://github.com/MechMania-28/Visualizer) to see the actual gameplay out.

## Report bugs
We are not professional developers and we make mistakes. If you think anything is wrong or have any questions, feel free to reach out in the Discord or in person with Mechmania staff!

## If you want more from the engine...
- Run with -Ddebug=true to display debug messages. It is recommended to pipe the output to a file because they might reach your terminal's line limit.

- To build the engine from source code, Run `./gradlew build`. For more building options, see [Gradle documentations](https://docs.gradle.org/current/userguide/command_line_interface.html).
