package com.keynes.organizador.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.keynes.organizador.R;
import com.keynes.organizador.config.ConfiguracaoFirebase;
import com.keynes.organizador.model.Usuario;

public class CadatroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadatro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                //Validar se os campos foram preenchidos
                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);

                            CadastrarUsuario();

                        }
                        else {
                            Toast.makeText(CadatroActivity.this,"Preencha a senha!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(CadatroActivity.this,"Preencha o e-mail!",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CadatroActivity.this,"Preencha o nome!",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void CadastrarUsuario(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(CadatroActivity.this,"Sucesso ao cadastrar usuário!",Toast.LENGTH_SHORT).show();
                    finish();


                } else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    } catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadatroActivity.this,excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
