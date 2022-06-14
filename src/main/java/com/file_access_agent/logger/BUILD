java_binary(
    name = "loggerBin",
    runtime_deps = ["//src/main/java/com/file_access_agent/logger:loggerLib"],
    visibility=["//visibility:public"],
)

java_library(
    name = "loggerLib",
    srcs = glob(["*.java"]),
    #visibility = [
    #    "//src/main/java/com/file_access_agent/logger/record:__pkg__",
    #    "//src/main/java/com/file_access_agent/advice:__pkg__"
    #    ],
    visibility = ["//visibility:public"],
    deps = [
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
    ]
)