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
        ],
    data = ["//src/main/java/com/file_access_agent/logger:loggerBin_deploy.jar"],
)

