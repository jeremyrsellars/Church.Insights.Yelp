@ECHO OFF
call lein clean
call lein cljsbuild once dev
