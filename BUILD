java_binary(
    name = "FileAccessAgent",
    deploy_manifest_lines = [
        "Premain-Class: com.file_access_agent.FileAccessAgent",
        "Can-Retransform-Classes: true",
    ],
    srcs = glob([
        "src/main/java/com/file_access_agent/*.java",
    ]),
    deps = [
        "@maven//:net_bytebuddy_byte_buddy",
        "//src/main/java/com/file_access_agent/transformer:transformers",
        "//src/main/java/com/file_access_agent/logger:loggerLib",
        "//src/main/java/com/file_access_agent/common/util/environment:util_environment_lib",
        "//:CopyLoggerBin",
        ],
    data = ["//src/main/java/com/file_access_agent/logger:loggerBin_deploy.jar"],
    resources = [
        "//:FileAccessResources",
    ],
)

filegroup (
    name = "FileAccessResources",
    srcs = [
        "file_access_agent.properties",
        "loggerBin.jar",
    ],
)

genrule (
    name = "CopyLoggerBin",
    srcs = ["//src/main/java/com/file_access_agent/logger:loggerBin_deploy.jar"],
    outs = ["loggerBin.jar"],
    cmd_bash = "cp $(location //src/main/java/com/file_access_agent/logger:loggerBin_deploy.jar) $(OUTS)",
)

