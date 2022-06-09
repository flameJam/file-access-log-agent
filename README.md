# File-Access-Log Java Agent (WIP)

...is a Java agent for logging accessed files and resources of a SUT during test runtime.
This is part of my masters thesis with the goal to include non-code artifacts, such as config-files, resources, ... into the Test Gap Analysis in Teamscale.

## Installation

Once you cloned the project and installed bazel,
adjust the `OUTPUT_FILE_LOCATION` in `src/main/java/com/file_access_agent/FileAccessAgent.java` to your needs. 

Then use

`bazel build //:FileAccessAgent_deploy.jar` 

to build the jar-file of the agent.

In order to get the additional jar-file "loggerBin_deploy.jar" you have to additionally execute

`bazel build //src/main/java/com/file_access_agent/logger:loggerBin_deploy.jar`.

The resulting jars will be
- `bazel-bin/FileAccessAgent_deploy.jar`
- `bazel-bin/src/main/java/com/file_access_agent/logger/loggerBin_deploy.jar`
### Requirements
- [bazel](https://bazel.build/) 5.1.1
- Java 8+

## Usage
Suppose you want to use the agent on your simple HelloWorld-Program:

`java -javaagent:<FileAccessAgent_deploy.jar>=<loggerBin_deploy.jar> yourHelloWorld.java`

The file containing the output will be a simple formatted text file, the location of which is currently still hardcoded in `src/main/java/com/file_access_agent/FileAccessAgent.java:OUTPUT_FILE_LOCATION`

