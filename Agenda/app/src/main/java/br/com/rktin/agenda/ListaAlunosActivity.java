package br.com.rktin.agenda;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.rktin.agenda.dao.AlunoDAO;
import br.com.rktin.agenda.modelo.Aluno;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = findViewById(R.id.lista_alunos);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);

                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });
        
        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });
        
        registerForContextMenu(listaAlunos);
    }

    @Override
    protected void onResume() {
        carregaLista();
        super.onResume();
    }

    private void carregaLista() {

        //instanciando o DAO e busca os alunos
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();
        //instanciando o DAO e busca os alunos
        
        ArrayAdapter<Aluno> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        //fazer uma ligação
        MenuItem ligar = menu.add("Ligar");


        //Mandar mensagem SMS
        MenuItem sms = menu.add("Sms");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms" + aluno.getTelefone()));
        sms.setIntent(intentSMS);

        //acessar localização no mapa
        MenuItem mapa = menu.add("Mapa");
        Intent intentMAPA = new Intent(Intent.ACTION_VIEW);
        intentMAPA.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        mapa.setIntent(intentMAPA);

        //Acessar o site cadastrado
        MenuItem irParaSite = menu.add("Ir Para Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

            //verificação do site
            String site = aluno.getSite();
            if(!site.startsWith("http://")) {
                site = "http://" + site;
            }

        intentSite.setData(Uri.parse(site));
        irParaSite.setIntent(intentSite);

        //deletar aluno
        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();

                carregaLista();

                return false;
            }
        });
    }

}
