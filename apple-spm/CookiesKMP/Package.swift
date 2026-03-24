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
            url: "https://github.com/FernandaFBMarques/Cookies-KMP/releases/download/0.1.4/CookiesKMP.xcframework.zip",
            checksum: "0f2792cb2a1bac2cf5cf44af9616b3ac3cee8bdc07a1ec634f3791c01846528f",
        ),
    ],
)

