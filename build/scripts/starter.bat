@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  starter startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and STARTER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\starter-1.0.0-SNAPSHOT.jar;%APP_HOME%\lib\vertx-lang-kotlin-4.4.1.jar;%APP_HOME%\lib\vertx-lang-kotlin-coroutines-4.4.1.jar;%APP_HOME%\lib\vertx-redis-client-4.4.1.jar;%APP_HOME%\lib\vertx-config-4.4.1.jar;%APP_HOME%\lib\vertx-grpc-server-4.4.1.jar;%APP_HOME%\lib\vertx-grpc-common-4.4.1.jar;%APP_HOME%\lib\vertx-core-4.4.1.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.4.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.7.21.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.90.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.90.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.90.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.90.Final.jar;%APP_HOME%\lib\netty-handler-4.1.90.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.90.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.90.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.90.Final.jar;%APP_HOME%\lib\netty-codec-4.1.90.Final.jar;%APP_HOME%\lib\netty-transport-4.1.90.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.90.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.90.Final.jar;%APP_HOME%\lib\netty-common-4.1.90.Final.jar;%APP_HOME%\lib\jackson-core-2.14.0.jar;%APP_HOME%\lib\grpc-stub-1.50.2.jar;%APP_HOME%\lib\grpc-protobuf-1.50.2.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.50.2.jar;%APP_HOME%\lib\grpc-api-1.50.2.jar;%APP_HOME%\lib\guava-31.1-android.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.7.21.jar;%APP_HOME%\lib\kotlin-stdlib-1.7.21.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.7.21.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\grpc-context-1.50.2.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\error_prone_annotations-2.11.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\proto-google-common-protos-2.9.0.jar;%APP_HOME%\lib\protobuf-java-3.21.7.jar


@rem Execute starter
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %STARTER_OPTS%  -classpath "%CLASSPATH%" io.vertx.core.Launcher %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable STARTER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%STARTER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
