# File-Access-Log Java Agent (WIP)

...is a Java agent for logging accessed files and resources of a SUT during test runtime.
This is part of my masters thesis with the goal to include non-code artifacts, such as config-files, resources, ... into the Test Gap Analysis in Teamscale.

## Installation

Once you cloned the project and installed bazel,
adjust the `output_file_location` and the `output_file_prefix` in `file_access_agent.properties` to your needs. The path in here has to be either absolute or will be interpreted as relative from the working directory during the execution of the instrumented program.

Then use

`bazel build //:FileAccessAgent_deploy.jar` 

to build the jar-file of the agent.

The resulting jars will be
- `bazel-bin/FileAccessAgent_deploy.jar`

### Building for different branches
If you intend to use different versions of the agent, for example the version on branch `main` and the version (currently in development) on branch `real_read_dev`, using the build-script `buildAgentForBranch.sh` might be nice. It does the usual building process, but then puts the output into a separate directory called `binaries/<your_currrent_git_branch_name>`.
Added this to prevent me from trying to execute large sets of tests with the agent while accidentally using a broken agent, because I previously spent time on a development branch and built a different agent-jar for debugging purposes...

### Requirements
- [bazel](https://bazel.build/) 5.1.1
- Java 8+

## Usage

Before executing the target program you have to configure the environment variable containing the repository directory of the target program - paths of files accessed during the execution will be outputed relative to it.

`export FILE_ACCESS_AGENT_REPOSITORY=/path/to/your/repo`

Suppose you want to use the agent on your simple HelloWorld-Program:

`java -javaagent:your/path/to/FileAccessAgent_deploy.jar yourHelloWorld.java`

The file containing the output will be a simple formatted JSON file, the location of which is configured in `file_access_agent.properties:output_file_location`.
If you wish to override the preconfigured location export the new location to `FILE_ACCESS_AGENT_OUTPUT`, like so:

`export FILE_ACCESS_AGENT_OUTPUT=/path/to/new/location/file_prefix_`

The path has to be either absolute or will be interpreted as relative from the working directory during the execution of the instrumented program.
The resulting file will look like this: `/path/to/your/location/file_prefix_<timestamp-in-millis-since-1970>.json`

### Debug Mode
For activating the debug mode set the environment variable `FILE_ACCESS_AGENT_DEBUG=true`.
As a Result the Agent will
1. output info on instrumentation and errors during the instrumentation
2. log the stacktrace which triggered another Log
    - currently only works for `FileInputStreamCreatedFileRecord` & `ResourceAcquiredRecord` -  the only ones used in the current version
3. add a new section to the output file which contains the stacktrace and other info for each corresponding record

Unfortunately the stacktrace logging probably has a bigger impact on the instrumented programs' performance.

### Special Use Cases
#### Tomcat

add the argument `-javaagent:path/to/agent.jar` to the environment variable `CATALINA_OPTS`:

- either use `export CATALINA_OPTS="$CATALINA_OPTS -javaagent:path/to/agent.jar"`
- or add the line `CATALINA_OPTS="$CATALINA_OPTS -javaagent:path/to/agent.jar"` to `<tomcat_home>/bin/setenv.sh`
  - you might have to create the script first and set it executable
  - for windows the file obviously has to be named `setenv.bat`
- using the environment variable `JAVA_OPTS` should work, too

#### Maven
Define the usual environment variables. Then define the Surefire plugin in your POM and pass the JVM arg via Surefire's configuration.
For example:

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>[your version]</version>
    <configuration>
        <argLine>-javaagent:/path/to/FileAccessAgent_deploy.jar</argLine>
    </configuration>
</plugin>
```

#### Gradle
Define the usual environment variables. Then find the `test` task in your or one of your `build.gradle` files and set the `test.jvmArgs` with the usual `-javaagent:...` argument.

Example:
```gradle
test {
    test.jvmArgs '-javaagent:/path/to/your/agent'
}
```