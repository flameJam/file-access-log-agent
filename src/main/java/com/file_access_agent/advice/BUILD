java_library(
    name = "advices",
    srcs = glob(["*.java"]),
    deps = [
        "@maven//:net_bytebuddy_byte_buddy",
        "//src/main/java/com/file_access_agent/logger:loggerLib",
        "//src/main/java/com/file_access_agent/common/util/environment:util_environment_lib",
    ],
    visibility = ["//src/main/java/com/file_access_agent/transformer:__pkg__"],
)