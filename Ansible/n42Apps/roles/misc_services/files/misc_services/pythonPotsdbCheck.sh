#!/bin/bash
 PIP=$(pip freeze |grep "potsdb")
 if [ -z "$PIP" ]
 then
	exit 1
 else
	exit 0
 fi
