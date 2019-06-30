package rayhan.dev.timer.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import rayhan.dev.timer.AppConstans
import rayhan.dev.timer.MainActivity
import rayhan.dev.timer.R
import rayhan.dev.timer.TimerNotificationActionReceiver
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
        companion object{
            private const val CHANNEL_ID_TIMER = "menu_timer"
            private const val CHANNEL_NAME_TIMER = "Timer App Timer"
            private const val Tiemr_ID = 0


            fun showTimerExpired(context: Context){
                val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
                startIntent.action = AppConstans.ACTION_START
                val startPendingIntent = PendingIntent.getBroadcast(context,
                    0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
                nBuilder.setContentTitle("Timer Expired!!!")
                    .setContentTitle("Start Again?")
                    .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .addAction(R.drawable.ic_play, "Start", startPendingIntent)
                val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

                nManager.notify(Tiemr_ID, nBuilder.build())


            }


            fun showTimerRunning(context: Context, wakeUpTime: Long){
                val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
                stopIntent.action = AppConstans.ACTION_STOP
                val stopPendingIntent = PendingIntent.getBroadcast(context,
                    0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
                pauseIntent.action = AppConstans.ACTION_PAUSE
                val pausePendingIntent = PendingIntent.getBroadcast(context,
                    0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

                val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
                nBuilder.setContentTitle("Timer is Running")
                    .setContentTitle("End: ${df.format(Date(wakeUpTime))}")
                    .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .setOngoing(true)
                    .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                    .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)

                val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

                nManager.notify(Tiemr_ID, nBuilder.build())


            }


            fun showTimerPaused(context: Context){
                val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
                resumeIntent.action = AppConstans.ACTION_START
                val resumePendingIntent = PendingIntent.getBroadcast(context,
                    0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                val nBuilder = getBasicNotificationBuilder(context, CHANNEL_ID_TIMER, true)
                nBuilder.setContentTitle("Timer is Paused.")
                    .setContentTitle("Resume?")
                    .setContentIntent(getPendingIntentWithStack(context, MainActivity::class.java))
                    .setOngoing(true)
                    .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)
                val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nManager.createNotificationChannel(CHANNEL_ID_TIMER, CHANNEL_NAME_TIMER, true)

                nManager.notify(Tiemr_ID, nBuilder.build())


            }

            fun hideTimerNotification(context: Context){
                val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nManager.cancel(Tiemr_ID)
            }



            private fun getBasicNotificationBuilder(context: Context, channelId: String, playSound: Boolean)
                    : NotificationCompat.Builder{
                val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val nBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_timer)
                    .setAutoCancel(true)
                    .setDefaults(0)
                if(playSound) nBuilder.setSound(notificationSound)
                return nBuilder

            }


            private fun <T> getPendingIntentWithStack(context: Context, javaClass: Class<T>): PendingIntent{
                val resultIntent = Intent(context, javaClass)
                resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP


                val stackBuilder = TaskStackBuilder.create(context)
                stackBuilder.addParentStack(javaClass)
                stackBuilder.addNextIntent(resultIntent)

                return stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
            }



            private fun NotificationManager.createNotificationChannel(channelName: String, channelID: String, playSound: Boolean) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    val channelImportance = if (playSound) NotificationManager.IMPORTANCE_DEFAULT
                    else NotificationManager.IMPORTANCE_LOW
                    val nChannel = NotificationChannel(channelID, channelName, channelImportance)
                    nChannel.enableLights(true)
                    nChannel.lightColor = Color.BLUE
                    this.createNotificationChannel(nChannel)
                }


            }

                }
        }