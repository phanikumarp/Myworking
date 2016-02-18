#!/bin/bash
   version=$(python -V 2>&1 | awk -F ' ' '/Python/ {print $2}')
    echo python version $version
    if [[ "$version" > "2.7" ]]; then
        exit 0
    else         
		exit 1
    fi


