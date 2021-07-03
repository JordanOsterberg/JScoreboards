# JScoreboards
Welcome to the JScoreboards Spigot library page.

JScoreboards is currently in **alpha** development- there will be bugs and I recommend you don't use this in production until some of those bugs get patched.

If you find bugs- report them as a GitHub issue. Thanks!
 
## Maven Repository

**Repository**:
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

**Dependency (type the latest version in for version tag)**
```
<dependency>
    <groupId>com.github.JordanOsterberg</groupId>
    <artifactId>JScoreboards</artifactId>
    <version></version>
</dependency>
```

[![](https://jitpack.io/v/JordanOsterberg/JScoreboards.svg)](https://jitpack.io/#JordanOsterberg/JScoreboards)

See [JitPack](https://jitpack.io/#JordanOsterberg/JScoreboards) for more information / the most up-to-date versioning if you're having trouble. 

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

It is unlikely that you will encounter any issues with other versions given how the API operates under the hood (no NMS, packets, etc). However, as in all software, there will be issues and bugs. Open a GitHub issue if you find something that needs to be fixed.