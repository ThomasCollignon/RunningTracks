# Java Flight Recorder

[I couldn't manage to render the flame graph with a free tool](https://stackoverflow.com/questions/75886090/cant-see-flame-graph-from-jfr-file-with-jmc-or-flameviewer).

For example to generate a .jfr file for a specific test class, use

`mvn test -Dtest=DataRunnerIT -DargLine="XX:StartFlightRecording,filename=flight.jfr"`

The [flame graph](https://www.jetbrains.com/help/idea/read-the-profiling-report.html) can't then be rendered using
IntelliJ Ultimate > View > Profiler > Open snapshot > _flight.jfr_
