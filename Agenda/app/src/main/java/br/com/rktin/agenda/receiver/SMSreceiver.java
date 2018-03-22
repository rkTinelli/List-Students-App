package br.com.rktin.agenda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.rktin.agenda.R;
import br.com.rktin.agenda.dao.AlunoDAO;

/**
 * Created by rktin on 3/22/2018.
 */

public class SMSreceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];

        String format = (String) intent.getSerializableExtra("format");

        SmsMessage message = SmsMessage.createFromPdu(pdu,format);
        String telefone = message.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);
        if (dao.ehAluno(telefone)){
            Toast.makeText(context, "Chegou sms !", Toast.LENGTH_LONG).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }
        dao.close();
    }
}
