# DAT257/DIT257 - Team Apricot

Welcome to team Apricot's GitHub repository!

## Project description
Team Apricot has designed and developed an Android application used to motivate users to go out for a walk. The app will generate random positions for the user to walk to, and whenever the user arrives at the destination, the app will prompt the user to take a photo to keep as a memory. Once the photo has been taken, it will be visible on the map in the app, and the user can click on the photo to view it again.

## User mappings

Below is a table consisting of GitHub user names and the corresponding real names for each member within team Apricot.

| GitHub user name | Name |
| ---------------- | ---- |
| danbrn | Daniel Brännvall |
| DKWA0000 | Joakim Tubring |
| Erik142 | Erik Wahlberger |
| jmollyj | Molly James |
| ramzajosoph | Ramza Josoph |
| valeria9090 | Valeria Nafuna |

## Repo paths

Within the repo, you can find source code, reflections, meeting notes, our social contract, the definition of done, our user stories and our scrum board. Links to everything is provided below:

- [Social contract](https://github.com/Erik142/Team-Apricot/blob/master/Social%20contract.md)
- [Definition of Done](https://github.com/Erik142/Team-Apricot/blob/master/Team%20Apricot%20DoD.pdf)
- [Team reflections](https://github.com/Erik142/Team-Apricot/tree/master/Reflections/Team%20reflections)
- [Individual reflections](https://github.com/Erik142/Team-Apricot/tree/master/Reflections/Individual%20Reflections)
- [User stories](https://github.com/Erik142/Team-Apricot/issues?q=is%3Aissue+label%3Asprint-backlog%2Cproduct-backlog)
- [User story tasks](https://github.com/Erik142/Team-Apricot/issues?q=is%3Aissue+label%3Atask)
- [Scrum board](https://github.com/Erik142/Team-Apricot/projects/1)
- [Contribution statistics](https://github.com/Erik142/Team-Apricot/graphs/contributors) Can be filtered on amount of commits, lines added and removed.
- [Source code](https://github.com/Erik142/Team-Apricot/tree/master/Source%20Code)
- [Pre-built APK file](https://github.com/Erik142/Team-Apricot/releases)

## External resources

Since we have chosen to develop an Android application, we have made good use of the Android SDK, and the corresponding documentation from Google: [developer.android.com](https://developer.android.com/).
For our external data source, we have used OpenStreetMap to create the map view in our Android application. To use OpenStreetMap, we have used a library called osmdroid, and their corresponding documentation: [osmdroid.github.io](https://osmdroid.github.io/osmdroid/).
We use a local SQLite database within the app to store paths to photos, store routes and achievements. To use an SQLite database within Android, we have used the Android Room API: [Android Room documentation](https://developer.android.com/jetpack/androidx/releases/room)

## Installation instructions

### Pre-built version

A prebuilt-version of the app can be found on the [releases](https://github.com/Erik142/Team-Apricot/releases) page. Transfer this to your Android device, then make sure that you have enabled the setting to allow installation of apps from "unknown sources" in Android. The way this is enabled differs slightly between devices and Android versions, so perform a quick Google search to see how to do it on your specific device. If you cannot find this setting manually, you will most likely be prompted to enable it when you try to install the application on your phone. Finally, open a file explorer application on your phone, navigate to the path where you saved the pre-built APK file, and click on it. Follow the on-screen instructions to install the application, then you should be set!

### DIY

For this to work, you need to have [Android Studio](https://developer.android.com/studio) installed on your computer. Open the [Project Walking]() subfolder in Android studio, and wait for Gradle to finish syncing. At this point, you can either create an [Android Virtual Device](https://developer.android.com/studio/run/managing-avds) and run the application that way, or you can use your own Android device to run the application. If you choose to run the app on your own device, you have to enable "developer settings" as well as the "USB Debugging" setting on your phone. This is done in different ways for different Android devices, so make a quick Google search to see what applies for your device. When that is done, you should be able to connect your device to your computer via a USB cable and run the application on your phone from Android Studio.
