package br.com.rktin.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.rktin.agenda.dao.AlunoDAO;
import br.com.rktin.agenda.modelo.Aluno;
import br.com.rktin.converter.AlunoConverter;

/**
 * Created by rktin on 3/26/2018.
 */

public class EnviaAlunos extends AsyncTask<Object, Object, String> {
    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunos(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context,"Aguarde", "Enviado aluno...", true, true);
    }

    @Override
    protected String doInBackground(Object... objects) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converte_para_JSON(alunos);

        WebClient webClient = new WebClient();
        String resposta = webClient.post(json);
        return resposta;
    }


    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta , Toast.LENGTH_LONG).show();
    }
}
