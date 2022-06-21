# File-Access-Log Java Agent (WIP)

...is a Java agent for logging accessed files and resources of a SUT during test runtime.
This is part of my masters thesis with the goal to include non-code artifacts, such as config-files, resources, ... into the Test Gap Analysis in Teamscale.

## Installation

Once you cloned the project and installed bazel,
adjust the `output_file_location` and the `output_file_prefix` in `file_access_agent.properties` to your needs. 

Then use

`bazel build //:FileAccessAgent_deploy.jar` 

to build the jar-file of the agent.

The resulting jars will be
- `bazel-bin/FileAccessAgent_deploy.jar`

### Requirements
- [bazel](https://bazel.build/) 5.1.1
- Java 8+

## Usage
Suppose you want to use the agent on your simple HelloWorld-Program:

`java -javaagent:<FileAccessAgent_deploy.jar>=<loggerBin_deploy.jar> yourHelloWorld.java`

The file containing the output will be a simple formatted JSON file, the location of which is configured in `file_access_agent.properties:output_file_location`

