#!/bin/bash

bazel build //:FileAccessAgent_deploy.jar

OUTPUT_DIR=binaries/$(git rev-parse --abbrev-ref HEAD)

mkdir -p $OUTPUT_DIR

if test -f "$OUTPUT_DIR/FileAccessAgent_deploy.jar"; then
    chmod +w $OUTPUT_DIR/FileAccessAgent_deploy.jar
fi

cp -L bazel-bin/FileAccessAgent_deploy.jar $OUTPUT_DIR/FileAccessAgent_deploy.jar