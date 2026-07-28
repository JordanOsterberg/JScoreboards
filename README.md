# JScoreboards
Welcome to the JScoreboards Spigot library page.

If you find bugs- report them as a GitHub issue. Thanks!

If you'd like a video tutorial on how this API works, I have a [YouTube video](https://youtube.com/watch?v=SoPWdEMNFAM) you can watch.
Otherwise, you can take a look at the [wiki](https://github.com/JordanOsterberg/JScoreboards/wiki).
 
## Maven Repository
Please note- as of version 2.0.3 the Maven repository has changed. Update your pom.xml accordingly:

**Repository**:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

**Dependency**
```xml
<dependency>
    <groupId>dev.jcsoftware</groupId>
    <artifactId>JScoreboards</artifactId>
    <version>2.1.4-RELEASE</version>
</dependency>
```

Make sure to specify your Java build version (e.x. Minecraft 1.8 requires Java 8, whereas 1.17 requires Java 16) in your `pom.xml`, as well as shade it into your jar file. The API will not work otherwise:
```xml
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>JAVA_VERSION_HERE</source>
                    <target>JAVA_VERSION_HERE</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```

If you're having trouble, please submit a GitHub issue. The self hosted Maven repo gig is new to me :]

See [LICENSE.md](LICENSE.md) for license information.

## Project Structure
To support multiple Spigot/Bukkit versions, the project utilizes an abstraction layer to communicate with older server API versions without breaking user facing JScoreboard API compatibility.

Simply, the project is organized like this:
- `api` module, responsible for user facing JScoreboards API. This API is extremely stable and will not change between versions unless necessary.
This module also contains a `SpigotAPIVersion` enum which decides which internal implementations to communicate with at runtime.
- `abstraction` module, which defines the requirements for a version specific implementation of certain internal API (generally revolving around registering and handling Minecraft `Scoreboard` objectives.)

With these two modules in place, version specific implementations can be written:
- `1_8-1_12` for supporting Minecraft 1.8 through 1.12*
*There is an additional module, `team-support-1_12`, for supporting new Spigot Team API introduced in 1.12
- `1_13` for supporting Minecraft 1.13
- `1_14-1_17` for supporting Minecraft 1.14 through 1.17 (current latest)

That being said, the only *tested* versions are as follows:
- 1.8
- 1.16
- 1.17

It is unlikely you will encounter any issues with other versions given how the API operates under the hood (no NMS, packets, etc). However, as in all software, there will be issues and bugs. Open a GitHub issue if you find something that needs to be fixed.
