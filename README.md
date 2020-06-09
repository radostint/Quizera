# Quizera
>Quizera is small quiz app. It contains few quizzes and a ranking system.

In order to use the app one should register. Players information is stored at Firebase database. Once player is logged in they have access to all quizzes. After finishing a certain quiz player receives information about final result and points won.
## Activities
### Main activity
Contains the login/register code. Once player registers and/or logs in he is redirected to the homescreen(HomeActivity).
### Home Activity
Displays content based on selected fragment. By default CategoryFragment is shown.
### Start Activity
When player chooses certain quiz(category) from the CategoryFragment, the categoryId of the chosen quiz is stored in global variable(Common.class).StartActivity is started and then questions from the chosen quiz are being added to ArrayList.List is being shuffled. Player must press the Play button in order to start the quiz.
### Play Activity
Player has 20 seconds to answer each question. If there is no player interaction for more than 20 seconds next question is loaded and no points are added to the final score.If player answers correctly 10 points are added to final score. Once the quiz ends data(correct answer,total questions,score) is sent to DoneActivity and it is started.
### Done Activity
Player sees their final score and answered questions/total questions(ex: 40  4/10).
In order to go back to HomeActivity player must press "Играй отново" button or just press the back button on their phone.
### Score Details Activity
When player clicks on a certain username in the RankingFragment he can see their best scores in each quiz(category)

## Fragments
### Category Fragment
Contains all playable quizzes(categories).
### Ranking Fragment
Contains best scores of all players.
## Other classes
* Common
  * Common.class |    Used for storing global variables.
* Model |   All of the classes below are used as data model in order to use with firebase db
  * Category.class
  * Question.class
  * QuestionScore.class
  * Ranking.class
  * User.class
* ViewHolder | All of the classes below are used for the RecyclerViews
  * CategoryViewHolder
  * RankingViewHolder
  * ScoreDetailsViewHolder
