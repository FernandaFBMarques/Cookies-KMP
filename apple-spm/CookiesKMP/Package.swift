// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CookiesKMP",
    platforms: [
        .iOS(.v15),
        .macOS(.v11),
    ],
    products: [
        .library(
            name: "CookiesKMP",
            targets: ["CookiesKMP"],
        ),
    ],
    targets: [
        .binaryTarget(
            name: "CookiesKMP",
            // Update URL and checksum per release.
            url: "https://github.com/FernandaFBMarques/Cookies-KMP/releases/download/0.1.3/CookiesKMP.xcframework.zip",
            checksum: "cd0392369f25cb718d2fc9e77f9aa82465d490db47889b9244feebb6f5dd876e",
        ),
    ],
)

