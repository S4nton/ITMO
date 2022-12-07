#!/bin/bash

grep -E .*'(WW)'.* /var/log/anaconda/X.log | sed 's/(WW)/Warning:/g' > fuil.log
grep -E .*'(II)'.* /var/log/anaconda/X.log | sed 's/(II)/Iformation:/g' >> fuil.log
