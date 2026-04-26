# TAK.gov Official ATAK Plugin Development Guide

## Sources

- https://tak.gov/user_builds
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-coding-style-guide
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/creating-graphics-on-the-map
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/custom-cot-details
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/delete-remote-map-item
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/focus-on-mapitem-and-showing-radial-menu
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/import-api
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/io-abstraction-layer
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/listening-to-cot-messages
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/map-event-handling
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/map-projects-and-coordinate-transformations
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/radial-menu-api
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/release-build-info
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/sending-cot-messages-through-atak
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/using-an-emulator
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/spinners-in-plugins
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/working-with-native-code
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-system-requirements
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-ui-design-guide
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/build-system-and-runtime
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/plugin-compatibility
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-build-env-4-1
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-build-env-4-2-and-4-2-1
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-3-0-and-4-4-0-from-4-2-1-and-4-2-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-5-x-from-4-4-and-4-3
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-7-4-6-1-from-4-5-1
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-8-0-and-4-8-1-from-4-7-and-4-6-1
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-9-0-from-4-8-1
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-4-10-0-from-4-9-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-0-0-from-4-10-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-1-0-from-5-0-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-2-0-from-5-1-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-3-0-from-5-2-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-4-0-from-5-3-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-5-0-from-5-4-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-6-0-from-5-5-0
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/file-formats-supported
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/mil-vs-civ-atak-diffs
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/disabling-hiding-preferences
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/testing-atak-plugins
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/debugging-atak-plugins
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/cross-products-guides
- https://tak.gov/documentation/resources/tak-developers/developer-documentation/cross-products-guides/data-package-format
- https://tak.gov/documentation/resources/tak-developers/code-deprecation-policy

## Third Party Pipeline Requirements

Source: https://tak.gov/user_builds

### Source Archive Requirements

Welcome to the TAK third party pipeline, an ephemeral build service which also signs third-party plugins, enabling operational baseline functionality. Information submitted though the third-party signing service is not monitored nor is it stored by the government. A visual indicator informs the end user that a plugin was signed using the third-party signing service rather than the official TAK Product Center build pipeline.

The TAK Configuration Steering Board has open sourced ATAK-CIV and the Standard SDK on DoD GitHub and approved the public release of additional binaries on TAK.gov to support IRAD development. Access to these resources does not imply development support or operational transition.

For the build to succeed in this pipeline, the source code submission must meet the requirements that follow. For source code based upon a recent plugintemplate clone, the Gradle and Gradle plugin requirements have already been met.

- The source code must be provided in a zip archive, with a single root folder at its root. The root folder name will be used as the name for all APKs that are built by the service.
- Gradle must be the source code build system. Gradle scripts and associated folder must be part of the source archive, located at the root folder described below.
- The Gradle target, assembleCivRelease, must be defined.
- The TPC gradle plugin, atak-gradle-takdev, must be used for all references to the ATAK SDK. If there are libraries within the source archive that have ATAK SDK dependencies, those too must utilize the gradle plugin to resolve required artifacts. This plugin fetches the SDK from the TPC hosted maven repository, and the source will be compiled using these libraries.
- For plugin versions targeting ATAK version 4.2 and beyond, atak-gradle-tak-dev must be the latest 2.x version available. A Maven range expression of 2.+ satisfies this requirement.
- Plugins target prior to ATAK version 4.2 should use atak-gradle-takdev version must use the latest 1.x available. A Maven range expression of 1.+ satisfies this requirement.

### Verification Command

Plugins should be verified prior to submission with the USG developer's tak.gov credentials. For example, the command:

\`\`\`
./gradlew -Ptakrepo.force=true -Ptakrepo.url=https://artifacts.tak.gov/artifactory/maven -Ptakrepo.user=<user> -Ptakrepo.password=<pass> assembleCivRelease
\`\`\`

should build successfully where <user> and <pass> are the developer's artifacts.tak.gov credentials. If this command or an equivalent fails, then the build pipeline too will fail. When seeking TPC support, the output of the example command will be one of the first questions asked when supporting build issues.

NOTE: access to artifacts.tak.gov is reserved for USG Federal and Military personnel at this time.

### Proguard Configuration

The proguard-gradle entry:
\`\`\`
-repackageclasses atakplugin.PluginTemplate
\`\`\`
PluginTemplate text should be replaced with a descriptor of your specific plugin. This helps with crash log identification.

### AndroidManifest Discovery Entry

The AndroidManifest.xml must contain the below entry in order to be discoverable by ATAK:

\`\`\`xml
<activity android:name="com.atakmap.app.component" tools:ignore="MissingClass">
    <intent-filter android:label="@string/app_name">
        <action android:name="com.atakmap.app.component" />
    </intent-filter>
</activity>
\`\`\`

### FAQ - Build Machine Configuration

**What JDK versions are installed on the Third Party Pipeline build machine?**
Gradle version 6.9.1 and jdk 17 for our ATAK plugin baseline. The plugin template project, https://github.com/deptofdefense/AndroidTacticalAssaultKit-CIV/tree/master/plugin-examples/plugintemplate, is the primarily supported configuration.

**What NDK Versions are installed on the Third Party Pipeline build machine?**
The following versions of the NDK are installed:
- 12.1.2977051
- 21.0.6113669
- 21.4.7075529
- 23.0.7599858
- 25.1.8937393

The TPP does not allow for the installation of the NDK version declared in build.gradle at build time; the plugin MUST use one of the pre-installed versions.

**What documentation is available?**
The ATAK Development documentation (https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development) can be referenced for resources and changes to the build environment across ATAK versions.

## SDK and API Requirements

### ATAK System Requirements

Source: https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-system-requirements

- **Operating System:** ATAK requires Android 5.0 (API 21) or later.
- **Hardware:** ATAK does not require specific hardware and should run on any Android device that supports the other System Requirements.
- **Graphics:** ATAK requires a graphics processor that supports GLES 3.0.
- **Storage, Memory and Processor:** ATAK does not have any specific requirements for storage, memory or processor, however, performance of the application will depend on the configuration.
- **Examples of Devices:** Testing of TAK is typically conducted on various Samsung devices since they are the most commonly deployed. However, TAK is compatible with other non-Samsung devices as well. For small phone-sized devices, hardware equivalent to a Samsung S5 or newer is required. For tablet-sized devices, the Samsung Tab S or newer is recommended. To ensure an optimal experience, it's advised to use at least Samsung S9-equivalent hardware or newer for phones, and a Samsung Tab S2 or newer for tablets.

### Build System and Runtime (history)

Source: https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/build-system-and-runtime

Changes to the build system or the runtime platform are governed by the Deprecation of the Build System and ABI/API Changes section of the TAK.gov Code Deprecation Policy.

| Version | Build System | Android SDK | NDK | Support Libraries |
|---------|--------------|-------------|-----|-------------------|
| 4.1 | Gradle (Android Plugin 3.6.3) | Target 29 / Min 21 | r12b | androidx.fragment:fragment:1.2.4, androidx.exifinterface:exifinterface:1.2.0, androidx.localbroadcastmanager:localbroadcastmanager:1.0.0 |
| 4.0 | Gradle (Android Plugin 3.5.3) | Target 26 / Min 21 | r12b | com.android.support:support-v4:26.1.0, com.android.support:exifinterface-v4:26.1.0 |
| 3.13 | Gradle (Android Plugin 3.5.2) | Min 21 | r12b | com.android.support:support-v4:26.0.0 |
| 3.12 | Gradle | Min 21 | r12b | com.android.support:support-v4:26.0.0 |
| 3.11 | Gradle (Android Plugin 3.3.2) | Min 21 | r12b | com.android.support:support-v4:26.0.0 |
| 3.10 | Gradle (Android Plugin 3.2.1) | Min 21 | r12b | (similar) |

### Plugin Compatibility

Source: https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/plugin-compatibility

There are several factors that make a plugin and the ATAK runtime compatible. All factors must be satisfied in order for a plugin to load in ATAK.

**Compatibility Factors:**

1. **API Runtime Version** - Both the plugin and the ATAK runtime must share the same API version (e.g. 4.1.0). The API version for the plugin is specified at the top via the variable ATAK_VERSION.

2. **Variant** - The variant (e.g. MIL, CIV, etc.) also referred to as flavor of the plugin affects which variant(s) of ATAK the plugin may be runtime compatible with. Starting with 4.2.0, plugins that are built for CIV may run in any variant of ATAK; plugins built for a specific variant may only run in that variant. Prior to 4.2, plugins were only allowed to run in the variant of ATAK that they were built for, including CIV. The variant is automatically set by the Build Variant used during the gradle build process. The plugin template build.gradle supports the various ATAK variants through the defined product flavors.

3. **Obfuscation** - The plugin and ATAK application must share the same source code obfuscation mapping; all release builds of the ATAK application employ source code obfuscation. Plugins built in the development environment are typically only obfuscation compatible with SDK builds of the ATAK application.

4. **Signing Keys** - Release builds of ATAK enforce signing key whitelisting. Only plugins built with specific signing keys will be loaded by ATAK. SDK builds of ATAK prior to the 4.1 feature freeze build also enforced signing key whitelisting. The signing key that is distributed as part of the SDK is on that whitelist. SDK builds coming after 4.1 feature freeze will not enforce any signing key whitelisting.

**Troubleshooting:**

The ATAK Package Mgmt tool will provide some information about plugin compatibility, and will only allow compatible plugins to be loaded. To get information about the installed plugin, click the Details button on that plugin row. The displayed screen will provide more information on why the plugin is not compatible. To get information about the ATAK runtime, go to Menu (...) > About. The screen will display the Plugin API that's available from the runtime. This must match the Plugin API that the plugin is targeting.

> **The ATAK Package Mgmt tool shows my plugin as compatible, but it still fails to load.**
> The cause is likely to be that you are trying to load a debug version of your plugin in a release version of ATAK. See the requirement about Obfuscation as a compatibility factor.

**Other Build Systems:**
The API version and variant are ultimately compiled into AndroidManifest.xml as the meta-data string "plugin-api" and takes the form "com.atakmap.app@<version>.<variant>" where version is the desired version of the ATAK runtime and variant is the build variant of ATAK (e.g. MIL or CIV). An example of the fully formed string looks like:
\`\`\`
com.atakmap.app@4.1.0.MIL
\`\`\`
Which means that the plugin is targeting ATAK MIL 4.1.0.

## Gradle Configuration

### Latest (ATAK 5.6.0)

Source: https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/changes-to-the-build-environment-for-5-6-0-from-5-5-0

| Item | Version | Notes |
|------|---------|-------|
| minSdkVersion | 21 | ATAK Core is set up so the minimum Android version is Android 5.0 (21) but this does not restrict plugins from targeting a newer version of Android. |
| compileSdk | 36 | |
| targetSdkVersion | 36 | |
| atak-gradle-takdev | 3.+ | Note: If you are fixing on a version of the version of atak-gradle-takdev and not using 3.+, you need to verify it is version 3.5.3 or newer. |
| Gradle | 8.14.3 | |
| Android Gradle Plugin | 8.13.0 | |
| Java | 17 | The source and target compatibility is still 1.8; however, the build environment must make use of https://adoptium.net/ JDK 17. |
| NDK | 27.3.13750724 | The full version is shipped with an abiFilter of { "armeabi-v7a", "arm64-v8a", "x86", "x86_64" }. If your plugin makes use of native libraries we REQUIRE shipping at least "arm64-v8a". |
| *Kotlin | 2.2.0 | Note that core does not make use of Kotlin and this is brought in due to dependencies on AndroidX. |

### Explicit Library Changes (ATAK 5.6.0)

| Library | Version |
|---------|---------|
| androidx.fragment:fragment | 1.8.9 |
| androidx.exifinterface:exifinterface | 1.4.1 |
| androidx.localbroadcastmanager:localbroadcastmanager | 1.1.0 |
| androidx.lifecycle:lifecycle-process | 2.9.4 |
| org.greenrobot:eventbus | 3.2.0 |
| com.squareup.okhttp3:okhttp | 4.11.0 |
| org.jetbrains.kotlin:kotlin-reflect | 2.2.0 |

### Resolution Strategy (5.6.0)

\`\`\`gradle
configurations.all {
    resolutionStrategy.eachDependency { details ->
        if (details.requested.group == 'androidx.core' !details.requested.name.contains('core-viewtree')) {
            details.useVersion '1.17.0'
        }
        if (details.requested.group == 'androidx.lifecycle') {
            details.useVersion '2.9.4'
        }
        if (details.requested.group == 'androidx.fragment') {
            details.useVersion '1.8.9'
        }
        if (details.requested.group == 'androidx.lifecycle') {
            details.useVersion "2.9.4"
        }
        if (details.requested.group == 'androidx.fragment') {
            details.useVersion "1.8.9"
        }
        if (details.requested.group == 'com.squareup.okhttp3') {
            details.useVersion "4.11.0"
        }
        if (details.requested.group == 'com.squareup.okio') {
            details.useVersion "3.2.0"
        }
        if (requested.group == 'org.jetbrains.kotlinx' requested.name == 'kotlinx-serialization-core') {
            details.useVersion "1.7.3"
        }
    }
}

configurations.implementation {
    exclude group: 'androidx.core', module: 'core-ktx'
    exclude group: 'androidx.core', module: 'core'
    exclude group: 'androidx.fragment', module: 'fragment'
    exclude group: 'androidx.lifecycle', module: 'lifecycle'
    exclude group: 'androidx.lifecycle', module: 'lifecycle-process'
    exclude group: 'com.squareup.okhttp3', module: 'okhttp'
    exclude group: 'com.squareup.okio', module: 'okio'
    exclude group: 'org.jetbrains.kotlinx', module: 'kotlinx-serialization-core'
}
\`\`\`

### gradle.properties (Required)

The gradle.properties files is required to have:
\`\`\`
# required for app bundles that utilize native libraries
android.bundle.enableUncompressedNativeLibs=false
\`\`\`

### RecyclerView Exclusions

For 1st order libraries, you may need to do additional work to exclude out any dependencies supplied by ATAK. For example if you make use of RecyclerView:

\`\`\`gradle
// Recyclerview version depends on some androidx libraries which
// are supplied by core, so they should be excluded. Otherwise
// bad things happen in the release builds after proguarding
implementation ('androidx.recyclerview:recyclerview:1.1.0') {
    exclude module: 'collection'
    // this is an example of a local exclude since androidx.core is supplied by core atak
    exclude module: 'core'
    exclude module: 'lifecycle'
    exclude module: 'core-common'
    exclude module: 'collection'
    exclude module: 'customview'
}
\`\`\`

### Apache HTTP Legacy Libraries

\`\`\`gradle
android {
    compileSdkVersion XX
    buildToolsVersion "XX.X.X"
    useLibrary 'org.apache.http.legacy'
}
\`\`\`

### Transparent Signing (AAB Support)

For developers who will need an AAB file capable of being turned into an APK that can load into ATAK, they will need to create the following file:

**File path:** \`app/src/main/res/xml/com_android_vending_archive_opt_out.xml\`

**Contents:**
\`\`\`xml
<?xml version="1.0" encoding="utf-8"?>
<optOut />
\`\`\`

## AndroidManifest Requirements

### Plugin Discovery Activity (Required)

As of ATAK 4.6, it is required that all plugins have the following fictitious Activity in the AndroidManifest.xml file to support plugin discovery. This is already included in the PluginTemplate:

\`\`\`xml
<activity android:name="com.atakmap.app.component" tools:ignore="MissingClass">
    <intent-filter android:label="@string/app_name">
        <action android:name="com.atakmap.app.component" />
    </intent-filter>
</activity>
\`\`\`

### As of ATAK 5.2.0+ (with android:exported)

\`\`\`xml
<activity android:name="com.atakmap.app.component"
          android:exported="true"
          tools:ignore="MissingClass">
    <intent-filter android:label="@string/app_name">
        <action android:name="com.atakmap.app.component" />
    </intent-filter>
</activity>
\`\`\`

### Native Libraries

If your plugin uses Native Libraries and set the Android SDK Target to 26 or newer (e.g. minSdkVersion 26) Then you will need to make sure your plugin AndroidManifest.xml application block contains:
\`\`\`xml
android:extractNativeLibs="true"
\`\`\`

### plugin-api Meta-data

The API version and variant are ultimately compiled into AndroidManifest.xml as the meta-data string "plugin-api" and takes the form:
\`\`\`
com.atakmap.app@<version>.<variant>
\`\`\`

For example: \`com.atakmap.app@4.1.0.MIL\` means that the plugin is targeting ATAK MIL 4.1.0.

### Package Discovery / QUERY_ALL_PACKAGES

Package discover is no longer allowed by normal applications unless there is a relationship between Android 11 and forward. The QUERY_ALL_PACKAGES permission has been completely removed from the core application and would require your associated apps to declare the faux query entry to allow for the ATAK application to find them. This is required for plugins as well as any apps that the plugins need to be able to find. For example if you have a service APK that is required by the plugin, the easiest way is to have your service APK include the same block in their Android Manifest:

\`\`\`xml
<!-- allow for app discovery -->
<activity android:name="com.atakmap.app.component"
          android:exported="true"
          tools:ignore="MissingClass">
    <intent-filter android:label="@string/app_name">
        <action android:name="com.atakmap.app.component" />
    </intent-filter>
</activity>
\`\`\`

### Context::registerReceiver Pattern (Critical, Android 14+)

Specifically the change to Context::registerReceiver which now must take a 3rd parameter if used in the plugin (unless used in a service or activity within the plugin). Please note that plugins should make use of AtakBroadcast instead of directly registering a system or application broadcast receiver. If you must use Context::registerReceiver please make use of this pattern in your code:

\`\`\`java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED);
} else {
    context.registerReceiver(receiver, filter);
}
\`\`\`

If you do not do this, you will receive crash reports that look like this:
\`\`\`
Exception java.lang.SecurityException:
  at android.os.Parcel.createExceptionOrNull (Parcel.java:3069)
  at android.os.Parcel.createException (Parcel.java:3053)
  at android.os.Parcel.readException (Parcel.java:3036)
  at android.os.Parcel.readException (Parcel.java:2978)
  at android.app.IActivityManager$Stub$Proxy.registerReceiverWithFeature (IActivityManager.java:6137)
  at android.app.ContextImpl.registerReceiverInternal (ContextImpl.java:1913)
  at android.app.ContextImpl.registerReceiver (ContextImpl.java:1853)
  at android.app.ContextImpl.registerReceiver (ContextImpl.java:1841)
  at android.content.ContextWrapper.registerReceiver (ContextWrapper.java:772)
\`\`\`

## Plugin Lifecycle Classes

(Plugins use AbstractPlugin, MapComponent, Tool, DropDown patterns. The official documentation defines requirements via the PluginTemplate located at https://git.tak.gov/samples/plugintemplate. Migration of legacy plugins is referenced via https://git.tak.gov/samples/PluginTemplateLegacy specifically https://git.tak.gov/samples/PluginTemplateLegacy/-/commit/6307d188e175b28a326a5e400824f67e9daba8e2)

ATAK Packages migration note (from 4.8 docs): "ATAK will be removing all core supplied transapp libraries for ATAK 4.9. If you have not already migrated away from using the transapp libraries for your Lifecycles and Tools, you will need to look at the PluginTemplate and Hellworld plugin examples. If you do not complete this migration, your plugin will fail to compile. This library was slated for removal as far back at 4.5."

### MapComponent Lifecycle (from Custom CoT Details example)

Once a class is developed (e.g. detail handler, tool registration), register it with the appropriate manager. Usually this is done in the MapComponent::onCreate method:

\`\`\`java
CotDetailManager.getInstance().registerHandler(
    "__special",
    sdh = new SpecialDetailHandler());
\`\`\`

When a plugin is unloaded or the program is stopped, the handler should be unregistered in the MapComponent::onDestroy method:

\`\`\`java
CotDetailManager.getInstance().unregisterHandler(sdh);
\`\`\`

### IO Abstraction Lifecycle Pattern

Implementors providing a custom IO abstraction layer should register their providers in the plugin's onCreate method (either on Lifecycle or MapComponent implementations, depending on plugin architecture). Implementors should ensure that they perform proper deregistration if the plugin's onDestroy method is invoked during the runtime of the application.

### MapMenuFactory / Radial Menu (From Radial Menu API)

Developers can add and remove their factories through the MapMenuReceiver's registerMapMenuFactory and unregisterMapMenuFactory methods. Factories are provided the MapItem instance associated with the menu action, and a MapMenuWidget is expected in return.

The default factory behavior can be replicated using the MenuResourceFactory class which implements the MapMenuFactory interface:

\`\`\`java
final MapView mapView = MapView.getMapView();
// using application, not plugin, assets, hence the application context
final MapAssets mapAssets = new MapAssets(mapView.getContext());
final MenuMapAdapter adapter = new MenuMapAdapter();
try {
    adapter.loadMenuFilters(mapAssets, "filters/menu_filters.xml");
} catch (IOException e) {
    Log.w(TAG, e);
}
MenuResourceFactory factory = new MenuResourceFactory(mapView, mapView.getMapData(), mapAssets, adapter);
\`\`\`

## ProGuard Requirements

### Minimum Versions by ATAK Release

- ATAK 4.8.x and 4.9.0: minimum proguard 7.2.2
- ATAK 5.0.0: minimum proguard 7.3.2
- ATAK 5.1.0+: minimum proguard 7.4.1

### Repackage Class Naming Rule

The proguard-gradle entry:
\`\`\`
-repackageclasses atakplugin.PluginTemplate
\`\`\`
PluginTemplate text should be replaced with a descriptor of your specific plugin. This helps with crash log identification.

For example: \`-repackageclasses atakplugin.MyCoolPlugin\`

As of atak-gradle-takdev version 2.5.1, linting is performed to make sure that your plugin will conform to certain static rules. One such rule is the proguard obfuscation naming rule which requires that your plugin have an obfuscation name other than Plugin Template. The error message you will see is:

> Proguard configured for plugintemplate, but this plugin is not the plugintemplate.

The fix is to modify the repackageclasses line in the proguard-gradle.txt file. Note: if you do not have a repackageclasses entry in your proguard-gradle.txt, you do not need to add one.

### Required dontwarn Directives

Release building might require additional dontwarn directives at the bottom of the proguard file if you have correctly excluded things from the plugin that are supplied by core.

\`\`\`
-dontwarn androidx.**
-dontwarn kotlin.**
-dontwarn kotlinx.**
\`\`\`

### Release Build Information (Critical)

When developing a plugin for ATAK, it is encouraged that it be run a few time with obfuscation enabled. This can be done by running:
\`\`\`
./gradlew assemble<Flavor>Release
\`\`\`

The result of this is a plugin that is itself obfuscated but can only be used against a developer version of ATAK.

1. Any use of reflection needs to be accounted for in the proguard-gradle.txt file.
2. Use of Lambdas for inner interfaces within ATAK are known to break during the obfuscation process.
3. Any use of gson or other mechanisms to persist or serialize information needs to be examined and accounted for in the proguard-gradle.txt file.

## Signing Requirements

### Plugin Compatibility - Signing Keys

Release builds of ATAK enforce signing key whitelisting. Only plugins built with specific signing keys will be loaded by ATAK.

SDK builds of ATAK prior to the 4.1 feature freeze build also enforced signing key whitelisting. The signing key that is distributed as part of the SDK is on that whitelist. SDK builds coming after 4.1 feature freeze will not enforce any signing key whitelisting.

### Third Party Pipeline Signing

The TAK Third Party Pipeline (https://tak.gov/user_builds) is an ephemeral build service which also signs third-party plugins, enabling operational baseline functionality. Information submitted through the third-party signing service is not monitored nor is it stored by the government. A visual indicator informs the end user that a plugin was signed using the third-party signing service rather than the official TAK Product Center build pipeline.

## Submission Requirements

Source: https://tak.gov/user_builds

- The source code must be provided in a zip archive, with a single root folder at its root. The root folder name will be used as the name for all APKs that are built by the service.
- Gradle must be the source code build system. Gradle scripts and associated folder must be part of the source archive.
- The Gradle target, **assembleCivRelease**, must be defined.
- The TPC gradle plugin, **atak-gradle-takdev**, must be used for all references to the ATAK SDK. Plugin versions targeting ATAK version 4.2 and beyond must use the latest 2.x version available (Maven range expression: 2.+). Plugins prior to ATAK version 4.2 should use the latest 1.x available (Maven range expression: 1.+).
- The plugin MUST use one of the pre-installed NDK versions (12.1.2977051, 21.0.6113669, 21.4.7075529, 23.0.7599858, 25.1.8937393).
- AndroidManifest.xml must contain the plugin discovery activity entry.
- proguard-gradle entry must replace PluginTemplate with a unique plugin descriptor.

### Submission UI (TAK.gov user_builds)

Submit your plugin by dragging and dropping files in the area provided or use the button to select your files. The page tracks: File Name, Uploaded date, Status (Success/Failed/Queued), and provides Actions (download).

## Version Compatibility

### Build Environment Compatibility Across ATAK Versions

This section consolidates the build environment requirements for each ATAK release.

#### ATAK 4.1
- Minimum Android version: Android 5.0 (21)
- Gradle 6.3 / Android Gradle 3.6.3 (R8 disabled)
- compileSdkVersion 28
- AndroidX migration from com.android.support
- androidx.fragment:fragment:1.2.4
- androidx.exifinterface:exifinterface:1.0.0
- androidx.localbroadcastmanager:localbroadcastmanager:1.0.0
- Native libraries require android:extractNativeLibs="true"

#### ATAK 4.2.0 / 4.2.1
- SDK shipped only in Civilian form. atak-mil-flavor.apk plugin enables the military SDK.
- Gradle 6.6.1 / Android Gradle 4.0.1 (R8 disabled)
- compileSdkVersion 29
- androidx.fragment:fragment:1.2.5
- androidx.exifinterface:exifinterface:1.3.0

#### ATAK 4.3.0 / 4.4.0
- Gradle 6.8.1-all / Android Gradle 4.0.2 (R8 disabled)
- compileSdkVersion 29
- androidx.fragment:fragment:1.3.0
- androidx.exifinterface:exifinterface:1.3.2

#### ATAK 4.5.X
- Gradle 6.9.1-all / Android Gradle 4.2.2 (R8 disabled)
- compileSdkVersion 30 / targetSdkVersion 30
- androidx.fragment:fragment:1.3.6
- androidx.exifinterface:exifinterface:1.3.3
- androidx.lifecycle:lifecycle-process:2.3.1
- Transient dependency: androidx.core:core:1.2.0

#### ATAK 4.7 / 4.6.1
- Gradle 6.9.1-all / Android Gradle 4.2.2 (R8 disabled)
- compileSdkVersion 30 / targetSdkVersion 30
- JDK: Adoptium JDK 8 or 11
- Source/target compatibility: 1.8
- Plugin discovery activity required (as of 4.6)

#### ATAK 4.8.0 / 4.8.1
- Gradle 6.9.2 (4.8.0) / 7.5.1 (4.8.1) / Android Gradle 4.2.2 (R8 disabled)
- compileSdkVersion 32 / targetSdkVersion 31
- Proguard 7.2.2 minimum
- Adoptium JDK 11
- NDK 23.1.7779620
- androidx.fragment:fragment:1.5.3
- androidx.exifinterface:exifinterface:1.3.4
- androidx.lifecycle:lifecycle-process:2.5.1
- transapp libraries removed (must migrate)

#### ATAK 4.9.0
- Gradle 7.5.1 / Android Gradle 4.2.2 (R8 disabled)
- compileSdkVersion 32 / targetSdkVersion 32
- Proguard 7.2.2 minimum
- Adoptium JDK 11
- NDK 25.1.8937393
- androidx.fragment:fragment:1.5.5
- androidx.exifinterface:exifinterface:1.3.5
- org.greenrobot:eventbus:3.2.0 (from TAK Kernel)
- gradle.properties: android.bundle.enableUncompressedNativeLibs=false

#### ATAK 4.10.0
- Gradle 7.5.1 / Android Gradle 7.4.2
- compileSdkVersion 33 / targetSdkVersion 33
- Proguard 7.2.2 minimum
- Adoptium JDK 11
- NDK 25.1.8937393
- androidx.fragment:fragment:1.5.7
- androidx.exifinterface:exifinterface:1.3.6
- androidx.lifecycle:lifecycle-process:2.6.1
- Kotlin 1.8.20

#### ATAK 5.0.0
- Gradle 7.5.1 / Android Gradle 7.4.2
- compileSdkVersion 33 / targetSdkVersion 33
- Proguard 7.3.2 minimum
- Adoptium JDK 11
- NDK 25.1.8937393
- androidx.fragment:fragment:1.6.1
- androidx.lifecycle:lifecycle-process:2.6.2
- org.jetbrains.kotlin:kotlin-reflect:1.8.22
- Kotlin 1.8.22

#### ATAK 5.1.0
- Gradle 7.5.1 / Android Gradle Plugin 7.4.2
- compileSdkVersion 34 / targetSdkVersion 34
- Proguard 7.4.1 minimum
- Adoptium JDK 11
- NDK 25.1.8937393 (REQUIRE shipping at least "arm64-v8a" if using native libs)
- androidx.fragment:fragment:1.6.2
- androidx.exifinterface:exifinterface:1.3.7
- Test harness: mockito-core:4.8.0 (replacing 3.12.4)
- Critical: Context::registerReceiver requires 3rd parameter for Android 14

#### ATAK 5.2.0
- Same as 5.1.0 but JDK 17 also acceptable
- compileSdkVersion 34 / targetSdkVersion 34
- AndroidManifest activity now requires android:exported="true"

#### ATAK 5.3.0
- Same as 5.2.0
- compileSdkVersion 34 / targetSdkVersion 34

#### ATAK 5.4.0
- Gradle 8.13 / Android Gradle Plugin 8.9.0
- compileSdkVersion 35 / targetSdkVersion 34
- Adoptium JDK 17 required
- NDK 25.1.8937393
- androidx.fragment:fragment:1.8.5
- androidx.lifecycle:lifecycle-process:2.8.7
- org.jetbrains.kotlin:kotlin-reflect:2.1.10
- Kotlin 2.1.10
- com.squareup.okhttp3:okhttp:4.11.0 (NEW)
- mockito-core:5.15.2 (PowerMockito removed)
- Introduction of okhttp and okio into the core

#### ATAK 5.5.0
- Gradle 8.13 / Android Gradle Plugin 8.9.0
- compileSdkVersion 35 / targetSdkVersion 35
- atak-gradle-takdev 3.5.3 minimum (or 3.+)
- JDK 17
- NDK 25.1.8937393
- androidx.fragment:fragment:1.8.7
- androidx.exifinterface:exifinterface:1.4.1
- androidx.lifecycle:lifecycle-process:2.9.0

#### ATAK 5.6.0
- Gradle 8.14.3 / Android Gradle Plugin 8.13.0
- compileSdk 36 / targetSdkVersion 36
- atak-gradle-takdev 3.+ (3.5.3 minimum)
- JDK 17
- NDK 27.3.13750724
- Kotlin 2.2.0
- androidx.fragment:fragment:1.8.9
- androidx.lifecycle:lifecycle-process:2.9.4

## Official Sample Code

### Creating Graphics on the Map

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/creating-graphics-on-the-map

The API includes a basic set of map graphics in the package, com.atakmap.android.maps. These include:
- Arrow (line with arrowhead)
- Ellipse
- Marker (symbols)
- Polyline (polyline and polygon)
- Rectangle

These map graphics objects extend from the base class, com.atakmap.android.maps.MapItem. The base class includes some defined metadata (e.g. UID, type, title) and allows for custom attribution (see MapItem.setMetaXXX(String, ...) and MapItem.getMetaXXX(String, ...)).

MapItem instances are held by the container object, com.atakmap.android.maps.MapGroup. A MapItem may only be a member of one MapGroup at any given time. MapGroup facilitates a hierarchical storage of MapItems; note that MapGroup does not allow for cycles. A default implementation, com.atakmap.android.maps.DefaultMapGroup is provided.

The root MapGroup is obtained via:
\`\`\`java
MapView mv = ...
MapGroup rootGroup = mv.getRootMapGroup();
\`\`\`

**Creating a MapGroup by Name:**
\`\`\`java
// obtain the root MapGroup
MapView mv = ...
MapGroup rootGroup = mv.getRootMapGroup();

String myMapGroupName = ...
// add a new MapGroup to the root with the specified name
MapGroup myMapGroup = rootGroup.addGroup(myMapGroupName);
\`\`\`

**Adding a Custom MapGroup, using the Default Implementation:**
\`\`\`java
// obtain the root MapGroup
MapView mv = ...
MapGroup rootGroup = mv.getRootMapGroup();

// create a new DefaultMapGroup
String myMapGroupName = ...
MapGroup myMapGroup = new DefaultMapGroup(myMapGroupName);
// configure the map group, e.g. add items
...
// add the MapGroup to the root MapGroup
myMapGroup = rootGroup.addGroup(myMapGroup);
\`\`\`

**Adding a Marker to the Map:**
\`\`\`java
// obtain the MapGroup to add the graphic to
MapGroup myMapGroup = ...

// create the marker. Give it a location and UID
// If the UID is not previously known (e.g. coming from an external source), use
// UUID.randomUUID().toString()
String uid = ...
// the location for the marker
GeoPoint location = ...
Marker myMarker = new Marker(location, uid);

// icons are referenced via URI. This allows ATAK finer memory management
// control. File based URIs are the simplest representation
String symbolUri = ...
Icon myIcon = new Icon(symbolUri);
myMarker.setIcon(myIcon);

// set the title for the marker
myMarker.setTitle(...);

// add the marker to the MapGroup
myMapGroup.addItem(myMarker);
\`\`\`

**Adding a Marker using PlacePointTool:**
\`\`\`java
MapGroup myMapGroup = ...
String uid = ...
GeoPoint location = ...
String callsign = ...
String type = ...

PlacePointTool.MarkerCreator mc = new PlacePointTool.MarkerCreator(location);
mc.setUid(uid);
mc.setCallsign(callsign);
mc.setType(type);
Marker myMarker = mc.placePoint();
myMapGroup.addItem(myMarker);
\`\`\`

The PlacePointTool (com.atakmap.android.user.PlacePointTool) is a utility that can be used to build a Marker Object. It offers some specific methods for creating Markers that are Cursor-on-Target (CoT) compatible. The icon used for the Marker will be automatically determined based on the assigned type (e.g. "a-f-G" is friendly ground unit, "a-h-G" is hostile ground unit, etc.).

**Adding a Polyline:**
\`\`\`java
MapGroup myMapGroup = ...
String uid = ...
Polyline myPolyline = new Polyline(uid);

GeoPoint[] points = ...
myPolyline.setPoints(points);

int strokeColor = ...
myPolyline.setStrokeColor(strokeColor);

float strokeWidth = ...
myPolyline.setStrokeWidth(strokeWidth);

myMapGroup.addItem(myPolyline);
\`\`\`

### Custom CoT Details

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/custom-cot-details

ATAK Allows for custom CoT details to be handled through the plugin architecture. A working example of this can be seen in the helloworld plugin. For this example the custom detail is __special. Please note that this is following the CoT guidance that non-standard or not yet standard CoT detail tags be prefaced with a double underscore.

\`\`\`java
public class SpecialDetailHandler implements MarkerDetailHandler {

    @Override
    public void toCotDetail(final Marker marker, final CotDetail detail) {
        Log.d(TAG, "converting to: " + detail);
        CotDetail special = new CotDetail("__special");
        special.setAttribute("count",
            String.valueOf(marker.getMetaInteger("special.count", 0)));
        detail.addChild(special);
    }

    @Override
    public void toMarkerMetadata(final Marker marker, CotEvent event, CotDetail detail) {
        Log.d(TAG, "detail received: " + detail + " in: " + event);
        marker.setMetaInteger("special.count",
            getInt(detail.getAttribute("count"), 0));
    }
}
\`\`\`

It is suggested that these are 1:1 allowing for a lossless round trip of data to and from the marker.

Register in MapComponent::onCreate:
\`\`\`java
CotDetailManager.getInstance().registerHandler(
    "__special",
    sdh = new SpecialDetailHandler());
\`\`\`

Unregister in MapComponent::onDestroy:
\`\`\`java
CotDetailManager.getInstance().unregisterHandler(sdh);
\`\`\`

### Delete a Remote Map Item

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/delete-remote-map-item

\`\`\`xml
<?xml version="1.0" standalone="yes"?>
<event start="2012-01-01T00:00:00Z" time="2012-01-01T00:00:00Z" stale="2020-01-01T00:00:00Z" how="m-g" type="t-x-d-d" uid="does-not-matter-must-be-unique" version="2.0">
    <detail>
        <link uid="the-uid-of-the-map-item-to-remove" relation="none" type="none"/>
        <__forcedelete/>
    </detail>
    <point ce="9999999" le="9999999" lat="36.789783" lon="-115.471535" hae="4433.086151"/>
</event>
\`\`\`

### Focus on a MapItem and Show Radial Menu

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/focus-on-mapitem-and-showing-radial-menu

\`\`\`java
MapItem myItem = ...
String uid = myItem.getUID();

// set up the Intent to "focus" on the map item
Intent focusIntent = new Intent();
focusIntent.setAction("com.atakmap.android.maps.FOCUS");
focusIntent.putExtra("uid", uid);
AtakBroadcast.getInstance().sendBroadcast(focusIntent);

// set up the Intent to show the radial menu
Intent menuIntent = new Intent();
menuIntent.setAction("com.atakmap.android.maps.SHOW_MENU");
menuIntent.putExtra("uid", uid);
AtakBroadcast.getInstance().sendBroadcast(menuIntent);
\`\`\`

### Import API

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/import-api

The Intent action, ImportReceiver.ACTION_IMPORT_DATA, is defined for the purpose of importing data into ATAK.

**Extras:**
| Extra | Required | Description |
|-------|----------|-------------|
| ImportReceiver.EXTRA_CONTENT | No | The type of the content to be imported. |
| ImportReceiver.EXTRA_MIME_TYPE | No | The MIME type of the content to be imported. |
| ImportReceiver.EXTRA_URI | Yes | The URI of the data to be imported. Either EXTRA_URI or EXTRA_URI_LIST must be specified. |
| ImportReceiver.EXTRA_URI_LIST | Yes | The URI of the data to be imported. |
| ImportReceiver.EXTRA_SHOW_NOTIFICATIONS | No | Boolean. Defaults to false. |
| ImportReceiver.EXTRA_ZOOM_TO_FILE | No | Boolean. Defaults to false. |
| ImportReceiver.ADVANCED_OPTIONS | No | Reserved. |

**Basic Import Intent Example:**
\`\`\`java
File f = ...
Intent i = new Intent(ImportExportMapComponent.ACTION_IMPORT_DATA);
i.putExtra(ImportReveiver.EXTRA_URI, Uri.fromFile(f).toString());
i.putExtra(ImportReceiver.EXTRA_ADVANCED_OPTIONS, true);
// no content or MIME type is specified, ATAK will auto-detect
AtakBroadcast.getInstance().sendBroadcast(i);
\`\`\`

**Registering a Custom Importer:**
- ImporterManager.registerImporter(...) - Registers your Importer implementation.
- MarshalManager.registerMarshal(...) - Registers your Marshal implementation.
- ImportExportMapComponent.getInstance().addImporterClasses(ImportInPlaceResolver.fromMarshal(...)) - "in place" import.
- ImportFilesTask.registerExtension(...) - Note: argument should be prefixed with the period ('.') character.

### IO Abstraction Layer

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/io-abstraction-layer

The IO Abstraction Layer is an API feature that allows plugin developers to manage filesystem and database interactions within the application layer. The primary intent for this feature is to allow for the integration of encryption services.

**IOProvider:**
The IOProvider interface providers two services: filesystem abstraction and SQLite database construction abstraction. An IOProvider instance is injected into the application via the static class, IOProviderFactory. The method, IOProviderFactory.registerProvider(...), registers the specified provider. The provider may be unregistered later via the method, IOProviderFactory.unregisterProvider(...).

The SQLite database implementation that ships with ATAK includes the SpatiaLite (https://www.gaia-gis.it/fossil/libspatialite/index) extensions.

**Major Assumptions:**
- Only one provider may be activated at any time
- When the provider is changed, any stored state that is stored as a result of the previous provider is cleared and reloaded
- ATAK does not make distinctions between sensitive and non-sensitive data

### Listening to CoT Messages

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/listening-to-cot-messages

**Marker-Specific Detail Handler:**
\`\`\`java
CotDetailManager.getInstance().registerHandler(
    "__special",
    sdh = new SpecialDetailHandler());
// later:
CotDetailManager.getInstance().unregisterHandler(sdh);
\`\`\`

**General Detail Handler:**
\`\`\`java
CotDetailManager.getInstance().registerHandler(aaaDetailHandler = new CotDetailHandler("__aaa") {
    private final String TAG = "AAACotDetailHandler";

    @Override
    public CommsMapComponent.ImportResult toItemMetadata(MapItem item, CotEvent event, CotDetail detail) {
        Log.d(TAG, "detail received: " + detail + " in: " + event);
        return CommsMapComponent.ImportResult.SUCCESS;
    }

    @Override
    public boolean toCotDetail(MapItem item, CotEvent event, CotDetail root) {
        Log.d(TAG, "converting to cot detail from: " + item.getUID());
        return true;
    }
});
\`\`\`

**MarshalManager / ImportManager:**
\`\`\`java
AbstractCoTEventMarshal acem;
MarshalManager.registerMarshal(acem = new AbstractCotEventMarshal("MyCustomType") {
    @Override
    protected boolean accept(final CotEvent event) {
        return (event.getType().startsWith("u-O-G-R"));
    }

    @Override
    public int getPriorityLevel() {
        return 2;
    }
});

ImporterManager.registerImporter(new AbstractCotEventImporter(pluginContext, "GG-COT") {
    @Override
    public Set<String> getSupportedMIMETypes() {
        LinkedHashSet<String> hashSet = new LinkedHashSet<String>();
        hashSet.add("application/cot+xml");
        return hashSet;
    }

    @Override
    public CommsMapComponent.ImportResult importData(CotEvent cot, Bundle bundle) {
        if (event.getType().startsWith("u-O-G-R")) {
            // process the event;
            return CommsMapComponent.ImportResult.SUCCESS;
        }
        return CommsMapComponent.ImportResult.FAILURE;
    }

    @Override
    protected CommsMapComponent.ImportResult importNonCotData(InputStream source, String mime) throws IOException {
        return CommsMapComponent.ImportResult.FAILURE;
    }
});
\`\`\`

**CommsLogger (Warning - serious performance impact):**
\`\`\`java
static class MyCommsLogger {
    public void logSend(CotEvent msg, String destination) { ... }
    public void logSend(CotEvent msg, String[] toUIDs) { ... }
    public void logReceive(CotEvent msg, String rxid, String server) { ... }
    public void dispose() { ... }
}

CommsLogger cotListener = new MyCommsLogger();
CommsMapComponent.getInstance().registerCommsLogger(cotListener);
// later:
CommsMapComponent.getInstance().unregisterCommsLogger(cotListener);
\`\`\`

### Sending CoT Messages

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/sending-cot-messages-through-atak

com.atakmap.comms.CotDispatcher is the CoT message dispatch interface. There are two instances - external (sends CoT message to all network outputs configured) and internal (routes CoT message through ATAK's internal CoT message bus).

\`\`\`java
CotDispatcher internal = com.atakmap.android.cot.CotMapComponent.getInternalDispatcher();
CotDispatcher external = com.atakmap.android.cot.CotMapComponent.getExternalDispatcher();
\`\`\`

A message is typically dispatched via:
\`\`\`java
String cot = ...
CotDispatcher dispatcher = ...
dispatcher.dispatch(CotEvent.parse(cot));
\`\`\`

For external dispatch, CotDispatcher.dispatchToBroadcast(...) sends to all Network Outputs. CotDispatcher.dispatch(CotEvent) dispatches to outputs preconfigured via flags. Other methods CotDispatcher.dispatchToContact(...) and CotDispatcher.dispatch(CotEvent, Bundle, CoTSendMethod) allow more configuration.

Send through TAK Server:
\`\`\`java
Bundle data = new Bundle();
data.putStringArray("toUIDs", new String { "uid of receiver" });
CotMapComponent.getExternalDispatcher().dispatch(cotEvent, data, CoTSendMethod.TAK_SERVER);
\`\`\`

### Map Event Handling

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/map-event-handling

The class, MapEventDispatcher, is the primary event bus for all high level events that occur on the map. The MapEventDispatcher instance is obtained from a MapView instance via MapView.getMapEventDispatcher().

**Subscription Methods:**
- MapEventDispatcher.addMapEventListener(MapEventDispatchListener) - subscribe to all events
- MapEventDispatcher.addMapEventListener(String, MapEventDispatcherListener) - by event type
- MapEventDispatcher.addMapItemEventListener(MapItem, OnMapEventListener) - by map item

**Stack Operations:**
- MapEventDispatcher.pushListeners() - copies the listeners at the current top of the stack and pushes that copy.
- MapEventDispatcher.popListeners() - discards the listener subscription at the top, restores prior state.

**Example:**
\`\`\`java
class MyCustomWidget {
    private MapView mapView = ...

    public void onActivated() {
        MapEventDispatcher eventDispatcher = mapView.getMapEventDispatcher();
        eventDispatcher.pushListeners();
        eventDispatcher.clearListeners(MapEvent.ITEM_CLICK);
        eventDispatcher.addMapItemEventListener(MapEvent.ITEM_CLICK, ...);
    }

    public void onDeactivated() {
        MapEventDispatcher eventDispatcher = mapView.getMapEventDispatcher();
        eventDispatcher.popListeners();
    }
}
\`\`\`

**Map Events:**
| Event Type | Description |
|------------|-------------|
| MapEvent.MAP_CLICK | The map was clicked (possibly a double tap) |
| MapEvent.MAP_CONFIRMED_CLICK | Confirmed not to be a double tap. |
| MapEvent.MAP_DOUBLE_TAP | Double tapped. |
| MapEvent.MAP_DRAW | Drag without panning. |
| MapEvent.MAP_LONG_PRESS | Long pressed |
| MapEvent.MAP_MOVED | Map moves, programmatically or via user interaction. |
| MapEvent.MAP_PRESS | A press occurred on the map. |
| MapEvent.MAP_RELEASE | A press has been released. |
| MapEvent.MAP_RESIZED | Size of the map on screen changes. |
| MapEvent.MAP_ROTATE | Map rotated by user touch. |
| MapEvent.MAP_SCALE | Map scaled in or out via pinch. |
| MapEvent.MAP_SCROLL | Map scrolled by the user. |
| MapEvent.MAP_SETTLED | Map animation completed. |
| MapEvent.MAP_TILT | Map tilted by user touch. |
| MapEvent.MAP_ZOOM | Deprecated. Not raised. |

**Item Events:**
| Event Type | Description |
|------------|-------------|
| MapEvent.ITEM_REFRESH | External change occurred to the item. |
| MapEvent.ITEM_PERSIST | Request to persist a MapItem. |
| MapEvent.ITEM_SHARED | MapItem requested to be sent. |
| MapEvent.ITEM_ADDED | MapItem added to the map. |
| MapEvent.ITEM_REMOVED | MapItem removed from the map. |
| MapEvent.ITEM_CLICK | MapItem clicked (possibly double tap). |
| MapEvent.ITEM_CONFIRMED_CLICK | Confirmed clicked, not double tapped. |
| MapEvent.ITEM_LONG_PRESS | MapItem long pressed. |
| MapEvent.ITEM_RELEASE | Press released. |
| MapEvent.ITEM_PRESS | MapItem pressed. |
| MapEvent.ITEM_DOUBLE_TAP | MapItem double tapped. |
| MapEvent.ITEM_GROUP_CHANGED | MapItem's group changes. |
| MapEvent.ITEM_IMPORTED | MapItem is imported. |
| MapEvent.ITEM_DRAG_STARTED | User begins to drag MapItem. |
| MapEvent.ITEM_DRAG_CONTINUED | Location moves while dragging. |
| MapEvent.ITEM_DRAG_DROPPED | MapItem released during drag. |

**Group Events:**
| Event Type | Description |
|------------|-------------|
| MapEvent.GROUP_ADDED | New MapGroup added to the map. |
| MapEvent.GROUP_REMOVED | MapGroup removed from the map. |
| MapEvent.GROUP_REFRESH | Deprecated. Not raised. |

### Map Projections and Coordinate Transformations

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/map-projects-and-coordinate-transformations

ATAK utilizes two Map Projections for display:
- Equirectangular (EPSG:4326) for the flat map
- ECEF (Earth-Centered-Earth-Fixed; EPSG:4978) for the globe

Both projections use the WGS84 Datum/Ellipsoid.

ATAK utilizes the Proj.4 library (https://proj.org/) which supports transformation between many projections and datums.

Map projections are abstracted via the interface, com.atakmap.map.projection.Projection. This interface defines:
- spatial reference ID is the EPSG code associated with the projection
- forward transforms a WGS84 coordinate into the x, y, z of the projection's coordinate system
- inverse transforms a coordinate in the x, y, z of the projection's coordinate system into a WGS84 coordinate

Instances of Projection are obtained through the static factory class, com.atakmap.projection.ProjectionFactory. Users may add their own Projection implementations by implementing the interface, com.atakmap.projection.ProjectionSpi.

### Spinners in Plugins

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/spinners-in-plugins

The Android Spinner (android.widget.Spinner) class cannot be used by plugins, due to incompatibilities in the way it uses the Context used to create it. PluginSpinner (com.atakmap.android.gui.PluginSpinner) is the drop in replacement for the Android Spinner.

**Using PluginSpinner in XML:** Replace android.widget.Spinner with com.atakmap.android.gui.PluginSpinner.

\`\`\`java
private void initializeSpinner() {
    _spinner = (PluginSpinner) view.findViewById(R.id.spinner);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(_mapCtx,
        android.R.layout.simple_spinner_item, listCallsigns);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    _spinner.setAdapter(adapter);
    _spinner.setOnItemSelectedListener(new SpinnerListener());
    _spinner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
}
\`\`\`

### Working with Native Code

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/working-with-native-code

ATAK includes native libraries for the following ABIs: x86, armeabi-v7a, arm64-v8a. All plugins should ensure that they target those ABIs.

Native libraries compiled for the ATAK core use NDK 12b. Plugins that have dependencies on any of the shared objects included with ATAK should use 12b, otherwise, they may utilize other versions of the NDK.

**NDK r12b Direct Download Links:**
- Windows 32-bit: https://dl.google.com/android/repository/android-ndk-r12b-windows-x86.zip
- Windows 64-bit: https://dl.google.com/android/repository/android-ndk-r12b-windows-x86_64.zip
- Mac OS X: https://dl.google.com/android/repository/android-ndk-r12b-darwin-x86_64.zip
- Linux 64-bit: https://dl.google.com/android/repository/android-ndk-r12b-linux-x86_64.zip

**NDK Install Locations:**
- $ANDROID_SDK_HOME/ndk
- set ndk.dir in the project's local.properties
- set the $ANDROID_NDK_HOME environment variable (gradle has deprecated use)

**Helpful Hints:**
- If using PluginNativeLoader, the init(Context) method MUST be invoked with the plugin context prior to trying to load libraries.
- Listing dependencies of a library: \`$ANDROID_NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/$HOST_PLATFORM/bin/arm-linux-androideabi-readelf -a libgdal.so | grep NEEDED\`

### Using an Emulator

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/using-an-emulator

Due to use of hardware accelerated graphics, ATAK may not run in an AVD emulator in a given host environment. The x86 Android VM (https://www.android-x86.org/) has been found to be a viable option.

**Helpful Tips:**
- The emulator needs to run with GLES 3.0 or later compatibility. Once launched, click the Menu (...) button on the menu bar, select Settings then Advanced tab. Ensure Open GL ES API Level is set to GLES 3.0 or higher.
- Reset graphics: \`emulator -avd EMULATOR_NAME -gpu host\`
- DNS issue fix: \`emulator @EMULATOR_NAME -dns-server 8.8.8.8\`

### Radial Menu API Default Construction

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-developer-guide/radial-menu-api

\`\`\`java
final MapView mapView = MapView.getMapView();
final MapAssets mapAssets = new MapAssets(mapView.getContext());
final MenuMapAdapter adapter = new MenuMapAdapter();
try {
    adapter.loadMenuFilters(mapAssets, "filters/menu_filters.xml");
} catch (IOException e) {
    Log.w(TAG, e);
}
MenuResourceFactory factory = new MenuResourceFactory(mapView, mapView.getMapData(), mapAssets, adapter);
\`\`\`

**Standard ConfigEnvironment:**
\`\`\`java
final MapView mapView = MapView.getMapView();
final MapAssets mapAssets = new MapAssets(mapView.getContext());
final MapData mapData = mapView.getMapData();

final PhraseParser.Parameters params = new PhraseParser.Parameters();
params.setResolver('@', new PhraseParser.BundleResolver(mapData));
params.setResolver('!', new PhraseParser.IsEmptyResolver());
params.setResolver('?', new PhraseParser.NotEmptyResolver());

ConfigEnvironment.Builder configBuilder = new ConfigEnvironment.Builder();
final ConfigEnvironment configEnvironment = configBuilder
    .setMapAssets(mapAssets)
    .setPhraseParserParameters(params)
    .build();
\`\`\`

**Adding MapItem Resolver:**
\`\`\`java
final MapItem mapItem = new Marker( ... );
final PhraseParser.Parameters itemParams = new PhraseParser.Parameters(params);
itemParams.setResolver('$', new PhraseParser.BundleResolver(mapItem));

ConfigEnvironment mapItemConfigEnvironment = configEnvironment
    .buildUpon()
    .setPhraseParserParameters(itemParams)
    .build();
\`\`\`

## Common Errors and Solutions

### Plugin Linting Error: PluginTemplate Name

As of atak-gradle-takdev version 2.5.1, linting is performed. The error message:
> Proguard configured for plugintemplate, but this plugin is not the plugintemplate.

**Fix:** Modify repackageclasses line in proguard-gradle.txt:
\`\`\`
-repackageclasses atakplugin.PluginTemplate
\`\`\`
Becomes:
\`\`\`
-repackageclasses atakplugin.MyCoolPlugin
\`\`\`

### ViewPager2 Library Issue

If you are using ViewPager as part of your plugin you will need to call \`pager.setSaveEnabled(false)\` otherwise you will get an exception [ATAK-20279].

### Plugin Compatibility - Won't Load

> The ATAK Package Mgmt tool shows my plugin as compatible, but it still fails to load.
> The cause is likely to be that you are trying to load a debug version of your plugin in a release version of ATAK.

### Context::registerReceiver Crash (Android 14)

\`\`\`
Exception java.lang.SecurityException:
  at android.os.Parcel.createExceptionOrNull (Parcel.java:3069)
  at android.app.ContextImpl.registerReceiver (ContextImpl.java:1853)
\`\`\`

**Fix:** Use AtakBroadcast or pattern with RECEIVER_EXPORTED for SDK_INT >= TIRAMISU.

### RecyclerView Build Issue

When using a recycler view in a plugin, you need to exclude modules that are provided by core ATAK. Otherwise bad things happen in the release builds after proguarding.

### ItemTouchHelper

When using an ItemTouchHelper inside of a plugin, the call to attachToRecyclerView will fail if using the RecyclerViewClass directly. There is a class supplied as part of the helloworld sample - \`com/atakmap/android/hellworld/utils/PluginRecyclerView.java\`. This is a drop in replacement for the RecyclerView referenced in the layout files.

### Native Libraries (Min SDK 26+)

If your plugin uses Native Libraries and set the Android SDK Target to 26 or newer (e.g. minSdkVersion 26), make sure your plugin AndroidManifest.xml application block contains:
\`\`\`
android:extractNativeLibs="true"
\`\`\`

### androidx.core Runtime Crash

A runtime crash missing saveAttributeDataForStyleable from a ViewCompat. Examples of how to correct this are in the helloworld build.gradle file. Use resolutionStrategy or global excludes.

## Complete Plugin Template

The plugin template project is located at:
- New plugin template: https://git.tak.gov/samples/plugintemplate
- Legacy plugin migration template: https://git.tak.gov/samples/PluginTemplateLegacy
- Migration commit example: https://git.tak.gov/samples/PluginTemplateLegacy/-/commit/6307d188e175b28a326a5e400824f67e9daba8e2
- DoD GitHub mirror: https://github.com/deptofdefense/AndroidTacticalAssaultKit-CIV/tree/master/plugin-examples/plugintemplate

### File Formats Supported (App Folder Structure)

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/file-formats-supported

\`/atak\` is the top level folder, all folders referenced below are relative to this folder.

**\`/DTED\`**: All DTED resides here. Format: \`/DTED/w117/n34.dt1\`, \`/DTED/w117/n34.dt2\`. Where w117 represents "westing" and n34.dt* represents "northing". Supports DTED Level 0, 1, 2, and 3.

**\`/grg\`**: GeoTIFF or KMZ GRG images go in this directory. Treated as overlay; visibility can be toggled.

**\`/imagery\`**: Data exported from FalconView or JMPS Map Data Manager. Formats: GeoJPEG2000, GeoTIFF, CIB/CADRG, ECRG, KMZ, MrSID, NITF, PFI, PRI, RPF.

**\`/imageryMobile\`**: SQLite tilesets from MOBAC (OSMDroid Sqlite format), WMS configuration data, Geopackage tilesets (.gpkg), Legacy tile caches (.zip).

**\`/prefs\`** (internal card only): Save Preferences output. Renaming to "defaults" pre-deploys preferences.

**APASS**: Imagery used with the APASS Android application is auto-discovered.

**\`/support/logs\`**: Diagnostic logs and crash reports. KML overlay files supported. Subset includes Styles, Placemark Point, LineString, Polygon, MultiGeometry. Specifically unsupported: Placemark LinearRing, MultiTrack, IconStyle/Icon/Href.

**\`/export\`**: Shapes exported from the map (e.g. KML).

**\`/tools/missionpackage\`**: Mission packages (created, imported, or received over-the-air).

**\`/overlays\`**: Vector overlays. Formats: DRW, GPX, KML, KMZ, LPT, Shapefile. In 3.10+, support for obj models and other 3D models from producers like Pix4D.

**\`/tools/videos\`**: Recorded videos for playback.

**\`/tools/jumpmaster\`**: Wind data files (CSV, TXT, CNS) and network wind servers config.

**\`/Databases/crumbs.sqlite\`** (encrypted as of 3.9+): Crumb points (self or other) recorded over the last 30 days.

**Crumbs Table Columns:**
\`uid, sid, title, timestamp, lat, lon, alt, ce, le, bearing, speed, ptsource, altsource, point_geom\`

Where:
- uid - the unique identifier for the map marker
- sid - the track segment id
- title - callsign or title
- timestamp - milliseconds since January 1, 1970, 00:00:00 GMT
- lat - latitude in degrees
- lon - longitude in degrees
- alt - altitude in HAE
- ce - CE90 for the point
- le - LE90 for the point
- bearing - bearing in degrees
- speed - speed in meters/second
- ptsource - source for the location point (USER, GPS, etc)
- altsource - source for the altitude point (USER, GPS, DTED, etc)
- point_geom - non-human readable spatial geometry for searching

### ATAK Coding Style Guide

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/atak-coding-style-guide

**Formatting Style:**
- ATAK makes use of the android-formatting.xml file supplied in the SDK
- Go to the Plugins Section of Android Studio Settings
- Click on Browse Repositories
- Type "Eclipse Code Formatter" in the search box
- Install and restart Android Studio
- Go to the Plugins Section of Android Studio Settings
- Select Eclipse Code Formatter
- Then select the Code Format XML file
- Click OK

### Testing ATAK Plugins

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/testing-atak-plugins

The ATAK SDK includes a framework for enabling automated testing of ATAK plugins. Because ATAK plugins are not fully feature Android activities, but rather libraries that are dynamically loaded into the ATAK process runtime, a special framework is required to enable Instrumented tests.

**Prerequisites:**
- ATAK Plugin Development Environment
- ATAK SDK
- ATAK Plugin Project, compatible with build system used by PluginTemplate
- Android emulator or physical device

**Installing the ATAK Plugin Test Framework:**
Copy the espresso folder from the SDK installation into your plugin project, such that espresso is a sibling to the directory that holds your project's build.gradle file.

Example:
- MyAtakPlugin/ ← your plugin's base directory
- MyAtakPlugin/app/build.gradle ← the project file for your plugin
- MyAtakPlugin/espresso ← the copy location for the espresso directory from the SDK

**Test Class Requirements:**
- The test class must derive from ATAKTestClass (com.atakmap.android.test.helpers.ATAKTestClass)
- Setup with @BeforeClass:

\`\`\`java
public class ExampleTest {
    @BeforeClass
    public static void setupPlugin() throws Exception {
        // Install plugin
        helper.installPlugin("My Plugin Name");
        ClassLoaderReplacer.fixClassLoaderForClass(ExampleTest.class, "com.atakmap.android.myplugin");
    }

    @AfterClass
    public static void restoreClassLoader() throws Exception {
        ClassLoaderReplacer.restoreLoader(ExampleTest.class);
    }
}
\`\`\`

**Executing Instrumented Tests:**
\`\`\`
./gradlew connected<flavor>DebugAndroidTest
\`\`\`

**Known Issues:** Debugging of Instrumented Tests is not known to work within the ATAK Plugin Testing Framework.

**Disable Animations on Device:**
\`\`\`
adb shell settings put global window_animation_scale 0 && adb shell settings put global transition_animation_scale 0 && adb shell settings put global animator_duration_scale 0
\`\`\`

### Debugging ATAK Plugins

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/debugging-atak-plugins

Android Studio can be used to debug both ATAK and ATAK plugins.

**Debugging ATAK:** Either launch the activity via Debug ATAK-app button or use the Attach Debugger to Android Process button to attach to a runtime of ATAK that is already in session. The name of the process will be \`com.atakmap.app\`.

**Debugging Plugins:** Since plugins run within the ATAK process, follow the same steps for Debugging ATAK to attach a debugging session.

**Helpful Tips:**
- Using the Attach Debugger to Android Process method is much faster
- Debugging native code seems to work much better in an emulator than on a physical device
- If Attach Debugger to Android Process isn't showing any processes for your device, select the Show All Processes checkbox
- If Attach Debugger to Android Process does nothing when you've selected com.atakmap.app, try changing the Debugger dropdown value to Java

### Disabling and Hiding Preferences

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/disabling-hiding-preferences

As of ATAK 3.11, there are now allowances for a person to craft a preference file (.pref) that can disable access to certain preference items. This preference file can be deployed via a DataPackage.

**Format:**
\`\`\`xml
<?xml version='1.0' standalone='yes'?>
<preferences>
<preference version="1" name="com.atakmap.app_preferences">
<entry key="hidePreferenceItem_xxxx" class="class java.lang.Boolean">false</entry>
<entry key="disablePreferenceItem_xxxx" class="class java.lang.Boolean">false</entry>
</preference>
</preferences>
\`\`\`

**Action:**
- disable = where false means it is darkened and cannot be interacted with.
- hide = no longer visible

**Sample Preference Keys (selected):**
- 'hidePreferenceItem_about' - About
- 'hidePreferenceItem_atakAccounts' - Accounts
- 'hidePreferenceItem_alertPreference' - Alert Preferences
- 'hidePreferenceItem_atakDocumentation' - ATAK User Manual
- 'hidePreferenceItem_bloodhoundPreferences' - Bloodhound Preferences
- 'hidePreferenceItem_bluetoothPref' - Bluetooth Preferences
- 'hidePreferenceItem_chatPort' - Chat Port
- 'hidePreferenceItem_chatPreference' - Chat Preferences
- 'hidePreferenceItem_dtedPreference' - Elevation Overlays Preferences
- 'hidePreferenceItem_gpsSettings' - GPS Preferences / Network GPS
- 'hidePreferenceItem_gridLinesPreference' - Grid Line Preferences
- 'hidePreferenceItem_largeTextMode' - Enable Large Text Mode
- 'hidePreferenceItem_largeActionBar' - Enable Large Tool Bar
- 'hidePreferenceItem_loadPrefs' - Load Preferences
- 'hidePreferenceItem_savePrefs' - Save Preferences
- 'hidePreferenceItem_locationCallsign' - My Callsign
- 'hidePreferenceItem_myIdentity' - My Callsign
- 'hidePreferenceItem_myServers' - TAK Servers
- 'hidePreferenceItem_myPlugins' - TAK Package Mgmt
- 'hidePreferenceItem_appsPref' - TAK Package Mgmt
- 'hidePreferenceItem_routePreference' - Route Preferences
- 'hidePreferenceItem_metricsPreference' - Metric Report Preferences
- 'hidePreferenceItem_videoPreference' - Video Preferences
- 'hidePreferenceItem_unitPreferences' - Unit Display Format Preferences
- 'hidePreferenceItem_settingsLogging' - Logging Preferences
- 'hidePreferenceItem_resetHints' - Reset Hints
- 'hidePreferenceItem_resetDeviceConfig' - TAK Device Configuration
- 'hidePreferenceItem_prepareForClone' - Prepare for Cloning / Clear Device unique information

(See full list at https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/disabling-hiding-preferences)

### Data Package Format

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/cross-products-guides/data-package-format

The data package is an easy way to transmit data to other TAK devices. Format is a zip file with .zip or .dpk (TAK 4.0 or newer) extension and an optional manifest.

\`\`\`
<root>
 |___ file 1
 |___ file 2
 |___ MANIFEST
        |___ manifest.xml
\`\`\`

**Manifest example:**
\`\`\`xml
<MissionPackageManifest version="2">
    <Configuration>
        <Parameter name="uid" value="a647112f-ce05-4312-a2b4-208d8d8a5fa8"/>
        <Parameter name="name" value="missionpackage.zip"/>
        <Parameter name="onReceiveDelete" value="true"/>
    </Configuration>
    <Contents>
        <Content ignore="false" zipEntry="file1"/>
        <Content ignore="false" zipEntry="file2"/>
    </Contents>
</MissionPackageManifest>
\`\`\`

**Configuration:**
- uid - the uid for the data package; if not supplied, a hash is generated to determine if file is unique
- name - the name of the data package for display
- onReceiveDelete - if true, deleted as soon as import finishes (ideal for staging)

**Contents:**
- Content - name of file in the data package; if ignore=true, file is not passed to the import manager

### Code Deprecation Policy

**Source:** https://tak.gov/documentation/resources/tak-developers/code-deprecation-policy

**Marking Deprecated APIs:** Well-known mechanisms shall be used to mark deprecated code. Markings shall provide:
- Version that the API was deprecated
- Version that the API will be removed

**Deprecation Grace Period:** Obsolete APIs will remain intact for **three baseline releases** after being marked as obsolete. For example, if marked as deprecated in x.5, it can be expected to be removed in the x.8 release.

**Deprecation Report:** Produced for every release with list of APIs marked as deprecated, expected date of removal, and APIs removed in the release.

**Versioned Endpoints:**
\`\`\`
{"version":"<version>","type" ...
\`\`\`

**Build System and ABI/API Changes** - These are NOT subject to the deprecation policy:
- Build System Changes (gradle/tooling updates)
- Core Supplied 3rd party Libraries (security/stability)
- Externally Forced Operating System / Build Deprecation

### Military vs Civilian ATAK Differences

**Source:** https://tak.gov/documentation/resources/tak-developers/developer-documentation/atak-development/mil-vs-civ-atak-diffs

| Component | Military ATAK | Civilian ATAK |
|-----------|---------------|---------------|
| Shortcut Icon | TAK with Skull | TAK with Bird |
| Bloodhound | "Quick Select Spi" field, callsign with ".SPI" | "Quick Select DP" field, callsign with ".DP" |
| Emergency Beacon | 911 Alert, Ring The Bell, Geo-fenced Breached, Troops In Contact | Alert, Ring The Bell, Geo-Fence Breached, In Contact |
| Fire Tools | Default Toolbar Fires/Digital Pointer | Digital Pointer Toolbar |
| Path | Settings/Tool Preferences/Fires Toolbar Preferences | Settings/Tool Preferences/Digital Pointer Toolbar Preferences |
| Build number | Ends in "(MIL)" or nothing | Ends in "(CIV)" |
| Toolbar Manager Profiles | Default, Minimal, JTAC, Planning | No JTAC |
| 9-line/5-line Control Preferences | Included | Not included |
| Mapsources | 13 Standard, 3 NGA | 10 Standard, no NGA |
| Markers | CoT2525B with track type/subtypes | Basic Markers without type/subtype |
| Mission Pallet | Includes CP, IP, BP/HA, WayPt, Sensor, OP, SSE Slant | Does not include CP, IP, BP/HA, SSE Slant |
| RedX Radial | 8 options including FAH Arrow and 9-Line/CFF | No FAH Arrow or 9-Line/CFF |

