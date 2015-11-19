@ECHO OFF
ECHO Building clojurescript to run in node.  The build should run node automatically.
pushd %~dp0
ECHO Running in %CD%
call build.cmd
popd