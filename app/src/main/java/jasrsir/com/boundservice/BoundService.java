package jasrsir.com.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Para vincular el BoundService con la activity debemos realizar una clase interna para el servicio que devuelva el servicio
 *
 * @link MyBinder
 */
public class BoundService extends Service {

    //objeto interno del servicio
    private IBinder mBinder;
    private Chronometer mChronometer; // Cronómetro a mostrar con el serviicio
    private final String TAG = "BoundService";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "in ONCREATE");
        mBinder = new MyBinder();
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    //regioon METODOS DE BOUNDSERVICE OBLIGATORIOS

    /**
     * OnBind develve el objeto IBinder
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "in ONBIND <<---------------------------------------");

        mChronometer.start();
       /* if (intent.getExtras() != null)
            mChronometer.setBase(intent.getExtras().getLong("time",0));*/
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "in ONREBIND <<---------------------------------------");
      //  mChronometer.setBase(intent.getExtras().getLong("time",0));
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "in ONUNBIND <<---------------------------------------");
        mChronometer.stop();
       /* Bundle time = new Bundle();
        time.putLong("time",mChronometer.getBase());
        intent.putExtras(time);*/
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "in ONDESTROY <<---------------------------------------");

        super.onDestroy();
    }

    /**
     * Clase Interna que devuelve la instancia del servicio.
     */
    protected class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }
    //endregion


    //region METODOS
    public String getTimeStamp() {
        long elapsedmillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        SimpleDateFormat outFmt = new SimpleDateFormat("HH:mm:ss:SSSS");
        Date date = new Date(elapsedmillis);
        String dateString = outFmt.format(date);
        return dateString;

    }
    //endregion


}
