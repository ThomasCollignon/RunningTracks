# Sonarqube analysis

Run a free local instance of sonarqube

## Pre-requisite

* Note: Sonarqube 9.9 needs Java 17.
* [Download Sonarqube](https://docs.sonarqube.org/latest/try-out-sonarqube/)
* Extract it in `/home`
* Remove the version number from the folder name.
* [Set-up Maven SonarScanner](https://docs.sonarqube.org/latest/analyzing-source-code/scanners/sonarscanner-for-maven/)
   * In `settings.xml` (located for ex. in `/etc/maven/` or in `~/.m2/`), add:

```xml

<settings>
   <pluginGroups>
      <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
   </pluginGroups>
   <profiles>
      <profile>
         <id>sonar</id>
         <activation>
            <activeByDefault>true</activeByDefault>
         </activation>
      </profile>
   </profiles>
</settings>
```

* Start sonar server: `~/sonarqube/bin/linux-x86-64/sonar.sh console`
* Configure the project: go in `localhost:9000`
   * create a project > manually
   * analyze > locally > set up a token

## Run analysis

### Start sonar server

`~/sonarqube/bin/linux-x86-64/sonar.sh console`

### Launch analysis

In project folder:
`mvn sonar:sonar -Dsonar.login=[token]`

See result in [localhost:9000](http://localhost:9000)
