# PAYO_ASSIGNMENT_FOR_ANDROID_INTERNSHIP
## Name: Prajata_Samanta
## GitHub Link: https://github.com/Prajata22/PayO_Assignment

## Main features
* Sign up with all the credentials required. All credentials are mandatory
* There is a check if password and confirm password are matching or not
* SQLite database is used as a local database for storing all credentials
* No two accounts can be created using same email
* Once an account is created, user can login using valid credentials
* In the home screen, a list of records is displayed in a recycler view by calling given api(both pages)
* Pagination is used in recycler view
* A toast is shown along with a graphic in case of api failure
* The list is displayed in ascending order based on first name and last name separately for both pages
* On long clicking an item, an alert dialog is shown asking for confirmation to delete it. On pressing delete the item is removed from the list
* Insted of a progress bar, a shimmer effect is given to the recycler view while loading api
* From the navigation drawer, own profile can be visited
* From the navigation drawer, on clicking log out there is a confirmation dialog to log out. On clicking log out, user will be redirected to login screen
* All kinds of form validations are present both in login screen as well as sign up screen in the form of toasts and animations

## Built from source
* Build tools: 30.0.2
* Support library: 1.2.0 - androidx
* minSdkVersion 21
* compileSdkVersion 30
* targetSdkVersion 30
* versionCode 1
* versionName 1.0
* Gradle plugin: 6.5.0
* Android Studio: 4.1.0
