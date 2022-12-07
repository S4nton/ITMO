#!/bin/bash

ps -e --format="pid cmd" | grep "[0-9] /sbin/" > res2
