package com.example.dater

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.dater.alarms.notification.DaterNotification
import com.example.dater.alarms.reminderAlarm.ReminderAlarmItem
import com.example.dater.alarms.reminderAlarm.ReminderSchedulerImpl
import com.example.dater.ui.Navigation.NavGraph
import com.example.dater.ui.components.BottomNavBar.viewComponent.BottomNavBarView
import com.example.dater.ui.components.TopFilterBar.viewComponent.TopFilterBarView
import com.example.dater.ui.theme.DaterTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DaterTheme {


                val notification = DaterNotification(this)
                notification.notificationCheck()


                val navController = rememberNavController()

                val reminderScheduler = ReminderSchedulerImpl(this)
                var alarmItem: ReminderAlarmItem? = null

                Surface(modifier = Modifier) {

                    Column (modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ){

                        Column{

                            TopFilterBarView(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            NavGraph(
                                navController = navController,
                                modifier = Modifier.weight(1f),
                            )

                            BottomNavBarView(
                                navController = navController,
                                modifier = Modifier
                            )
                        }
                    }



                }

            }
        }
    }
}

fun ToastMessage(
    context: Context,
    string: String,

){
    Toast.makeText(context,string,Toast.LENGTH_SHORT).show()
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}