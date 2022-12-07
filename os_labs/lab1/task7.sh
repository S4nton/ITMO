#!/bin/bash

grep -E -r -h -o -I "([a-zA-Z0-9\_\-\.]+)@([a-zA-Z]+)\.([a-zA-Z]{2,5})" /etc | tr '\n' ',' | sed 's/,$//'
