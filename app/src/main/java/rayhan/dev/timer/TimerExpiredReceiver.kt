package rayhan.dev.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import rayhan.dev.timer.util.NotificationUtil
import rayhan.dev.timer.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
      NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(MainActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)

    }
}
