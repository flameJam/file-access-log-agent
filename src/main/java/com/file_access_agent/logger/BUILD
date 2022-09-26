java_binary(
    name = "loggerBin",
    runtime_deps = ["//src/main/java/com/file_access_agent/logger:loggerLib"],
    visibility=["//visibility:public"],
)

java_library(
    name = "loggerLib",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/java/com/file_access_agent/common/util/location:util_location_lib",
    ]
)