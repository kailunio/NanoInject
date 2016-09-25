#!/bin/bash

class_path=''
for s in `ls -1`
do
	if [[ ${s:0-4} == '.jar' ]];then
		class_path="$class_path./$s:"
	fi
done
export CLASSPATH=$class_path

javac NanoInject.java
javac NanoInjectTest.java
java org.junit.runner.JUnitCore NanoInjectTest

ls -1 | grep .class$ | sed 's/^/rm /' | sed 's/\$/\\$/' | bash
