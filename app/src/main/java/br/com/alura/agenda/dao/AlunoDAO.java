package br.com.alura.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda.models.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {

    private static final String TABELA = "ALUNOS";

    public AlunoDAO(Context context) {
        //Agenda - Nome do banco, não tabela.
        super(context, "Agenda", null, 2);
    }

    //Quando se usa a aplicação pela primeira vez.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE Alunos " +
                "(id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "telefone TEXT, " +
                "endereco TEXT, " +
                "site TEXT, " +
                "nota  REAL); ";

        sqLiteDatabase.execSQL(sql);

    }

    //Quando se modifica a aplicação e já existem usuários utilizando.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Alunos";

        sqLiteDatabase.execSQL(sql);

        onCreate(sqLiteDatabase);
    }

    public void inserir(Aluno aluno){

        SQLiteDatabase writable = getWritableDatabase();

        ContentValues values = pegaDados(aluno);

        writable.insert("Alunos", null, values);

        close();
    }

    public List<Aluno> getAlunos() {

        SQLiteDatabase readable = getReadableDatabase();

        List<Aluno> alunos = new ArrayList<>();

        Cursor cursor = readable.rawQuery("SELECT * FROM Alunos", null);

        while (cursor.moveToNext()) {

            Aluno aluno = new Aluno();
            aluno.setId(  cursor.getLong(cursor.getColumnIndex("id")) );
            aluno.setNome( cursor.getString(cursor.getColumnIndex("nome")) );
            aluno.setEndereco( cursor.getString(cursor.getColumnIndex("endereco")) );
            aluno.setTelefone( cursor.getString(cursor.getColumnIndex("telefone")) );
            aluno.setSite( cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota( cursor.getDouble(cursor.getColumnIndex("nota")));

            alunos.add(aluno);
        }
        cursor.close();
        close();

        return alunos;
    }

    public void deletar(Long alunoId) {
        SQLiteDatabase writable = getWritableDatabase();

        writable.delete("Alunos", "id = ?", new String[]{alunoId.toString()});
    }

    public void atualizar(Aluno aluno) {
        SQLiteDatabase writable = getWritableDatabase();

        ContentValues values = pegaDados(aluno);

        writable.update(TABELA, values, "id = ?", new String[]{aluno.getId().toString()});
    }

    @NonNull
    private ContentValues pegaDados(Aluno aluno) {

        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());

        return values;
    }

}
