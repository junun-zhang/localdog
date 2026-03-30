#!/usr/bin/env bash

##############################################################################
#
#   Copyright 2015-2023 Gradle Inc.
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
##############################################################################

# Attempt to set APP_HOME

# Resolve links: $0 may be a link
app_path=$0

# Need this for daisy-chaining
saved_arg_0="$0"
while [[ -h "$app_path" ]]; do
  ls_output="$(ls -ld "$app_path")"
  link_target=$(expr "$ls_output" : '.*-> \(.*\)$')
  if [[ "$link_target" == /* ]]; then
    app_path="$link_target"
  else
    app_path="$(dirname "$app_path")/$link_target"
  fi
done

# Convert relative path to absolute path
app_home=$(cd "$(dirname "$app_path")" && pwd -P)

# Use the package built into the distribution if present
if [[ -e "$app_home"/gradle/wrapper/gradle-wrapper.jar ]]; then
    GRADLE_WRAPPER_JAR="$app_home"/gradle/wrapper/gradle-wrapper.jar
else
    # Fall back to the globally installed version
    GRADLE_WRAPPER_JAR="$(which gradle | sed 's/\/bin\/gradle$//')/lib/gradle-wrapper.jar"
fi

# Escape application home
save () {
    string="$1"
    strlen=${#string}
    result=""
    for (( i=0; i<strlen; i++ )); do
        c="${string:i:1}"
        case $c in
            [!a-zA-Z0-9./-])
                printf -v oct '%03o' "'$c"
                result+=$(printf \\%s "$oct")
                ;;
            *)
                result+="$c"
                ;;
        esac
    done
    printf %s "$result"
}

escaped_app_home=$(save "$app_home")

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$cygwin" = "false" -a "$darwin" = "false" ] ; then
    case $MAX_FD in
      max*)
        # In POSIX sh, ulimit -H is undefined.
        if command -v ulimit >/dev/null 2>&1; then
            MAX_FD=$(ulimit -H)
        else
            MAX_FD=4096
        fi
        ;;
      *)
        MAX_FD=4096
        ;;
    esac
    ulimit -n "$MAX_FD"
    if [ $? -ne 0 ]; then
        warn "Could not set maximum file descriptor limit: $MAX_FD"
    fi
fi

# Collect all arguments for the java command, stacking in reverse order:
#   * args from the command line
#   * the main class name
#   * -classpath
#   * -D...appname settings
#   * --module-path (only if needed)
#   * DEFAULT_JVM_OPTS

# For Cygwin, switch paths to Windows format before running java
if $cygwin ; then
    APP_HOME=`cygpath --path --mixed "$APP_HOME"`
    CLASSPATH=`cygpath --path --mixed "$CLASSPATH"`
    JAVACMD=`cygpath --unix "$JAVACMD"`

    # Now convert the arguments to Windows paths.
    for arg in "$@" ; do
        if [[ -f "$arg" ]] ; then
            arg=`cygpath --path --mixed "$arg"`
        fi
        CASE_ARGS=("$CASE_ARGS" "$arg")
    done
    shift
    for arg in "${CASE_ARGS[@]}" ; do
        set -- "$@" "$arg"
    done
fi

exec "$JAVACMD" "$@"