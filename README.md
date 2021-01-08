# Song Snap

### Description:
Song Snap is a mobile application where you can share your favourite Spotify music to your friend with your recommendations, just like a DJ.

### Contributors:
Nancy(Leqi) Wan
Angela Tian
***Remark***: The project was originally created at the Cmd-f hackathon @ UBC in 2019.

### Core features:
1. Register and Login to your own account and link it to your Spotify account
2. Play your favourite Spotify playlist and snap a clip of your favourite song
3. Look up all snap history
3. Record your recommendations(this would be combined with the music piece you just recorded but still needs further elaboration)
4. Share the audio piece to your friend through WhatsApp messenger

### Architecture:
***Activities/Controllers***
`app/src/main/java/MainActivity`: Page navigation and play/pause/shift current song track
`app/src/main/java/activities/com.androidtutorialshub.loginregister/RegisterActivity`: Registration and login
`app/src/main/java/activities/com.androidtutorialshub.loginregister/MySpotifyAuthenticationActivity`: Authenticate the user's Spotify account
`app/src/main/java/activities/com.androidtutorialshub.loginregister/SongListActivity`: Display all snap history
`app/src/main/java/activities/com.androidtutorialshub.loginregister/RecordActivity`: External voice recorder
`app/src/main/java/activities/com.androidtutorialshub.loginregister/TimerPageActivity`: Media(music) recorder

***Layouts/Views***
`app/src/main/res/layout/app_bar_main`: Main page
`app/src/main/res/layout/activity_login` + `app/src/main/res/layout/activity_register`: User login and new user registration interface
`app/src/main/res/layout/activity_users_list`: Snap history interface
`app/src/main/res/layout/activity_timer_page`: External voice recorder interface
`app/src/main/res/layout/activity_countdown`: Media(music) recorder interface

### Demo(Interfaces)
![](demo/demo.gif)

### Running the project:
Since ***gradle*** is used to build the project, but there may be build errors for multiple reasons(e.g. invalid build cache, outdated dependencies, etc.), it is usually recommended to follow the steps below when trying to run the project once in a while/for the first time:
1. File -> Invalidate Cache/Restart...
2. Build -> Clean build + Rebuild
3. Run `./gradlew --refresh-dependencies clean build`
4. Start your project

### References:
Login and registration: http://www.androidtutorialshub.com/android-login-and-register-with-sqlite-database-tutorial/
Spotify SDK from https://github.com/spotify/android-sdk/releases

