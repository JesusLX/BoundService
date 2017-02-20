package jasrsir.com.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static android.R.attr.name;

/**
 * El bound service (el que debe comunicarse con una activity)
 * su ciclo de vida depende de la activity a la que está ligado.
 * Nuestra nec esidad es cr4ear un servicio que contabilicve algo
 * como la activity no puede (no debe) contabilizar eso
 * vamnos a hacerle un BoundServiceç
 * Creamos comunicacion Cliente/ nservicio
 * La historia está en que vamos a hacer una actividad que necesita un servicio que va a mostrar el tiempo
 */
public class MainActivity extends AppCompatActivity {

    private BoundService mBoundService;
    private boolean mServiceBound;
    public static FloatingActionButton mFabStart;
    private FloatingActionButton mFabStop;
    private TextView mTxvClock;

    /**
     * ServiceConnection es la clase que gestiona la comunicación con el servicio y el cliente.
     * DEBEMOS TENER EN CUENTA.......
     * todo ¿qué hacemos cuando se inicia el servicio?
     * todo ¿qué hacemos cuando se detiene el servicio?
     */
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFabStart = (FloatingActionButton) findViewById(R.id.fabStart);
        mFabStop = (FloatingActionButton) findViewById(R.id.fabStop);
        mTxvClock = (TextView) findViewById(R.id.txvClock);
        mFabStop.setEnabled(false);

        mFabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mServiceBound) {
                    Intent intent = new Intent(MainActivity.this, BoundService.class);
                    //startService(intent);
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                }
                mTxvClock.setText(mBoundService.getTimeStamp());
                mFabStop.setEnabled(true);
            }
        });

        mFabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    unbindService(serviceConnection);
                    mServiceBound = false;
                }
               /* Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);*/
                mFabStop.setEnabled(false);
                mTxvClock.setText("SA PARAO TODO");
            }
        });

        /**
         * ServiceConnection es la clase que gestiona la comunicación con el servicio y el cliente.
         * DEBEMOS TENER EN CUENTA.......
         * todo ¿qué hacemos cuando se inicia el servicio?
         * todo ¿qué hacemos cuando se detiene el servicio?
         */
        serviceConnection = new ServiceConnection() {
            /**
             * Método quye se llama cuando SE HA HECHO EL bindService() desde la activity que lo va a usar
             * @param name
             * @param service
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBoundService = ((BoundService.MyBinder) service).getService();
                mServiceBound = true;
            }

            /**
             * Método que se llama cuando se ha desvinculado el servicio o eliminado
             * con lo que no hace falta destruirlo (Porque ya se destruye y es redundante)
             * @param name
             */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceBound = false;
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Iniciamos el intent del BoundService
        Intent intent = new Intent(MainActivity.this, BoundService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        //
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(serviceConnection);
            mServiceBound = false;
        }
        //Intent intent = new Intent(MainActivity.this, BoundService.class);
        //stopService(intent);
    }
}
