package rayhan.dev.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import rayhan.dev.timer.util.NotificationUtil
import rayhan.dev.timer.util.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       when (intent.action){
           AppConstans.ACTION_STOP -> {

               MainActivity.removeAlarm(context)
               PrefUtil.setTimerState(MainActivity.TimerState.Stopped,context)
               NotificationUtil.hideTimerNotification(context)

           }

           AppConstans.ACTION_PAUSE -> {
               var SecondsRemaining = PrefUtil.getSecondsRemaining(context)
               val alarmSetTime = PrefUtil.getAlarmSetTime(context)
               val nowSeconds = MainActivity.nowSecond

               SecondsRemaining -= nowSeconds -alarmSetTime
               PrefUtil.setSecondsRemaining(SecondsRemaining, context)

               MainActivity.removeAlarm(context)
               PrefUtil.setTimerState(MainActivity.TimerState.Paused, context)
               NotificationUtil.showTimerPaused(context)
           }
           AppConstans.ACTION_RESUME -> {
               var SecondsRemaining = PrefUtil.getSecondsRemaining(context)
               val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSecond, SecondsRemaining)
               PrefUtil.setTimerState(MainActivity.TimerState.running, context)
               NotificationUtil.showTimerRunning(context, wakeUpTime)
           }
           AppConstans.ACTION_START -> {
               val minuteRemaining = PrefUtil.getTimerLength(context)
               val SecondsRemaining = minuteRemaining * 60L
               val wakeUpTime = MainActivity.setAlarm(context, MainActivity.nowSecond, SecondsRemaining)
               PrefUtil.setTimerState(MainActivity.TimerState.running, context)
               PrefUtil.setSecondsRemaining(SecondsRemaining, context)
               NotificationUtil.showTimerRunning(context, wakeUpTime)
           }
       }
    }
}
